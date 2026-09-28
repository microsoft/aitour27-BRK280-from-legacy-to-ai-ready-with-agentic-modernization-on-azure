<a name="start-building"></a>

<p align="center">
<img src="img/banner-ai-tour-27.png" alt="Microsoft AI Tour 2027" width="100%"/>
</p>

# [Microsoft AI Tour 2027](https://aitour.microsoft.com)

### Session description

Almost every enterprise has a legacy portfolio that everyone knows needs work and nobody has time to touch. In this session you'll watch GitHub Copilot modernization assess a 22-application Java and .NET Framework portfolio in parallel, follow your organization's own rules and custom skills, and then upgrade a real application, with a person reviewing every step. This repo has the session's demo videos and a hands-on path so you can run the same upgrade yourself.

### 🎬 Session videos

All demos in this session are pre-recorded. The videos are the main reference for the session, so watch them in order:

| # | Video | What you'll see | Watch |
|---|---|---|---|
| 0 | **Current situation** | The Caldova legacy portfolio: 22 apps (11 Java, 11 .NET Framework). A Java 8 / Struts 1.x payment gateway and a .NET Framework 4.8 Web Forms app, including the problems hiding in each one. | At Slide |
| 1 | **Assess at scale** | One JSON file lists the whole portfolio. The Modernize CLI sends the assessment to cloud agents in parallel, and every repo gets a pull request with architecture docs, dependencies, and security findings. | At Slide |
| 2 | **Govern and customize** | A central skills library (PII handling, managed identity), which sets how the organization wants the work done. | At Slide |
| 3 | **Execute end-to-end** | The Payment Gateway is upgraded to Java 21 and Spring Boot 3 using the assessment. The agent commits one reviewable phase at a time, and the public routes stay the same. | At Slide |


### 🚀 Getting started

#### In a guided session

If you're following along during a live session:

1. Watch the four demo videos as the presenter narrates them. Nothing needs to be installed during the session.
2. Bookmark this repo at **aka.ms/aitour27/BRK280** (or scan the QR code on the closing slide).
3. Optional: After understanding the session, open [`src/`](src/README.md) to run the upgrade on your own machine.

#### On your own

If you're learning at your own pace:

1. Watch the session videos in order, 0 → 3.
2. Clone this repository and install the Modernize CLI;
3. Follow [`src/`](src/README.md) to upgrade one Java app and one .NET app, one at a time.

> **Why only two apps?** The session portfolio has 22 applications. To keep the hands-on part short, this repo includes **one Java back end** (`CaldovaPaymentGateway`) and **one .NET back end** (`CaldovaDrugPricingService`), each with its own JSON config. The workflow is the same one you see in the videos; only the scale is smaller.

### 🎯 Learning outcomes

By the end of this session, you will be able to:

- Explain why assessment comes first, and run a portfolio assessment from a single JSON config file
- Use custom skills so agent output follows your organization's standards
- Upgrade a Java 8 app to Java 21 and a .NET Framework 4.8 app to .NET 10 with the Modernize CLI, and review the result like any pull request

### 💻 Technologies used

- GitHub Copilot modernization (modernization agent and Modernize CLI)
- GitHub Copilot in VS Code and Visual Studio, plus GitHub cloud agents
- Custom skills
- Java 8 / Struts 1.x → Java 21 / Spring Boot 3
- .NET Framework 4.8 → .NET 10

### 📚 Continue your learning

Pick your next step based on your learning style:

| Resource | What you'll get |
|----------|-----------------|
| **[GitHub Copilot modernization docs](https://aka.ms/ghcp-modernization)** | The official starting point for assessments, upgrades, custom skills, and the Modernize CLI |
| **[.NET Modernization for Beginners](https://aka.ms/ghcp-appmod/dotnet-mod-beginners)** | A guided, step-by-step course for modernizing a .NET app with GitHub Copilot |
| **[What's new in agentic modernization](https://aka.ms/agentic-modernization/build-blog)** | The full list of new features |
| **[Microsoft Learn](https://learn.microsoft.com)** | Official documentation and guided learning paths on these topics |

### 🌟 Microsoft Learn MCP Server

The Microsoft Learn MCP Server gives your AI agent direct access to Microsoft's official documentation, with grounded, up-to-date answers about the topics in this session, including GitHub Copilot modernization, Java, and .NET upgrades.

**GitHub Copilot CLI**: install with

```shell
copilot plugin install microsoftdocs/mcp
```

**VS Code**: one-click install  
[![Install in VS Code](https://img.shields.io/badge/VS_Code-Install_Microsoft_Learn_MCP-0098FF?style=flat-square&logo=visualstudiocode&logoColor=white)](https://vscode.dev/redirect/mcp/install?name=microsoft-learn&config=%7B%22type%22%3A%22http%22%2C%22url%22%3A%22https%3A%2F%2Flearn.microsoft.com%2Fapi%2Fmcp%22%7D)

For more information, visit the [Learn MCP Server repo](https://aka.ms/learnmcp).

### 👥 Content owners

<table>
<tr>
    <td align="center"><a href="http://github.com/PabloNunes">
        <sub><b>Pablo Lopes</b></sub></a><br />
            <a href="https://github.com/PabloNunes" title="talk">📢</a>
    </td>
    <td align="center"><a href="http://github.com/codemillmatt">
        <sub><b>Matt Soucoup</b></sub></a><br />
            <a href="https://github.com/codemillmatt" title="talk">📢</a>
    </td>
</tr></table>

### Deliver this session

Presenters and re-delivery partners can find the deck, the demo videos, the train-the-trainer recording, presenter notes, and delivery guidance in [`delivery-resources/`](delivery-resources/README.md).

### ⚖️ Trademarks

This project may contain trademarks or logos for projects, products, or services. Authorized use of Microsoft trademarks or logos is subject to and must follow [Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/legal/intellectualproperty/trademarks/usage/general). Use of Microsoft trademarks or logos in modified versions of this project must not cause confusion or imply Microsoft sponsorship.

Any use of third-party trademarks or logos are subject to those third-party's policies.