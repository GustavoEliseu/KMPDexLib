# CLAUDE.md — KMPDexLib

Architecture plan and AI-collaboration rules for the KMPDexLib project.

---

## Project Overview

Kotlin Multiplatform (KMP) + Compose Multiplatform project.
A Pokédex-style app consuming a GraphQL API (Apollo Kotlin).

Two features: **Pokémon List** and **Pokémon Detail**.

### Module Dependency Order

```
core-infra → core-domain → core-data → concept-list
                                     → concept-detail
                                                    → composeApp
```

### Planned Modules

| Module | Role |
|--------|------|
| `composeApp` | App entry, navigation root, DI initialization |
| `concept-list` | Pokémon list screen (MVI feature) |
| `concept-detail` | Pokémon detail screen (MVI feature) |
| `core-domain` | Domain models, repository interfaces, use cases — no frameworks |
| `core-data` | Apollo GraphQL client, repository implementations, mappers |
| `core-infra` | Leaf module: DI primitives, platform config, logging, coroutine utils |

---

## Architecture

**Flow**: `Screen → ViewModel → UseCase → Repository → GraphQL (Apollo)`

- `core-domain` does NOT import Apollo, Koin, or Compose
- ViewModel NEVER calls Repository directly — always through a UseCase
- Each concept module is an independent Gradle module with its own DI module

### Feature Folder Structure (per concept)

```
concept-X/
  features/{feature}/      ← Contract, State, ViewModel, Screen, README.md, BEHAVIOR.md
  domain/                  ← Model, Repository interface, UseCase
  data/                    ← ApolloRepository impl, mappers
  routes/                  ← @Serializable navigation routes
  di/XModule.kt            ← Koin module (self-contained)
```

### MVI Pattern

| Piece | Direction | Rule |
|-------|-----------|------|
| `Event` | View → ViewModel | User intents; `sealed interface` |
| `State` | ViewModel → View | Immutable; `@Immutable data class`; `val` + `List` only |
| `Effect` | ViewModel → View | One-shot (navigate, show snackbar); `sealed interface` |

```kotlin
// State — always @Immutable, always val
@Immutable
data class PokemonListState(
    val isLoading: Boolean = true,
    val pokemons: List<PokemonSummary> = emptyList(),
    val error: String? = null,
)

// Events — sealed interface
sealed interface PokemonListEvent {
    data object LoadPokemons : PokemonListEvent
    data class SelectPokemon(val id: Int) : PokemonListEvent
}

// Effects — sealed interface
sealed interface PokemonListEffect {
    data class NavigateToDetail(val id: Int) : PokemonListEffect
}
```

State updates use `updateState { copy(...) }` — never reassign the whole state directly.

---

## GraphQL (Apollo Kotlin)

### Setup

- Plugin: `com.apollographql.apollo3`
- Schema location: `core-data/src/commonMain/graphql/schema.graphqls`
- Query files: `core-data/src/commonMain/graphql/{feature}/FeatureQuery.graphql`
- Generated models live in `core-data` only — never exposed to concept modules directly
- Mappers in `core-data/data/` translate Apollo generated types → domain models

### Apollo Client Config (core-infra)

```kotlin
// core-infra provides ApolloClient via DI
// Endpoint: https://graphql.pokeapi.co/v1beta2
val apolloClient = ApolloClient.Builder()
    .serverUrl(BuildConfig.GRAPHQL_URL)
    .build()
```

### Query Notes (verified against live API)

- **Sprites**: JSONB field — access via `sprites(path: "front_default")` on `pokemonsprites`
- **Color**: `pokemon → pokemonspecy → pokemoncolor { name }` (values: "red", "blue", "yellow", etc.)
- **Flavor text**: `pokemon → pokemonspecy → pokemonspeciesflavortexts`
  - Filter English: `where: { language: { name: { _eq: "en" } } }` (use `"en"`, not `"English"`)
  - Get latest: `order_by: [{ version_id: desc }], limit: 1`
- **Default forms only**: always add `where: { is_default: { _eq: true } }` on `pokemon`

### Mapping Rule

Apollo → Domain mapping must be explicit, never implicit:

```
Apollo generated type  →  [Mapper in core-data]  →  Domain model (core-domain)
```

Mappers are extension functions: `fun PokemonQuery.Data.Pokemon.toDomain(): PokemonSummary`

---

## Screens

### concept-list: Pokémon List

- Loads a paginated (or full) list of Pokémon via `GetPokemonListQuery`
- Displays: name, sprite/image, number
- On tap → emits `SelectPokemon(id)` event → `NavigateToDetail(id)` effect

### concept-detail: Pokémon Detail

- Loads detail for a single Pokémon by ID via `GetPokemonDetailQuery`
- Displays: name, image, types, base stats, height, weight

---

## Coding Standards

Mirrors mobile-group-trips standards:

- `sealed interface` (NOT `sealed class`)
- `data object` (no params), `data class` (with params)
- `val` and `List` in States — never `var` or `MutableList`
- `@Immutable` on all State classes
- Small functions (< 40 lines), early returns, no deep nesting
- No hardcoded colors/dimensions — use Design System tokens when available

### Anti-Patterns (NEVER)

- ViewModel accessing Repository directly
- Apollo types leaking outside `core-data`
- Domain types depending on Apollo/Ktor/Koin
- Singletons with mutable state
- Hardcoded GraphQL URLs or API keys in source

---

## DI (Koin)

Each concept module declares its own Koin module in `di/XModule.kt`.
`composeApp` assembles all modules: `startKoin { modules(coreInfraModule, coreDataModule, listModule, detailModule) }`.

---

## Documentation (Spec-Driven)

Every feature folder MUST have:
- `README.md` — technical intent, decisions, Intent Changelog
- `BEHAVIOR.md` — business rules in Given/When/Then format

Every data/domain folder MUST have a `README.md`.

**Feature without spec = incomplete feature.**

---

## Testing

- TDD: tests before implementation (Red → Green → Refactor)
- Test naming: `given X when Y then Z`
- Structure: Arrange / Act / Assert
- Use **Fakes** (not Mocks) except at true I/O boundaries
- Use **Turbine** for Effects
- Use `StandardTestDispatcher()` + `advanceUntilIdle()`

### Test locations

```
concept-X/src/commonTest/.../features/{feature}/   ← StateTest, ViewModelTest
concept-X/src/commonTest/.../domain/usecase/       ← UseCaseTest
concept-X/src/commonTest/.../data/repository/      ← RepositoryTest
concept-X/src/commonTest/.../testutils/fakes/      ← Fakes
```

### Mandatory coverage

- State computed properties: 100%
- ViewModel events and effects: 100%

---

## Build & Test Commands

```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:testDebugUnitTest
./gradlew :concept-list:testDebugUnitTest
./gradlew :concept-detail:testDebugUnitTest
./gradlew :core-domain:testDebugUnitTest
./gradlew :core-data:testDebugUnitTest
./gradlew :concept-list:testDebugUnitTest --tests "*.ClassName"
```

---

## DRY Registry

| Module | Registry |
|--------|----------|
| UI components, composables, UI extensions | `composeApp/dry.md` |
| Mappers, Apollo helpers, repository helpers | `core-data/dry.md` |
| Base classes, domain utils, result handling | `core-domain/dry.md` |

Before creating any reusable component, consult the relevant `dry.md`.

---

## Implementation Roadmap

### Phase 1 — Foundation

- [x] Add Gradle modules: `core-infra`, `core-domain`, `core-data`
- [x] Wire Apollo Kotlin plugin + download schema (`core-data/src/commonMain/graphql/schema.graphqls`)
- [ ] Configure Apollo client in `core-infra` (wire into Koin)
- [ ] Set up Koin in `core-infra`

### Phase 2 — List Feature

- [x] Add `concept-list` module
- [x] Write `GetPokemonListQuery.graphql`
- [ ] Domain: `PokemonSummary` model, `PokemonRepository` interface, `GetPokemonListUseCase`
- [ ] Data: `ApolloRepository` impl, mapper
- [ ] MVI: `State`, `Event`, `Effect`, `ViewModel`
- [ ] Screen: `PokemonListScreen`
- [ ] Tests (TDD)

### Phase 3 — Detail Feature

- [x] Add `concept-detail` module
- [x] Write `GetPokemonDetailQuery.graphql`
- [ ] Domain: `PokemonDetail` model, `GetPokemonDetailUseCase`
- [ ] Data: impl, mapper
- [ ] MVI: `State`, `Event`, `Effect`, `ViewModel`
- [ ] Screen: `PokemonDetailScreen`
- [ ] Tests (TDD)

### Phase 4 — Navigation & Assembly

- [ ] Navigation graph in `composeApp` (Compose Navigation or Voyager)
- [ ] Assemble all Koin modules
- [ ] End-to-end smoke test

---

## Quick References

| What | Where |
|------|-------|
| Architecture | This file — `CLAUDE.md` |
| DRY — UI | `composeApp/dry.md` |
| DRY — Domain | `core-domain/dry.md` |
| DRY — Data | `core-data/dry.md` |
| GraphQL schema | `core-data/src/commonMain/graphql/schema.graphqls` |
| Queries | `core-data/src/commonMain/graphql/{feature}/` |
