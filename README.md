# Truecaller Test App

This project is an Android application built using **Clean Architecture**, **Koin**, **Ktor** and **Kotlin Coroutines**.  
It analyzes web content fetched from a remote source and provides results such as:
- **The 15th character**
- **Every 15th character**
- **Word frequency count**

---

## Environment

- **Java Version**: 17
- **Kotlin Version**: 1.9.0
- **Android Gradle Plugin (AGP)**: 8.4.1
- **Android SDK**:
  - **Compile SDK**: 36
  - **Target SDK**: 36
  - **Min SDK**: 29
- **Android Studio Version**: Jellyfish | 2023.3.1 Patch 1

---

## Project Structure

The project is divided into three modules following **Clean Architecture** principles:

### Domain Module
**Purpose:** Defines core business logic and use cases.

**Responsibilities:**
- Declares interfaces (e.g., `ContentRepository`).
- Implements independent use cases:
  - `GetContentUseCase` – Fetches raw text content.
  - `Get15thCharacterUseCase` – Finds the 15th character.
  - `GetEvery15thCharacterUseCase` – Extracts every 15th character.
  - `GetWordCountsUseCase` – Computes word frequencies.
- Framework styled and fully unit-testable.

---

### Data Module
**Purpose:** Provides implementations of repositories and data sources.

**Responsibilities:**
- Implements `ContentRepository` using **Ktor HTTP Client**.
- Manages data fetching, error handling, and model mapping.
- Provides dependencies via **Koin** (`dataModule`).
- Includes tests using **Ktor MockEngine** to simulate API calls.

---

### UI Module
**Purpose:** Handles presentation and interaction logic.

**Responsibilities:**
- Contains `MainViewModel`, which coordinates all use cases.
- Maintains an immutable `UiState` to represent loading, content, and error states.
- Executes multiple background tasks concurrently using **Kotlin Coroutines**.
- Uses **Koin** for dependency injection.

---

## Tech Stack

| Purpose | Library |
|----------|----------|
| Dependency Injection | [Koin](https://insert-koin.io/) |
| Networking | [Ktor Client](https://ktor.io/docs/getting-started-ktor-client.html) |
| Concurrency | [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) |
| Logging | [Timber](https://github.com/JakeWharton/timber?tab=readme-ov-file) |
| Unit Testing | JUnit5, MockK, Ktor MockEngine |
| Code Style | Ktlint |
| Architecture | Clean Architecture (Domain → Data → UI) |

---

## Testing Strategy

Each module includes focused **unit tests**:

- **Domain Layer Tests**:  
  Test use case logic in isolation.

- **Data Layer Tests**:  
  Use **Ktor MockEngine** to verify repository behavior and error handling.

- **UI Layer Tests**:  
  Validate `MainViewModel` state changes using `runTest` and `StandardTestDispatcher`.

All tests follow the **AAA pattern (Arrange → Act → Assert)**.

---

## Solution

### Step 1: Fetching Remote Content
The process begins in the `MainViewModel`, where the method `loadContent()` is invoked.
It triggers the `GetContentUseCase`, which internally calls the `ContentRepositoryImpl` to fetch data from the API.
The fetched text is then wrapped in a `ContentResponse` and returned to the `ViewModel`.
If any exception occurs during the network call (e.g., timeout, bad response), it is caught and logged using `Timber`, and an empty content response is returned to prevent app crashes.

### Step 2: Launching Parallel Tasks
Once the content is successfully fetched, three independent tasks start simultaneously using async coroutines. This allows them to run in parallel rather than sequentially improving performance and responsiveness.
Each task updates part of the UI state independently as it completes.
If one task fails, others continue unaffected ensuring graceful degradation.

### Step 3: Individual Task Logic

##### Get15thCharacterUseCase
This use case extracts the 15th character from the fetched content. If the content is shorter than 15 characters, it returns null. The result is then posted to `UiState`.

##### GetEvery15thCharacterUseCase
This use case iterates over the content and extracts every 15th character. For long text, this efficiently computes a list of all characters at positions 15, 30, 45, etc. The result updates `UiState`.

##### GetWordCountsUseCase
This use case counts how often each word appears in the text. It normalizes words (lowercase), splits on whitespace, and counts frequencies. The final map updates `UiState`.

### Step 4: Updating UI State

Each coroutine updates its respective field in the `UiState` independently, while setting and clearing loading indicators. After all coroutines complete:
- All isLoading flags reset to false
- Any errors caught during execution populate `UiState`. This ensures real-time state management for each concurrent process.

### Step 5: Error Handling
Each part of the process is wrapped in a try/catch block:
- Network errors → Logged by Timber and update error in UiState
- Individual use case failures → Only affect their respective fields, not others

This design makes the app resilient and ensures fault isolation between concurrent tasks.

---

## Assumptions & Simplifications
- The network layer assumes a static test URL returning textual content.
- Minimal UI implementation to focus on architectural correctness.
- Simplified error propagation to UiState.error.
- No persistent storage or caching is implemented.
- Use cases execute independently and concurrently within MainViewModel.
- Koin is used instead of manual dependency injection for maintainability.

---