# Caldova Drug Pricing Service

[Back to the root instructions](../README.md)

## Pharmacy role

Legacy drug pricing rate lookup used by settlement-adapter compatibility flows.
The service exposes the original SOAP identity while the application-facing
description remains pharmacy pricing.

## Runtime and technology

- WCF service on .NET Framework 4.8.
- Mono 6.12 with XSP4.
- Basic HTTP SOAP and WSDL metadata.

## Service routes

- SOAP/WSDL route: `/services/currency/CurrencyService.svc?wsdl`.
- Health: `GET /services/currency/health` through Nginx.
- The `/services/currency/` prefix and `CurrencyService.svc` name are retained
  compatibility identifiers.

This is an API-only service and has no human-facing browser UI or screenshot.

## Key dependencies

- `sqlserver` / `CaldovaDB` seeded pricing-rate data.
- No in-estate service calls this endpoint;
  unmatched REST behavior is documented as a fallback.

## Local build and run

From the repository root:

```bash
docker compose build caldova-drug-pricing-service
docker compose up -d caldova-drug-pricing-service
```

The WSDL and health route are also covered by `bash tests/m1-smoke-tests.sh`.
