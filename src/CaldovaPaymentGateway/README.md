# Caldova Payment Gateway

[Back to the root instructions](../README.md)

## Pharmacy role

Legacy Java screen for posting prescription, copay, and patient-account
payments, then reviewing payment history. The payment action posts to the
Claims Ledger rather than performing claims adjudication.

## Runtime and technology

- Java 8 WAR built with Gradle 7.6.
- Tomcat 9 with Struts 1.x, JSP, JSTL, SQL Server JDBC, and JSON.

## Browser route and API behavior

- UI: `/payments/login.do`, `/payments/makePayment.do`, and
  `/payments/paymentHistory.do`.
- Pharmacy API: `POST /payments/api/pharmacy/copay` through Nginx; direct
  service path is `/api/pharmacy/copay` on port `9001`.
- Health action: `/payments/health.do`.
- Root-deployed `.do` actions such as `/makePayment.do` are redirected by Nginx
  to the retained `/payments/` prefix.

![Payment gateway](../docs/assets/screenshots/caldova-payment-gateway.png)

## Key dependencies

- `caldova-claims-ledger` for payment posting.
- `caldova-auth-gateway` shared session-token validation.
- `sqlserver` / `CaldovaDB` for account and payment history.

## Local build and run

From the repository root:

```bash
docker compose build caldova-payment-gateway
docker compose up -d caldova-payment-gateway
```

Open <http://localhost/payments/> with an authenticated portal session.
