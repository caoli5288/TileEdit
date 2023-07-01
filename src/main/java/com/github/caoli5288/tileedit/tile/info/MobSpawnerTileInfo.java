package com.github.caoli5288.tileedit.tile.info;

import com.github.caoli5288.tileedit.tile.TileInfoData;
import com.google.common.collect.ImmutableList;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Objects;

public class MobSpawnerTileInfo extends TileInfo {

    public static final List<String> FIELDS = ImmutableList.of("delay", "customName", "max");

    @Override
    protected void saveState(TileInfoData data, BlockState state) {
        CreatureSpawner spawner = (CreatureSpawner) state;
        data.setDelay(spawner.getDelay());
        data.setCustomName(spawner.getSpawnedType().name());
        data.setMax(spawner.getMaxNearbyEntities());
        data.setFields(FIELDS);
    }

    @Override
    protected boolean loadState(TileInfoData data, BlockState state) {
        CreatureSpawner spawner = (CreatureSpawner) state;
        boolean bool = false;
        if (spawner.getDelay() != data.getDelay()) {
            spawner.setDelay(data.getDelay());
            bool = true;
        }
        if (!Objects.equals(spawner.getSpawnedType().name(), data.getCustomName())) {
            spawner.setSpawnedType(EntityType.valueOf(data.getCustomName()));
            bool = true;
        }
        if (spawner.getMaxNearbyEntities() != data.getMax()) {
            spawner.setMaxNearbyEntities(data.getMax());
            bool = true;
        }
        return bool;
    }
}
