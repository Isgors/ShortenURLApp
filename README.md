# 🔗 URL Shortener Android App

A simple yet powerful Android application built to demonstrate modern Android development practices. The app allows users to shorten URLs using a remote service, view their history of shortened links, and manage the list.

## ✨ Features

- **Shorten URLs**: Enter a long URL and get a shortened version back.
- **History List**: All shortened URLs are saved locally and displayed in a list for easy access.
- **Delete URLs**: Remove single entries or clear the entire history.
- **Clean & Modern UI**: A user-friendly interface built entirely with Jetpack Compose.
- **Error Handling**: Displays user-friendly messages for network errors or invalid input.

---

## 🏗️ Architecture: Clean + MVVM

This project follows a modern, scalable, and testable architecture based on **Clean Architecture** principles, combined with the **Model-View-ViewModel (MVVM)** pattern for the UI layer.

This separation of concerns makes the codebase:
- **Independent of the UI**: Business logic can be reused with any UI framework.
- **Testable**: Each layer can be tested in isolation.
- **Maintainable**: Changes in one layer have minimal impact on others.

Our architecture is divided into three main layers:

### 🌳 1. Presentation Layer
- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Built with **Jetpack Compose** 🎨, using a unidirectional data flow (UDF). State flows down from the `ViewModel` to the `Composable`s, and events flow up.
- **`ViewModel`**: Responsible for holding and managing UI-related state. It interacts with the Domain layer via Use Cases.
- **Dependency Injection**: `Koin` is used to provide `ViewModel` instances and other dependencies.

### 🧠 2. Domain Layer
- **Pure Kotlin Module**: This layer is the core of the application. It contains the business logic and is completely independent of the Android framework.
- **Use Cases (Interactors)**: Single-purpose classes that encapsulate a specific piece of business logic (e.g., `ShortenUrlUseCase`, `DeleteAllUrlsUseCase`).
- **Models**: Defines the core data structures of the application (e.g., `Url`).
- **Repository Interface**: Defines the contract for data operations, which the Data layer will implement. This keeps the Domain layer independent of how data is fetched or stored.

### 💾 3. Data Layer
- **Repository Implementation**: Implements the repository interface defined in the Domain layer. It's the single source of truth for all app data.
- **Data Sources**: The repository manages multiple data sources:
    - **`RemoteDataSource`**: Responsible for making network calls to the URL shortener API using **Ktor**.
    - **`LocalDataSource`**: Responsible for caching data locally using the **Room** database.
- **Mappers**: Utility functions to convert network response models and database entities to the domain models, and vice-versa.

---

## 🚀 Technologies & Dependencies

This project leverages a stack of modern, best-practice libraries to build a robust and efficient application.

- **UI & Foundation**
    - [**Jetpack Compose**](https://developer.android.com/jetpack/compose) 🎨: The modern declarative UI toolkit for building native Android apps.
    - [**Kotlin Coroutines**](https://kotlinlang.org/docs/coroutines-guide.html) ⏳: For managing background threads and handling asynchronous operations seamlessly.
    - [**Kotlin Flow**](https://kotlinlang.org/docs/flow.html) 🌊: A reactive stream library used for handling streams of data, especially from the database and use cases.

- **Dependency Injection**
    - [**Koin**](https://insert-koin.io/) 💉: A pragmatic and lightweight dependency injection framework for Kotlin.

- **Networking**
    - [**Ktor Client**](https://ktor.io/docs/client-create-new-application.html) 🌐: A modern, asynchronous networking client built by JetBrains. Used for all API communication.
    - [**Kotlinx.Serialization**](https://github.com/Kotlin/kotlinx.serialization) ↔️: For parsing JSON responses from the server into Kotlin data classes.

- **Database**
    - [**Room**](https://developer.android.com/training/data-storage/room) 🚪: A robust persistence library that provides an abstraction layer over SQLite.

- **Testing**
    - [**JUnit 4**](https://junit.org/junit4/) ✅: The standard framework for unit testing.
    - [**MockK**](https://mockk.io/) 🎭: A powerful mocking library for Kotlin, used to create mock objects in unit tests.
    - [**Jetpack Compose Test**](https://developer.android.com/jetpack/compose/testing) 🧪: The official framework for testing Compose UIs, allowing for interaction and verification of `Composable`s.
    - [**Robot Pattern**](https://academy.realm.io/posts/layout-tricks-from-the-trade-writing-better-android-ui-tests/) 🤖: Used in our UI tests to create a layer of abstraction, making tests more readable and maintainable.

---

## 🛠️ How to Build & Run

1.  **Clone the repository**: `git clone <repository-url>`
2.  **Open in Android Studio**: Open the project with the latest stable version of Android Studio.
3.  **Sync Gradle**: Let Android Studio download all the required dependencies.
4.  **Run the app**: Click the 'Run' button to build and install the app on an emulator or a physical device.

---

## 🧪 Testing Strategy

Testing is a first-class citizen in this project. We have a suite of tests to ensure the app is correct and robust.

- **Unit Tests**: Located in `app/src/test`. These tests cover the ViewModel, Use Cases, Data Sources and Repository implementations. They run on the local JVM and use **MockK** to mock dependencies.
- **UI / Instrumentation Tests**: Located in `app/src/androidTest`. These tests verify the UI behavior and user flows. We use the **Jetpack Compose Test** framework along with the **Robot Pattern** to write clean, readable, and stable UI tests.
