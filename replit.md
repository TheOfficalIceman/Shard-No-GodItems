# Shard Ascension - Minecraft Plugin

A Paper 1.21 Minecraft server plugin introducing a custom ability system with shards, rolling, combinations, and admin controls.

## Building

Requires Java 21. Run:

```
JAVA_HOME=/nix/store/3ilfkn8kxd9f6g5hgr0wpbnhghs4mq2m-openjdk-21.0.7+6 ./gradlew build
```

Output JAR: `build/libs/Shard Ascension-1.0-SNAPSHOT.jar`

The "Build Plugin" workflow is configured to run this automatically.

## Architecture

- `ShardAscensionPlugin.java` — entry point; registers commands, listeners, managers
- `ability/` — ability interface, abstract base, and manager (cooldowns, grants, cycling, combination)
- `ability/standard/` — individual standard abilities
- `ability/combined/` — combined abilities unlocked via the Ability Combination Core item
- `data/PlayerDataManager.java` — YAML-based persistence for player ability and shard data
- `data/PlayerAbilityData.java` — per-player data model (abilities, selected, shards)
- `command/` — command executors
- `item/ItemManager.java` — custom items and crafting recipes
- `listener/` — event listeners (player join, item interactions)
- `util/AbilityRegistrar.java` — central ability + combination registration
- `util/ActionBarUpdater.java` — recurring task displaying ability/cooldown status

## Commands

| Command | Permission | Description |
|---|---|---|
| `/rollability` | — | Roll a random new ability |
| `/ability` | — | Use selected ability |
| `/abilityswap` | — | Cycle to next ability |
| `/ascensiongive <player> <amount>` | admin | Give ascension fragments |
| `/setability <player> <ability>` | admin | Set a player's ability directly |
| `/setshards <player> <amount>` | admin | Add shards to a player (stacks, max 3) |

## Player Data

Stored in `playerdata.yml` per UUID:
- `abilities` — list of ability IDs
- `selected` — currently selected ability ID
- `shards` — shard count (0–3)
