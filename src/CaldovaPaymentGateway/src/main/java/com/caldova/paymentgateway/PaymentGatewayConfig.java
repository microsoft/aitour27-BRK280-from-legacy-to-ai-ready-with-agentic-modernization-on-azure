package com.caldova.paymentgateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PaymentGatewayConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            InputStream inputStream = PaymentGatewayConfig.class.getClassLoader().getResourceAsStream("payment-gateway.properties");
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
                inputStream.close();
            }
        } catch (IOException ignored) {
        }
    }

    private PaymentGatewayConfig() {
    }

    public static String getDbUrl() {
        String host = read("DB_HOST", "db.host");
        String port = read("DB_PORT", "db.port");
        String name = read("DB_NAME", "db.name");
        return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + name + ";encrypt=false;trustServerCertificate=true";
    }

    public static String getDbUser() {
        return read("DB_USER", "db.user");
    }

    public static String getDbPassword() {
        return read("DB_PASSWORD", "db.password");
    }

    public static String getSsoCookieName() {
        return read("SSO_COOKIE_NAME", "sso.cookie.name");
    }

    public static String getAuthGatewayWhoAmIUrl() {
        return read("AUTH_GATEWAY_WHOAMI_URL", "auth.gateway.whoami.url");
    }

    public static String getLedgerUrl() {
        return read("LEDGER_URL", "ledger.url");
    }

    public static String getPharmacyClaimsUrl() {
        return read("PHARMACY_CLAIMS_URL", "pharmacy.claims.url");
    }

    public static int getPharmacyCopayDebitAccountId() {
        return getInt("PHARMACY_COPAY_DEBIT_ACCOUNT_ID", "pharmacy.copay.debit.account.id", 1);
    }

    public static int getPharmacyCopayCreditAccountId() {
        return getInt("PHARMACY_COPAY_CREDIT_ACCOUNT_ID", "pharmacy.copay.credit.account.id", 2);
    }

    public static int getSettlementAccountId() {
        String value = read("LEDGER_SETTLEMENT_ACCOUNT_ID", "ledger.settlement.account.id");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }

    private static int getInt(String envKey, String propertyKey, int defaultValue) {
        String value = read(envKey, propertyKey);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return defaultValue;
        }
    }

    private static String read(String envKey, String propertyKey) {
        String value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        return PROPERTIES.getProperty(propertyKey, "");
    }
}
