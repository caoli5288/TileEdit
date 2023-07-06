package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class MobSpawnerTileInfo extends AbstractTileInfo {

    protected EntityType spawnedType;
    protected int delay;
    protected int maxNearby;

    @Override
    protected void saveState(BlockState state) {
        CreatureSpawner spawner = (CreatureSpawner) state;
        delay = spawner.getDelay();
        spawnedType = spawner.getSpawnedType();
        maxNearby = spawner.getMaxNearbyEntities();
    }

    @Override
    protected boolean loadState(BlockState state) {
        CreatureSpawner spawner = (CreatureSpawner) state;
        boolean bool = false;
        if (spawner.getDelay() != delay) {
            spawner.setDelay(delay);
            bool = true;
        }
        if (spawnedType != spawner.getSpawnedType()) {
            spawner.setSpawnedType(spawnedType);
            bool = true;
        }
        if (spawner.getMaxNearbyEntities() != maxNearby) {
            spawner.setMaxNearbyEntities(maxNearby);
            bool = true;
        }
        return bool;
    }
}
