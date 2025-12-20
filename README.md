# 🏗️ ComposeFoundry
### *The Reactive Multi-Module Design System & Architecture*

ComposeFoundry is a production-ready Android "Foundry" designed to accelerate the development of high-quality, adaptive applications. It bridges the gap between **MVI (Model-View-Intent)** and **Reactive MVVM**, prioritizing type safety, automation, and developer experience.

---

## 🏛️ Architectural Pillars

### 1. Reactive MVVM + UDF (Unidirectional Data Flow)
We chose a hybrid architecture that combines the simplicity of MVVM with the strictness of MVI.

*   **State (`StandardUiState`)**: A single, immutable source of truth for every screen. We use a sealed interface to enforce mutually exclusive states: `Loading`, `Success`, `Error`, and `Empty`.
*   **Side Effects (`UiEffect`)**: Transient actions (Navigation, Toasts) are handled via `Channels` to ensure they are consumed exactly once, preventing "sticky" events on configuration changes.
*   **Standardization**: Our `StandardViewModel` reduces boilerplate for the 90% of screens that follow a data-fetching pattern.

### 2. Adaptive "Smart Shell"
The UI is not just responsive; it is **Adaptive**.
*   Using the `NavigationSuiteScaffold`, the app automatically hotswaps between a **Bottom Bar** (Mobile) and a **Navigation Rail** (Tablet/Landscape).
*   The shell is "Environment Aware," featuring a global **Connectivity Monitor** and `OfflineBanner` that pushes content down safely without overlapping UI.

### 3. Automated Navigation (KSP)
*   **Annotation-Driven**: Simply tag a Composable with `@Destination`.
*   **Type-Safe Mapping**: The KSP processor generates a central `Destinations` registry, including metadata for Labels and Icons, making it effortless to build dynamic menus.

### 4. Zero-Leakage ViewModels (`UiText`)
*   ViewModels emit `UiText` objects (sealed classes representing either Hardcoded Strings or String Resource IDs).
*   **Why?** This prevents `android.content.Context` leaks and allows for effortless Unit Testing and Localization without mocking Android framework classes.

### 5. Infrastructure-as-Code
*   **Secure Storage**: Encrypted DataStore (Kotlin Serialization) for sensitive settings and tokens.
*   **Network Resilience**: A `safeCall` wrapper converts Retrofit exceptions into typed `NetworkResult` objects.
*   **Debug Expert Mode**: A hidden "Expert Menu" (long-press top bar in DEBUG) allows for hotswapping API environments (Dev/Staging/Prod) and wiping local state instantly.

---

## 📦 Multi-Module Structure

| Module           | Responsibility                                                     |
|:-----------------|:-------------------------------------------------------------------|
| `:app`           | The Application Shell, DI wiring, and Feature logic.               |
| `:designsystem`  | Foundation (Theme/Type), Architecture Core, and Atomic Components. |
| `:annotations`   | Navigation and Logic markers used for code generation.             |
| `:ksp-processor` | The engine that generates NavGraphs and Destination registries.    |

---

## 🚀 Usage Pattern
To build a new feature:
1. Define your data model in the feature's `data/api/model` package.
2. Extend `StandardViewModel<MyData, MyEffect>`.
3. Annotate your screen with `@Destination`.
4. Use `FoundryStateWrapper` in your UI to handle the Loading/Error/Success transitions automatically.

---

## 👨‍💻 Author
**atfotiad** - *Lead Architect*
