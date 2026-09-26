package com.caldova.paymentgateway;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class PharmacyCopayServlet extends HttpServlet {
    private static final BigDecimal UNIT_PRICE = new BigDecimal("1.50");
    private static final BigDecimal COPAY_RATE = new BigDecimal("0.20");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PharmacyCopayRequest copayRequest;
        try {
            copayRequest = PharmacyCopayRequest.parse(readBody(request));
        } catch (JSONException | IllegalArgumentException exception) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "INVALID_REQUEST", exception.getMessage());
            return;
        }

        BigDecimal copayAmount = UNIT_PRICE
            .multiply(BigDecimal.valueOf(copayRequest.quantity))
            .multiply(COPAY_RATE)
            .setScale(2, RoundingMode.HALF_UP);
        String claimNumber = buildClaimNumber(copayRequest);
        String ledgerBody = "{" +
            "\"debitAccountId\":" + PaymentGatewayConfig.getPharmacyCopayDebitAccountId() + "," +
            "\"creditAccountId\":" + PaymentGatewayConfig.getPharmacyCopayCreditAccountId() + "," +
            "\"amount\":" + copayAmount.toPlainString() + "," +
            "\"description\":\"" + escape(
                "Pharmacy copay customer " + copayRequest.customerId + " " +
                    copayRequest.prescriptionNumber + " quantity " + copayRequest.quantity
            ) + "\"," +
            "\"referenceNumber\":\"" + escape(claimNumber) + "\"" +
            "}";

        try {
            LedgerResponse ledgerResponse = postToClaimsLedger(ledgerBody);
            JSONObject result = new JSONObject();
            result.put("status", "POSTED");
            result.put("ledgerPosted", true);
            result.put("claimNumber", claimNumber);
            result.put("copayAmount", copayAmount);
            result.put("transactionId", ledgerResponse.transactionId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(result.toString());
        } catch (DependencyException exception) {
            writeError(response, exception.status, exception.code, exception.getMessage());
        }
    }

    private LedgerResponse postToClaimsLedger(String body) throws DependencyException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(PaymentGatewayConfig.getPharmacyClaimsUrl()).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            byte[] requestBody = body.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(requestBody.length);
            try (OutputStream output = connection.getOutputStream()) {
                output.write(requestBody);
            }

            int status = connection.getResponseCode();
            String responseBody = readResponse(connection, status);
            if (status == HttpServletResponse.SC_CONFLICT) {
                throw new DependencyException(
                    HttpServletResponse.SC_CONFLICT,
                    "LEDGER_REFERENCE_CONFLICT",
                    "Claims Ledger rejected the pharmacy claim reference."
                );
            }
            if (status != HttpServletResponse.SC_OK) {
                throw new DependencyException(
                    HttpServletResponse.SC_BAD_GATEWAY,
                    "LEDGER_REJECTED",
                    "Claims Ledger rejected the pharmacy copay."
                );
            }

            JSONObject payload = new JSONObject(responseBody);
            if (!"POSTED".equalsIgnoreCase(payload.optString("status")) ||
                payload.optLong("transactionId", 0L) <= 0L) {
                throw new DependencyException(
                    HttpServletResponse.SC_BAD_GATEWAY,
                    "LEDGER_INVALID_RESPONSE",
                    "Claims Ledger returned an invalid pharmacy claim response."
                );
            }
            return new LedgerResponse(payload.getLong("transactionId"));
        } catch (JSONException exception) {
            throw new DependencyException(
                HttpServletResponse.SC_BAD_GATEWAY,
                "LEDGER_INVALID_RESPONSE",
                "Claims Ledger returned an invalid pharmacy claim response."
            );
        } catch (IOException exception) {
            throw new DependencyException(
                HttpServletResponse.SC_BAD_GATEWAY,
                "LEDGER_UNAVAILABLE",
                "Claims Ledger was unavailable for the pharmacy copay."
            );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String buildClaimNumber(PharmacyCopayRequest request) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String canonical = request.idempotencyKey;
            byte[] bytes = digest.digest(canonical.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder("CLM-");
            for (int index = 0; index < 18; index++) {
                result.append(String.format(Locale.ROOT, "%02X", bytes[index]));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    private static String readBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line = reader.readLine();
        while (line != null) {
            body.append(line);
            line = reader.readLine();
        }
        return body.toString();
    }

    private static String readResponse(HttpURLConnection connection, int status) throws IOException {
        InputStream stream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();
        if (stream == null) {
            return "";
        }
        try (InputStream input = stream; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static void writeError(HttpServletResponse response, int status, String code, String message)
        throws IOException {
        response.setStatus(status);
        JSONObject body = new JSONObject();
        body.put("status", "ERROR");
        body.put("errorCode", code);
        body.put("message", message == null ? "Request could not be processed." : message);
        response.getWriter().write(body.toString());
    }

    private static String escape(String value) {
        return value == null ? "" : value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n");
    }

    private static final class PharmacyCopayRequest {
        private int customerId;
        private String prescriptionNumber;
        private int quantity;
        private String idempotencyKey;

        private static PharmacyCopayRequest parse(String raw) throws JSONException {
            JSONObject payload = new JSONObject(raw == null ? "" : raw);
            PharmacyCopayRequest request = new PharmacyCopayRequest();
            request.customerId = payload.optInt("customerId", 0);
            request.prescriptionNumber = payload.optString("prescriptionNumber", "").trim();
            request.quantity = payload.optInt("quantity", 0);
            request.idempotencyKey = payload.optString("idempotencyKey", "").trim();

            if (request.customerId <= 0 || request.quantity <= 0) {
                throw new IllegalArgumentException("customerId and quantity must be greater than zero.");
            }
            if (request.prescriptionNumber.length() == 0 || request.prescriptionNumber.length() > 40) {
                throw new IllegalArgumentException("prescriptionNumber is required and must be at most 40 characters.");
            }
            if (request.idempotencyKey.length() == 0 || request.idempotencyKey.length() > 200) {
                throw new IllegalArgumentException("idempotencyKey is required and must be at most 200 characters.");
            }
            return request;
        }
    }

    private static final class LedgerResponse {
        private final long transactionId;

        private LedgerResponse(long transactionId) {
            this.transactionId = transactionId;
        }
    }

    private static final class DependencyException extends Exception {
        private final int status;
        private final String code;

        private DependencyException(int status, String code, String message) {
            super(message);
            this.status = status;
            this.code = code;
        }
    }
}
