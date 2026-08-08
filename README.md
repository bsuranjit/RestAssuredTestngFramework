# 🎵 Spotify API Test Automation Framework

[![Spotify API Tests](https://github.com/bsuranjit/RestAssuredTestngFramework/actions/workflows/maven-tests.yml/badge.svg)](https://github.com/bsuranjit/RestAssuredTestngFramework/actions)

> Enterprise-grade REST API automation framework built from scratch using Java, REST Assured, and TestNG — tested against the **live Spotify Web API** with full OAuth2 token management, parallel execution, Docker containerization, and dual CI/CD integration (Jenkins + GitHub Actions).

---

## 📋 Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Framework Architecture](#framework-architecture)
- [Key Features](#key-features)
- [Engineering Challenges & Solutions](#️-engineering-challenges--solutions)
- [Project Structure](#project-structure)
- [Design Patterns](#design-patterns)
- [Test Scenarios](#test-scenarios)
- [How to Run Locally](#how-to-run-locally)
- [Docker](#docker)
- [CI/CD Pipelines](#cicd-pipelines)
- [Test Reports](#test-reports)
- [Author](#author)

---

## Overview

This framework automates REST API testing for the [Spotify Web API](https://developer.spotify.com/documentation/web-api/) — a real-world, OAuth2-protected production API. It was designed and built from scratch to demonstrate enterprise-level API test automation practices including automated token management, reusable request specifications, POJO-based serialization, parallel test execution, Docker containerization, and dual CI/CD integration across both self-hosted (Jenkins) and cloud-native (GitHub Actions) pipelines.

**Live CI/CD build results:**
```
Tests run: 5 | Failures: 0 | Errors: 0 | Skipped: 0
BUILD SUCCESS — Total time: 22 seconds (Docker) / 27 seconds (Jenkins) / 22 seconds (GitHub Actions)
```

---

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 11 | Core language |
| REST Assured | 5.4.0 | API automation DSL |
| TestNG | 7.10.2 | Test framework & parallel execution |
| Jackson Databind | 2.17.2 | JSON serialization / deserialization |
| Lombok | 1.18.32 | Boilerplate code reduction |
| Allure | 2.27.0 | Test reporting |
| JavaFaker | 1.0.2 | Dynamic test data generation |
| AspectJ | 1.9.22 | Allure + TestNG integration |
| Maven Surefire | 3.2.5 | Test runner & parallel config |
| Docker | Latest | Containerized test execution |
| Jenkins | Latest | Self-hosted CI/CD pipeline |
| GitHub Actions | Latest | Cloud-native CI/CD pipeline |
| GitHub | - | Version control & pipeline trigger |

---

## Framework Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    TEST LAYER                           │
│              PlayListTest.java                          │
│         (Business-readable test cases)                  │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│               APPLICATION API LAYER                     │
│                PlaylistApi.java                         │
│      (Abstracts HTTP calls from test logic)             │
└──────────┬───────────────────────────┬──────────────────┘
           │                           │
┌──────────▼───────────┐  ┌────────────▼────────────────┐
│     API LAYER        │  │        POJO LAYER            │
│  SpecBuilder.java    │  │  Playlist / Owner /          │
│  TokenManager.java   │  │  Tracks / Error.java         │
│  RestResource.java   │  │  (Serialization models)      │
│  Route.java          │  └─────────────────────────────┘
│  StatusCode.java     │
└──────────┬───────────┘
           │
┌──────────▼───────────────────────────────────────────┐
│                  UTILS LAYER                          │
│   ConfigLoader | DataLoader | FakerUtils |            │
│   PropertyUtils                                       │
│   (Config management, test data, utilities)           │
└───────────────────────────────────────────────────────┘
```

---

### CI/CD & Execution Flow

The diagram below shows the complete end-to-end execution flow — from a GitHub push all the way through to the Allure report. This project runs on **two independent CI/CD systems in parallel** on every push:

```mermaid
graph TD
    GH[📦 GitHub Repository] -->|Push triggers build| J[⚙️ Jenkins CI/CD Pipeline]
    GH -->|Push triggers workflow| GA[☁️ GitHub Actions]
    J -->|docker build + mvn clean test| D
    GA -->|mvn clean test on GitHub-hosted runner| D

    subgraph D[🐳 Test Execution]
        TS[🧪 TestNG Suite\n5 Parallel Threads]
        TS --> PA[PlaylistApi\nApplication Layer - Facade Pattern]
        PA --> SB[SpecBuilder\nReusable Request Setup]
        PA --> TM[🔐 TokenManager\nOAuth2 Singleton Pattern]
        SB --> RR[REST Assured\nHTTP Client]
        TM -->|Auto-renews token on 401| RR
    end

    RR -->|GET / POST / PUT| SPOT[🎵 Spotify Web API\napi.spotify.com]
    SPOT -->|JSON Response| RR
    RR -->|Jackson Deserialize| POJO[POJO Layer\nPlaylist · Owner · Tracks]
    POJO -->|Assert results| RESULT[✅ Test Results\n5/5 PASS]
    RESULT -->|Generate| AR[📊 Allure Report\n100% Pass Rate]

    style GH fill:#24292e,color:#fff
    style J fill:#2E75B6,color:#fff
    style GA fill:#2088FF,color:#fff
    style D fill:#1D9E75,color:#fff
    style SPOT fill:#1DB954,color:#fff
    style TM fill:#C0392B,color:#fff
    style AR fill:#1D9E75,color:#fff
    style RESULT fill:#1D9E75,color:#fff
```

**Flow summary:**
- **GitHub push** → triggers both Jenkins (self-hosted) and GitHub Actions (cloud-native) simultaneously
- **Jenkins** → builds Docker image and runs `mvn clean test` on a local Windows server
- **GitHub Actions** → runs `mvn clean test` on a GitHub-hosted Ubuntu runner, credentials injected via GitHub Secrets
- **Docker** → provides identical execution environment on any machine
- **TokenManager** → automatically renews OAuth2 token when expired (no manual intervention)
- **5 parallel threads** → execute simultaneously with zero data collision
- **Allure** → generates visual report with full request/response details

---


## Key Features

### ✅ Automated OAuth2 Token Management
The `TokenManager` class automatically detects when an access token has expired (HTTP 401 response) and renews it by firing a POST request to the Spotify token endpoint — without any manual intervention. This means the CI/CD pipelines and Docker container can run on any schedule without breaking due to token expiry.

```
Flow: API Call → 401 Detected → POST /api/token → Fresh Token → Retry
```

### ✅ Docker Containerization
The framework runs inside a Docker container — guaranteeing identical test execution on any machine, any OS, and any CI/CD environment. No "works on my machine" problems.

```bash
docker build -t spotify-test-framework .
docker run -e CLIENT_ID=xxx -e CLIENT_SECRET=xxx spotify-test-framework
```

### ✅ Reusable REST Assured Specifications
`SpecBuilder.java` provides centralized, reusable `RequestSpecification` and `ResponseSpecification` objects — eliminating duplication across all test classes and ensuring consistent headers, base URIs, and content types.

### ✅ POJO-Based Serialization & Deserialization
All request bodies and response payloads are mapped to Java POJOs using Jackson Databind. Lombok annotations (`@Getter`, `@Setter`, `@Builder`) eliminate boilerplate getter/setter code.

### ✅ Parallel Test Execution
Tests run across 5 concurrent threads via Maven Surefire configuration — significantly reducing total execution time compared to sequential runs.

### ✅ Dynamic Test Data Generation
`FakerUtils.java` uses JavaFaker to generate unique playlist names and descriptions for every test run — ensuring tests are never dependent on hardcoded static data.

### ✅ Positive & Negative Scenario Coverage
The framework tests both success paths and failure paths — including expired token handling (401), missing required field validation (400), and full CRUD operations (GET, POST, PUT).

### ✅ Allure Test Reporting
Allure reports provide rich visual test execution reports with request/response details, step-by-step breakdowns, and pass/fail history.

### ✅ Dual CI/CD Integration
The framework runs on **both** Jenkins (self-hosted) and GitHub Actions (cloud-native) simultaneously — demonstrating versatility across enterprise on-premise and modern cloud-native automation approaches. Both pipelines inject credentials securely via environment variables (Jenkins Build Parameters / GitHub Secrets), with zero secrets committed to source control.

---

## ⚙️ Engineering Challenges & Solutions

Building an enterprise-grade framework from scratch involves solving real-world constraints that tutorials never cover. Here are the core challenges encountered during development and exactly how each was resolved:

### 1. The "Token Expiry" Bottleneck

**Challenge:** The Spotify Web API requires OAuth2 authentication, and tokens expire frequently. Hardcoding tokens or manually refreshing them breaks any CI/CD or nightly scheduling, making it impossible to achieve "hands-off" automation.

**Solution:** I implemented a custom `TokenManager` class using the **Singleton Pattern**. The framework now performs a proactive "check-and-refresh" logic: if a request returns a `401 Unauthorized` status code, the `TokenManager` transparently handles the POST request to the token endpoint to fetch a new token, updates the header, and retries the original request — all without test interruption.

```
Flow: API Call → 401 Detected → TokenManager.renewToken() → Fresh Token → Retry → ✅ Pass
```

---

### 2. Ensuring "Works on My Machine" Parity Across Four Environments

**Challenge:** Developing on a local machine (Windows/Eclipse) often leads to environmental dependencies that fail when moved to a shared Jenkins server, a Docker container, or a cloud-hosted GitHub Actions runner (e.g., mismatched Java versions, missing config files, or stale credentials in one environment but not another).

**Solution:** I containerized the entire framework using **Docker**, and validated the exact same test suite across four independent environments: local Eclipse, Jenkins (self-hosted), Docker, and GitHub Actions (cloud-hosted). This surfaced — and resolved — a real-world class of bugs where the same root cause (an expired OAuth token) manifested differently in each environment: a stale git branch pointer, a cached Docker layer, a hardcoded Jenkins parameter, and a missing GitHub Secret. Refactored `ConfigLoader` to gracefully fall back across system properties → environment variables → local file, so the same codebase runs correctly whether credentials come from a local file, Docker `-e` flags, Jenkins build parameters, or GitHub Secrets.

| Environment | Tests | Result | Time |
|---|---|---|---|
| Local Eclipse | 5/5 | ✅ PASS | ~15s |
| Docker Container | 5/5 | ✅ PASS | 22s |
| Jenkins CI/CD | 5/5 | ✅ PASS | 27s |
| GitHub Actions | 5/5 | ✅ PASS | 22s |

---

### 3. Preventing Data Collision in Parallel Execution

**Challenge:** Running tests in parallel (5 concurrent threads via TestNG/Surefire) created the risk of data contention or race conditions — especially during authentication and playlist creation where hardcoded data would cause conflicts.

**Solution:**
- **Data Isolation:** Integrated `JavaFaker` to generate unique, random playlist names and descriptions for every execution, ensuring that concurrent threads never collide on test data.
- **Thread Safety:** Designed the `TokenManager` to be thread-safe using the Singleton pattern, preventing multiple threads from firing simultaneous refresh requests — which preserves API rate limits and prevents authentication errors.

---

### 4. Improving Debugging Efficiency

**Challenge:** When a test fails in a CI/CD pipeline, reading raw console logs is time-consuming and often lacks sufficient context for a quick root-cause analysis. Generic, swallowed exception messages made this worse — a caught exception that just says "failed to get token" hides whether the real cause is a bad credential, a network issue, or a missing config value.

**Solution:** I integrated **Allure Reporting** with the framework, using `@Step` annotations and automatic attachment of request/response payloads to turn CI/CD failure logs into rich, visual reports. I also refactored exception handling in `TokenManager` to surface the actual underlying error (HTTP status code and response body) instead of a generic message — cutting root-cause diagnosis time from hours to minutes in practice.

---

## Project Structure

```
src/
├── main/java/
│   └── RestAssuredFramework/
│       └── App.java
│
└── test/
    ├── java/
    │   └── com/spotify/oauth2/
    │       ├── api/
    │       │   ├── RestResource.java
    │       │   ├── Route.java
    │       │   ├── SpecBuilder.java
    │       │   ├── StatusCode.java
    │       │   ├── TokenManager.java
    │       │   └── applicationApi/
    │       │       └── PlaylistApi.java
    │       ├── pojo/
    │       │   ├── Error.java
    │       │   ├── ExternalUrls.java
    │       │   ├── InnerError.java
    │       │   ├── Owner.java
    │       │   ├── Playlist.java
    │       │   └── Tracks.java
    │       ├── tests/
    │       │   ├── BaseTest.java
    │       │   └── PlayListTest.java
    │       └── utils/
    │           ├── ConfigLoader.java
    │           ├── DataLoader.java
    │           ├── FakerUtils.java
    │           └── PropertyUtils.java
    └── resources/
        ├── config.properties.example
        ├── data.properties
        └── allure.properties

.github/
└── workflows/
    └── maven-tests.yml
```

> Note: `config.properties` (real credentials) is `.gitignore`'d and not committed. Copy `config.properties.example` to `config.properties` and fill in your own values to run locally.

---

## Design Patterns

### Singleton Pattern — `TokenManager`
Ensures only one token renewal process runs at a time across all parallel threads — preventing race conditions and duplicate refresh requests during concurrent test execution.

### Builder Pattern — POJOs with Lombok
```java
Playlist playlist = Playlist.builder()
    .name(faker.name().fullName())
    .description(faker.lorem().sentence())
    .isPublic(false)
    .build();
```

### Facade Pattern — `PlaylistApi`
```java
PlaylistApi.post(playlist);
PlaylistApi.get(playlistId);
PlaylistApi.update(playlistId, updatedPlaylist);
```

---

## Test Scenarios

| Test | Type | HTTP Method | Expected Status |
|------|------|-------------|----------------|
| `ShouldBeableToGetAPlaylist` | Positive | GET | 200 OK |
| `ShouldBeableTocreatePlaylist` | Positive | POST | 201 Created |
| `ShouldBeableToUpdateAPlaylist` | Positive | PUT | 200 OK |
| `ShouldnotBeableTocreatePlaylistWithExpiredToken` | Negative | POST | 401 Unauthorized |
| `ShouldnotBeableTocreatePlaylistWithoutname` | Negative | POST | 400 Bad Request |

---

## How to Run Locally

### Prerequisites
- Java 11+
- Maven 3.6+
- Spotify Developer account with app credentials

### Environment Variables
```
BASE_URI=https://api.spotify.com
ACCOUNT_BASE_URI=https://accounts.spotify.com
CLIENT_ID=your_spotify_client_id
CLIENT_SECRET=your_spotify_client_secret
REFRESH_TOKEN=your_refresh_token
USER_ID=your_spotify_user_id
GRANT_TYPE=refresh_token
```

> ⚠️ Never hardcode credentials. Always pass them as environment variables. Copy `src/test/resources/config.properties.example` to `config.properties` for local runs, or supply the values above as system properties / environment variables.

### Run all tests
```bash
mvn clean test \
  -DBASE_URI=https://api.spotify.com \
  -DACCOUNT_BASE_URI=https://accounts.spotify.com
```

### Generate Allure report
```bash
mvn clean test
allure serve target/allure-results
```

---

## Docker

Run the entire test suite inside a Docker container — identical results on any machine.

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed and running

### Build the image
```bash
docker build -t spotify-test-framework .
```

### Run tests
```bash
docker run \
  -e CLIENT_ID=your_spotify_client_id \
  -e CLIENT_SECRET=your_spotify_client_secret \
  -e REFRESH_TOKEN=your_refresh_token \
  -e USER_ID=your_spotify_user_id \
  -e GRANT_TYPE=refresh_token \
  spotify-test-framework
```

### Expected output
```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Total time: 22 seconds
```

### Why Docker?
- ✅ Identical execution on any OS — Windows, Mac, Linux
- ✅ No environment setup required on new machines
- ✅ Clean isolated container for every test run
- ✅ Credentials passed securely as environment variables
- ✅ Ready for cloud CI/CD environments

---

## CI/CD Pipelines

This project intentionally runs on **two independent CI/CD systems** to demonstrate both self-hosted enterprise-style and modern cloud-native automation.

### Jenkins (self-hosted)
Runs on a local Jenkins server. Pulls from GitHub, compiles, executes tests with parameters injected at build time, and reports results.

```
GitHub Push → Jenkins pulls → mvn clean test → 5 parallel threads → BUILD SUCCESS
```

**Sample build output:**
```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS — Total time: 27.063 s
```

### GitHub Actions (cloud-native)
Defined in [`.github/workflows/maven-tests.yml`](.github/workflows/maven-tests.yml). Triggers automatically on every push to `main`, runs on a GitHub-hosted Ubuntu runner with zero local infrastructure, and pulls credentials securely from **GitHub Secrets**.

```
GitHub Push → GitHub-hosted runner spins up → mvn clean test → BUILD SUCCESS
```

**Sample workflow run:** [View latest runs →](https://github.com/bsuranjit/RestAssuredTestngFramework/actions)

```yaml
- name: Run tests
  run: >
    mvn clean test
    -DCLIENT_ID=${{ secrets.CLIENT_ID }}
    -DCLIENT_SECRET=${{ secrets.CLIENT_SECRET }}
    -DREFRESH_TOKEN=${{ secrets.REFRESH_TOKEN }}
    -DUSER_ID=${{ secrets.USER_ID }}
    -DGRANT_TYPE=${{ secrets.GRANT_TYPE }}
```

---

## Test Reports

Allure reports generated after each run include full request/response details, step-by-step execution breakdown, and historical trends.

```bash
mvn clean test
allure serve target/allure-results
```

---

## Author

**Suranjit Biswas**
- GitHub: [@bsuranjit](https://github.com/bsuranjit)
- LinkedIn: [linkedin.com/in/suranjit-biswas-21932b216](https://linkedin.com/in/suranjit-biswas-21932b216)
- Email: biswassuranjit76@gmail.com

---

## License

This project is open source and available under the [MIT License](LICENSE).
