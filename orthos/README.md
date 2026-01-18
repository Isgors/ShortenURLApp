# 🛡️ Orthos — Drop‑in Runtime Integrity & Policy Engine for Android

Orthos is a modular Android security engine that combines **build‑time hardening** (ASM + Gradle plugin) with a **runtime signal + policy evaluator** to answer one question:

> **“Can this app instance be trusted right now?”**

It is designed to feel like a **drop‑in SDK (Firebase‑like)** while remaining fully inspectable and testable.

---

## 🚨 Demo disclaimer (please read first)

This repository is intentionally **not production‑hardened**.

The following are included **only to simplify evaluation**:

- 📄 `README.md` with full explanations
- 🔑 `release.jks`
- 🧾 `keystore.properties`
- 🧾 `local.properties`
- 🛠️ Preconfigured `release` setup (signing + minify)

✅ In a production setup:

- Keystores & secrets would **never** be committed
- CI/CD would inject secrets from a **vault**
- Policies/flags would be served by a backend and protected by **integrity checks**
- Canary seeds / native agreements would be rotated and monitored

---

## 🧠 Mental model

Orthos follows a simple pipeline:

```
Signals → Executor → (Weights) → Policy → Verdict
```

- **Signals** produce observations (root/emulator/canary/etc.)
- An **Executor** runs enabled signals
- **Weights** (from feature config) scale confidence
- A **Policy DSL** turns evidence into a decision
- The app reacts to a final **Verdict**

---

## 🧩 The Signal → Executor → Verdict pattern

```
┌──────────┐     ┌──────────────┐     ┌──────────┐
│ Signals  │ --> │  Executor    │ --> │ Verdict  │
└──────────┘     └──────────────┘     └──────────┘
       ▲                  ▲                  ▲
       │                  │                  │
 Feature config     Weighting logic     Policy DSL / JSON
```

---

## 🏗️ Modules (Firebase‑like structure)

Orthos is split into focused modules so apps can adopt it incrementally.

```
orthos/
├── orthos-core        → Pure Kotlin domain (models + verdict types)
├── orthos-plugin      → Gradle plugin (ASM instrumentation, build tasks)
├── orthos-runtime     → Signal engine + snapshot/weights/policy evaluation
├── orthos-sdk         → ✅ Public facade (drop‑in “SDK” entrypoint)
└── orthos-devtools    → 🧪 Dev Panel (override flags/policy without reinstall)
```

### 1) `orthos-core` (domain)
- `SignalId`, `SignalType`, `SignalResult`
- `RuntimeState`, `OrthosVerdict`

### 2) `orthos-plugin` (build-time hardening)
- Registers ASM visitors (e.g., canary/native agreement injectors)
- Generates and consumes a `keep-registry.txt` to produce keep rules
- Variant-aware enablement so instrumentation only runs when intended

### 3) `orthos-runtime` (engine)
- Loads feature snapshot (local/remote)
- Resolves enabled signals
- Executes signals and applies weights
- Evaluates policy (via DSL definition)
- Returns `OrthosVerdict` **with evidences**

### 4) `orthos-sdk` (drop-in facade)
The consumer app integrates with a stable surface:

```kotlin
interface OrthosRuntimeApi {
    suspend fun evaluate(): OrthosVerdict
}
```

The SDK selects between:

- ✅ **Real** runtime (full evaluation)
- 🧊 **NoOp** runtime (always SAFE, score 0) — useful for debug builds and safe‑off mode

### 5) `orthos-devtools` (Dev Panel)
A developer UI to:
- Override feature flags and policy thresholds
- Apply changes instantly
- Avoid “uninstall app → reinstall” loops during development

---

## 🚪 Security‑aware navigation (Gate pattern)

Instead of hardcoding the security UI as the entrypoint, the app starts at a **Gate**.

High-level flow:

```
App launch
   ↓
Gate Screen
   ↓
Orthos enabled?
   ├── NO  → Home
   └── YES → OrthosVerdictScreen
                 ↓
        SAFE/SUSPICIOUS → Home
        TAMPERED → Exit app
```

Why it matters:
- Orthos can be **fully disabled** per build variant
- Security UI becomes **opt-in and controlled**
- The rest of the app stays clean and unaware of security plumbing

---

## ✅ One source of truth: “Orthos enabled”

Orthos should not end up in a state where:
- plugin is enabled, but runtime is not (or vice‑versa)

This repo uses a single build-time flag (example):

```kotlin
buildTypes {
  debug { resValue("bool", "orthos_enabled", "false") }
  release { resValue("bool", "orthos_enabled", "true") }
}
```

Then the SDK checks enablement via one function (e.g.):
```kotlin
Orthos.isEnabled(context)
```

---

## 🧬 Policy DSL

Orthos uses a small Policy DSL to define decisions based on final score:

```kotlin
policy {
    score(SumScoreStrategy)

    whenScore {
        atLeast(100, RuntimeState.TAMPERED)
        otherwise(RuntimeState.SAFE)
    }
}
```

Policies can be:
- Built locally via DSL
- Derived from JSON
- Hot-swapped remotely (demo-friendly)
- Evaluated with fail-safes (Conservative / Permissive)

---

## 🖥️ Demo Security UI (consumer app)

The demo app includes a small “security package” to visualize verdicts:

- `OrthosVerdictScreen.kt`
- `OrthosVerdictUiState.kt`
- `OrthosVerdictViewModel.kt`

Behavior:
- **SAFE** → Continue
- **SUSPICIOUS** → Continue anyway
- **TAMPERED** → Close app

This demonstrates how a product could:
- block access
- reduce feature access
- log telemetry
- trigger additional verification steps

---

## 🧪 How to integrate (consumer app)

### 1) Apply the plugin
```kotlin
plugins {
    id("dev.igordesouza.orthos.plugin")
}

orthos {
    enabled = true
    enabledBuildTypes = setOf("release")
}
```

### 2) Add the dependency
```kotlin
dependencies {
    implementation("dev.igordesouza.orthos:runtime:0.1.0")
    // If using the SDK facade:
    implementation("dev.igordesouza.orthos:sdk:0.1.0")
    // Dev tools should be debug-only:
    debugImplementation("dev.igordesouza.orthos:devtools:0.1.0")
}
```

### 3) Wire DI (Koin example)
```kotlin
single {
    Orthos.install(context = androidContext(), enabledFromConsumer = BuildConfig.ORTHOS_ENABLED)
}
```

Then your ViewModel depends on the facade instead of the runtime directly:
```kotlin
class OrthosVerdictViewModel(
  private val orthos: OrthosRuntimeApi
) : ViewModel()
```

---

## 🧰 Technologies used

- Kotlin + Coroutines
- Jetpack Compose
- Koin
- Android Gradle Plugin (AGP) APIs
- ASM (bytecode visitors + instrumentation)
- R8 / Proguard integration (keep rules)
- Feature snapshot (JSON → runtime config)

---

## 🎯 Final notes

This codebase is intentionally structured to highlight real-world platform engineering skills:

- bytecode instrumentation and build tooling
- variant-aware security configuration
- policy-as-code design (DSL)
- safe fallbacks and fail-safe behavior
- SDK ergonomics (drop-in, stable API surface)
- developer productivity (dev panel overrides)
- clean app integration (Gate navigation)

---

## 👨‍💻 Author

**Igor de Souza**  
Status: Experimental / Educational
