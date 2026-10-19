# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Pulsar is a multi-module Android app (Kotlin, Jetpack Compose, Material 3, Hilt, Room3, Retrofit + kotlinx.serialization) for market data / a stock price board. UI strings are in Vietnamese. Package root: `com.hmst.pulsar`. minSdk 24, compile/target SDK 36, Java 11. Gradle 9.6, AGP 9.x.

## Commands

Use `./gradlew` from the Bash tool or `.\gradlew.bat` from PowerShell.

```sh
./gradlew assembleDebug                      # build the app
./gradlew installDebug                       # install on a connected device/emulator
./gradlew test                               # all JVM unit tests
./gradlew :app:testDebugUnitTest --tests "com.hmst.pulsar.ExampleUnitTest"   # single test class
./gradlew :app:connectedDebugAndroidTest     # instrumented tests (needs a device)
./gradlew lint                               # Android lint
./gradlew :feature:market:assembleDebug      # build a single module
```

There is no ktlint/detekt setup; `kotlin.code.style=official`.

## Architecture

### Build logic (`build-logic/convention`)
All module configuration lives in convention plugins (an included build); module `build.gradle.kts` files should stay tiny — just apply a plugin, set `namespace`, and add module-specific deps. SDK/Java levels are in `com/hmst/pulsar/buildlogic/AndroidConfig.kt`.

| Plugin id | Used by | Adds |
|---|---|---|
| `pulsar.android.application` | `:app` | Android app + Compose + Compose BOM deps |
| `pulsar.android.library` | non-UI core modules | Android library config |
| `pulsar.android.library.compose` | `:core:designsystem`, features | library + Compose |
| `pulsar.android.feature` | `:feature:*` | library.compose + Hilt + kotlinx-serialization + `:core:designsystem` + navigation/hilt-navigation/lifecycle-compose |
| `pulsar.hilt` | any module with `@Module`/`@Inject`/`@HiltViewModel` | Hilt + KSP |
| `pulsar.android.room` | `:core:database` | Room3 runtime + KSP compiler |

All versions live in `gradle/libs.versions.toml`; convention plugins look up catalog entries by alias string (`libs.library("...")`, `libs.pluginId("...")`), so renaming a catalog alias breaks them at configuration time. Type-safe project accessors are enabled (`projects.core.network`). New modules must be added to `settings.gradle.kts`.

### Module graph
- `:app` — `MainActivity`/`MainApplication` (Hilt entry points), `PulsarApp` root, and `navigation/AppNavHost.kt`, the **only** place that knows every feature and wires navigation between them.
- `:core:network` — shared `OkHttpClient`, `Json`, and an unscoped `Retrofit.Builder`. Each feature builds its own Retrofit with its own base URL from that builder (multiple APIs). Exposes Retrofit and kotlinx-serialization as `api`.
- `:core:datastore` — a single app-wide Preferences `DataStore` (`app_preferences`); each feature owns and defines its own keys.
- `:core:database` — Room3 (scaffolded: `dao/`, `entity/`, `di/`).
- `:core:designsystem` — `PulsarTheme`, colors, typography, shared components.
- `:feature:market`, `:feature:priceboard` — features depend on core modules only, **never on another feature**.

### Feature module layout
Each feature follows the same clean-architecture package skeleton (empty dirs are kept with `.gitkeep`):
- `data/` — `remote/dto`, `mapper`, `repository` (impl), `di`
- `domain/` — `model`, `repository` (interface), `usecase`
- `navigation/` — a `@Serializable data object XRoute`, `NavController.navigateToX()`, and `NavGraphBuilder.xScreen(callbacks...)`. Cross-feature navigation is passed in as lambdas from `AppNavHost`.
- `ui/` — `XScreen` (stateful: `hiltViewModel()` + `collectAsStateWithLifecycle`) delegating to `XContent` (stateless, previewable with `PulsarTheme`), `XViewModel` exposing `StateFlow<XUiState>`, `XUiState` data class with a nested sealed `Content` (`Loading`/`Success`/`Error`), and `component/` for screen-local composables.

Each screen draws its own `Scaffold`; the app root only hosts navigation.
