# Source code

[← Back to the session README](../README.md) · [Attendee instructions](../delivery-resources/README.md)

This folder has the two legacy applications you upgrade in the BRK280 hands-on. Both come from **Caldova**, the fictional 22-application portfolio used in the session videos. We copied out **one Java back end and one .NET back end** so an upgrade fits in one sitting.

| Folder | Application | Starts on | Upgrade target | JSON config |
|---|---|---|---|---|
| [`java/CaldovaPaymentGateway/`](java/CaldovaPaymentGateway/) | Payment Gateway | Java 8, Struts 1.3, JSP, Gradle | Java 21 | [`../modernize/java-upgrade.json`](../modernize/java-upgrade.json) |
| [`dotnet/CaldovaDrugPricingService/`](dotnet/CaldovaDrugPricingService/) | Drug Pricing Service | .NET Framework 4.8, WCF | .NET 10 | [`../modernize/dotnet-upgrade.json`](../modernize/dotnet-upgrade.json) |

> [IMPORTANT]
> This code is **legacy on purpose**. The old frameworks, hand-written SQL, and plain-text configuration are the things the assessment should find and the upgrade should fix. Please don't "clean it up" before you run the lab.

---

## Java: `CaldovaPaymentGateway`

Posts prescription, copay, and patient-account payments and shows payment history. **This is the same app upgraded in Video 3.**

```text
java/CaldovaPaymentGateway/
├── build.gradle                         ← Java 8, Struts 1.3.10, JSTL, SQL Server JDBC, org.json (2014)
├── settings.gradle
└── src/main/
    ├── java/com/caldova/paymentgateway/
    │   ├── LoginAction.java             ← Struts Action: sign-in
    │   ├── LoginForm.java               ← Struts ActionForm for sign-in
    │   ├── PaymentAction.java           ← Struts Action: post a payment (the class Video 3 turns into a Spring controller)
    │   ├── PaymentForm.java             ← Struts ActionForm for payments
    │   ├── PaymentHistoryAction.java    ← Struts Action: list past payments
    │   ├── PaymentHistoryRecord.java    ← payment history row
    │   ├── PharmacyCopayServlet.java    ← raw servlet for the pharmacy copay API
    │   ├── HealthAction.java            ← health check action
    │   ├── SsoSessionService.java       ← validates the shared sign-in cookie
    │   ├── SessionUser.java             ← signed-in user
    │   ├── AccountOption.java           ← account picker item
    │   ├── PaymentGatewayConfig.java    ← reads settings from the properties file
    │   └── PaymentGatewayConnectionFactory.java ← opens JDBC connections by hand
    ├── resources/
    │   └── payment-gateway.properties   ← database, sign-in, and ledger settings
    └── webapp/
        ├── index.jsp
        ├── health.jsp
        └── WEB-INF/
            ├── struts-config.xml        ← every route declared in XML (/login, /makePayment, /paymentHistory, /health)
            ├── web.xml
            └── jsp/
                ├── login.jsp
                ├── payment.jsp
                └── payment-history.jsp
```

**What the assessment should flag**

- `build.gradle` targets **Java 8** and uses **Struts 1.3.10**, which reached end of life in 2013, plus a 2014 JSON library.
- Routing lives in XML (`struts-config.xml`) with `ActionForm` classes and `.do` paths.
- HTTP handling and SQL are written by hand, with no dependency injection.
- Database credentials sit in plain text in `payment-gateway.properties`.

**Public routes to keep:** `/makePayment.do`, `/paymentHistory.do`, `/login.do`, and `/health.do`. Other parts of the portfolio depend on them, so they're frozen public contracts. In Video 3 the app still answers on `/makePayment.do` after the upgrade.

---

## .NET: `CaldovaDrugPricingService`

A drug pricing rate lookup exposed as a SOAP service. It has no user interface, so an upgrade can't break a screen.

```text
dotnet/CaldovaDrugPricingService/
├── CaldovaDrugPricingService.csproj     ← old-style project file, TargetFrameworkVersion v4.8
├── packages.config                      ← old NuGet package format
├── web.config                           ← connection string, WCF endpoint, debug settings
├── IDrugPricingService.cs               ← WCF service contract (GetRate, GetSupportedCurrencies, ConvertAmount)
├── DrugPricingService.cs                ← service implementation
├── DrugPricingRateData.cs               ← data contract returned by GetRate
├── DbConfig.cs                          ← builds the connection string from environment variables or web.config
├── CurrencyService.svc                  ← WCF endpoint file
├── Health.ashx / Health.ashx.cs         ← health check handler
└── Default.aspx                         ← placeholder landing page
```

**What the assessment should flag**

- The project targets **.NET Framework 4.8** and references `System.Web` and `System.ServiceModel`, which don't run on modern .NET as they are.
- The service is **WCF over SOAP** (`basicHttpBinding`).
- `web.config` has database credentials in plain text, `debug="true"`, `customErrors="Off"`, and exception details returned to callers.

---

## The JSON configs

Each app has **its own** config file. The format is the same one you see at the start of **Video 1**: in the video it lists all 22 apps, and here each file lists one.

**`modernize/java-upgrade.json`**

```json
{
  "repos": [
    {
      "name": "CaldovaPaymentGateway",
      "path": "REPLACE-WITH-ABSOLUTE-PATH/src/java/CaldovaPaymentGateway",
      "description": "Java 8 + Struts 1.x payment service to upgrade to Java 21"
    }
  ]
}
```

**`modernize/dotnet-upgrade.json`**

```json
{
  "repos": [
    {
      "name": "CaldovaDrugPricingService",
      "path": "REPLACE-WITH-ABSOLUTE-PATH/src/dotnet/CaldovaDrugPricingService",
      "description": ".NET Framework 4.8 WCF pricing service to upgrade to .NET 10"
    }
  ]
}
```

| Field | What it means |
|---|---|
| `name` | Friendly name shown in reports |
| `path` | **Absolute** path to the app folder on your machine. Replace `REPLACE-WITH-ABSOLUTE-PATH-TO-THIS-REPO` with where you cloned this repo. |
| `url` | Use this **instead of** `path` to point at a github.com repo, which is required for cloud agents. Add `"branch": "main"` alongside it. |
| `description` | Optional note for people reading the file |

Use them like the videos show or test locally! 
Learn how to do a step-by-step at the main documentation for the [Modernize CLI](https://learn.microsoft.com/en-us/azure/developer/github-copilot-app-modernization/modernization-agent/overview)

---

## Good to know

- **These apps won't run on their own.** In the full Caldova portfolio they depend on a shared SQL Server database (`CaldovaDB`), a sign-in service, and a claims ledger, and none of those are in this repo. You don't need them. The upgrade works on the source code and checks that it builds.
- **The credentials are fake.** The database settings in `payment-gateway.properties` and `web.config` are demo values, left in on purpose so the assessment has something to find. Never use real secrets in a lab.
- **Caldova is fictional.** Some names (`/loans/`, `.ZAVAAUTH`, `zava.bank`) are older identifiers kept for compatibility. They don't mean the app is anything other than a demo pharmacy scenario.
