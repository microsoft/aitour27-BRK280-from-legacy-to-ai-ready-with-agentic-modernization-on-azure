# Delivery resources

Presenter, re-delivery, and train-the-trainer materials for this session.

## Core materials

| Item | Link | Notes |
|---|---|---|
| Delivery deck | Link with the AI Tour local team | The four demo videos are embedded in the deck. |
| Session recording |  | Optional URL when available. The train-the-trainer (TTT) recording walks through the full session. |
| Attendee landing page | [Session README](../README.md) | Public starting point |
| Workshop/lab instructions | [Instructions](../src/README.md) | Attendee hands-on: upgrade one Java app and one .NET app, one at a time. Includes the links to demo videos 0–3. |

## Delivery checklist

- Review the session README
- Review the attendee instructions
- Open the deck
- Review the presenter guidance below
- Review live demo reproducibility guidance
- Validate any required environment or setup
- Watch demo videos 0–3 and the TTT recording end to end
- Reconfirm the availability label (GA / Preview) of every capability you name, since labels are point-in-time
- Check that every aka.ms link in the deck and this repo resolves

## Session preparation

- Review the attendee entry point from the root README.
- Review the delivery deck.
- Validate the required environment and setup.
- Download the demo videos locally so you don't depend on venue Wi-Fi.
- Know the wording rule: **modernization** is the whole journey (assess, plan, upgrade, migrate), and what the demos execute and attendees do in this repo is **upgrading**. Some deck speaker notes still say "modernize" in places; use "upgrade" when you describe what the demos and the lab do.

## Run of show

45-minute breakout. All demos are **pre-recorded videos**, and they carry the session. Your job while they play is **color commentary, not play-by-play**. The audience can see what's on screen, so tell them *why it matters*.

| Segment | Content | Minutes |
|---|---|---|
| 1 | Why modernization needs agents now (slides) | ~9 |
| 2 | Video 0 — Current situation | ~2 |
| 3 | Video 1 — Assess at scale | ~6 |
| 4 | Video 2 — Govern and customize | ~3 |
| 5 | Video 3 — Execute end-to-end | ~7 |
| 6 | Recap, capabilities, and calls to action | ~5 |
| — | Transitions, buffer, and Q&A | remaining |

Never cut the recap and calls to action.

**Video 0 — Current situation.** Shows the Caldova portal, the Java Payment Gateway (Java 8, Struts 1.3, XML routing, hand-written HTTP and SQL), and the .NET Patient Account Manager (.NET Framework 4.8, `System.Web`, plain-text credentials in `web.config`). Land it: *"Everything works, and that's exactly the problem. Because it works, it never gets prioritized."* Then widen to all 22 apps (11 Java, 11 .NET Framework): assessing them one at a time is a queue, not a strategy.

**Video 1 — Assess at scale.** Shows a JSON file with one entry per repo, the Modernize CLI assessment sent to cloud agents, progress in GitHub Agents HQ, and a pull request per repo (assessment overview, architecture doc, dependency and API inventory, security findings). Land it: *the portfolio is an input, not a click path; the fan-out is the product.* Bridge: the `web.config` connection string from Video 0 shows up as a finding, which leads into governance. Close: the same config runs headless in a pipeline, so assessment becomes continuous.

**Video 2 — Govern and customize.** Shows the Skills Library (`pii-handling`, `azure-managed-identity`) covering target architecture, security, and compatibility. Land it: *Skills are procedure (how we make it true).*

**Video 3 — Execute end-to-end.** Shows the Payment Gateway plan (Java 21 + Spring Boot 3, from the assessment and the rulebook), phase-by-phase commits, and `PaymentAction` running as a Spring controller that still answers on `/makePayment.do`. Land it: nothing merges on its own, and one app is shown on stage because of time, not capability. The same loop works for .NET. Close with **aka.ms/ghcp-modernization** and the repo link **aka.ms/aitour27/BRK280**.

**Likely questions**

- *"Why `/loans/` or `.do` URLs?"* They're frozen public route contracts. The framework changed; the product didn't.
- *"The UI still looks old."* On purpose. Migration isn't redesign!
- *"What about the other 21 apps?"* They went through the same assessment fan-out in Video 1.
- *"Is Caldova real?"* No. It's a fictional legacy portfolio in the shape most enterprises run.

**Avoid:** touring app features, reading diffs line by line, calling Caldova production software, presenting containers as part of the legacy apps, and running the modernization agent live.

## Demo reproducibility

There are no live demos. All four demos are pre-recorded and embedded in the deck.

The full 22-app Caldova portfolio shown in the videos isn't published. To reproduce the core upgrade step, use this repo: [`src/`](../src/README.md) walks through upgrading `CaldovaPaymentGateway` (Java 8 → Java 21), the same app upgraded in Video 3, and `CaldovaDrugPricingService` (.NET Framework 4.8 → .NET 10). Each app uses its own JSON config under `modernize/`.

## Setup notes

- Delivering the session needs only the deck and the downloaded videos. No Azure subscription or demo environment is required.
- If you want more experience, try the hands-on part yourself: install the Modernize CLI, run `gh auth login`, and have JDK 21 (Java app) or Windows with the .NET 10 SDK (.NET app). 

## Support

Content owner or contact: Pablo Lopes. Open an issue in this repo.
