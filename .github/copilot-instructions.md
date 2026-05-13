# Ye Gamol Chattels Copilot Instructions

## Build, test, and run

- Use the 1.12.2 port scaffold on branch `1.12.2-port`.
- Main commands:
  - `./gradlew runClient` - launch the Forge dev client
  - `./gradlew runServer` - launch the Forge dev server
  - `./gradlew build` - build the mod jar
  - `./gradlew test` - run the test task
  - `./gradlew test --tests 'ivorius.yegamolchattels.SomeTest'` - run a single test class or pattern
- There are currently no `src/test` sources in this repository, so port work is validated mainly through `runClient`/`build` until tests are added.

## High-level architecture

- `src/main/java/ivorius/yegamolchattels/YeGamolChattels.java` is the mod bootstrap. It owns config loading, GUI/network setup, event handler registration, material setup, content registration, crafting init, achievements, and worldgen toggles.
- `YGCRegistryHandler` is the central content registrar. `YGCBlocks` and `YGCItems` are static holder classes; most new gameplay content still gets instantiated and registered from `YGCRegistryHandler`, not from the holder classes themselves.
- The codebase is content-heavy and split by concern:
  - `blocks/`, `items/`, `entities/` contain gameplay objects and tile entities
  - `client/rendering/` contains the large TESR/model/render stack for statues, shelves, clocks, gongs, pedestals, microblocks, banners, and flags
  - `crafting/YGCCrafting.java` centralizes recipes plus the plank-saw and plank-refinement registries
  - `events/` separates FML-bus work from Forge event bus work
  - `gui/` contains the saw bench, table press, carving, and config GUIs
- Resources are not just standard item/block textures. Many rendered blocks use `assets/yegamolchattels/textures/mod/` plus specialized models in `client/rendering/`, so visual ports usually need coordinated Java and resource updates.

## Key conventions

- This repository is mid-port from Forge 1.7.10 to 1.12.2. The Gradle scaffold was copied from `/root/arsmagica2`, but most gameplay code still uses 1.7.10-era Forge/FML APIs. Prefer incremental ports from the bootstrap/registries outward instead of mixing old and new registration styles in one change.
- Keep registration centralized. When adding or porting a block/item/entity, update `YGCRegistryHandler` and the corresponding static slot in `YGCBlocks` or `YGCItems`; many other classes assume those globals are populated during startup.
- Many content items are metadata-driven. Item classes such as banners, flags, pedestals, shelves, planks, and gongs derive behavior, names, and textures from damage values, so metadata changes usually require matching updates in lang keys, textures, and recipe outputs.
- Config is data-bearing, not just booleans. `YGCConfig` parses blacklist sets, entity IDs, and custom recipe definitions that feed directly into `PlankSawRegistry` and `PlanksRefinementRegistry`; preserve those hooks when porting crafting or config UI code.
- Client-only rendering state is manually managed. `ClientProxy`, `YGCForgeEventHandler`, `TextureAllocationHandler`, and `SnowGlobeCallListHandler` coordinate TESRs, cached textures, and GL cleanup; treat client/server separation carefully when moving code to newer Forge APIs.
- Optional mod integration is isolated in `YGCOutboundCommunicationHandler` and `mods/`. Keep compatibility hooks behind loader checks instead of referencing optional mods from core registration code.
