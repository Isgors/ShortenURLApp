# 🛡️ Orthos — Runtime Integrity & Policy Engine for Android

Welcome to **Orthos**, a modular security policy engine for Android.

Orthos provides a flexible, DSL-based framework to define, evaluate, and enforce security policies at both build-time and runtime.

## 🚨 Demo Disclaimer (Please Read First)

This repository is intentionally **NOT production-hardened**.

The following are included **only to simplify evaluation**:

- 📄 `README.md` with full explanations
- 🔑 `release.jks`
- 🧾 `keystore.properties`
- 🧾 `local.properties`
- 🛠️ Preconfigured `release` setup

👉 **In a real production environment:**
- Keystores would **never** be committed
- Secrets would live in **secure vaults**
- CI/CD would inject credentials at build time
- Runtime policies would be remotely managed

---

## 🧠 What is Orthos?

Orthos is a **policy-driven runtime protection system** that answers a single question:

> _“Can this app instance be trusted?”_

It does so by:
- Collecting **Signals**
- Executing them via a **Signal Engine**
- Evaluating a **Policy DSL**
- Producing a **Verdict**

---

## 🧩 The Signal → Executor → Verdict Pattern

```
┌─────────┐     ┌────────────┐     ┌─────────┐
│ Signal  │ --> │  Executor  │ --> │ Verdict │
└─────────┘     └────────────┘     └─────────┘
```

---

## 🧬 Policy DSL

Orthos uses a **Policy DSL** to define how signals are evaluated.

Supported policies:
- **Strict Policy**
- **Graded Policy**

Policies can be serialized, remotely updated, and safely evaluated with fail-safes.

---

## 🚀 Project Structure

Orthos is divided into three specialized modules to provide end-to-end security:

### 1. 🧠 `orthos-core`
The foundation of the engine. It contains the core logic, base data models, and the internal evaluation bridge. It is a pure Kotlin module, making it lightweight and highly testable.

### 2. 🔌 `orthos-plugin`
A custom **Gradle Plugin** that integrates into the Android build process.
- **Static Analysis**: Scans project configurations.
- **Build Integration**: Automates the preparation of security registries (like the `PrepareOrthosKeepRegistryTask`).
- **Enforcement**: Can be configured to fail builds that do not meet minimum security standards.

### 3. ⚡ `orthos-runtime`
The client-side library used within the Android application.
- **Policy DSL**: A powerful, human-readable Domain Specific Language to define security rules.
- **Evaluators**: Component that executes the defined policies and returns a `Verdict`.
- **Fail-Safe Handlers**: Configurable behavior (Conservative vs. Permissive) for when an error occurs during evaluation.

---

## 🛠️ Technologies Used

- **Kotlin 2.1.0** 🖥️
- **Gradle Kotlin DSL** 🐘
- **Kotlin DSL API** (for the policy builder)
- **Android Gradle Plugin (AGP) API**
- **Koin** (for runtime dependency injection)
- **Coroutines** (for asynchronous policy evaluation)

---

## 📖 How to Use

### 🔌 Applying the Plugin
Add the plugin to your `app/build.gradle.kts`:

```kotlin
plugins {
    id("dev.igordesouza.orthos.plugin")
}

orthos {
    enabled = true
    enabledBuildTypes = setOf("release")
}
```

---

## 🖥️ Demo Security UI

The demo app includes:
- `OrthosVerdictScreen.kt`
- `OrthosVerdictUiState.kt`
- `OrthosVerdictViewModel.kt`

This UI demonstrates how apps can react to security verdicts.

---

## 🚀 Final Notes

Orthos is **experimental** and designed for **education and evaluation**.

👨‍💻 **Author:** Igor de Souza  
🛡️ **Status:** Experimental / Educational
