package com.github.caoli5288.tileedit.tile;

import com.github.caoli5288.tileedit.tile.info.AirTileInfo;
import com.github.caoli5288.tileedit.tile.info.ChestTileInfo;
import com.github.caoli5288.tileedit.tile.info.MobSpawnerTileInfo;
import com.github.caoli5288.tileedit.tile.info.SkullTileInfo;
import com.github.caoli5288.tileedit.tile.info.TileInfo;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.util.Map;

public class TileInfoMap {

    public static final Map<Material, TileInfo> TILE_DATA_MAP = Maps.newEnumMap(Material.class);

    static {
        TILE_DATA_MAP.put(Material.AIR, new AirTileInfo());
        TILE_DATA_MAP.put(Material.CHEST, new ChestTileInfo());
        TILE_DATA_MAP.put(Material.MOB_SPAWNER, new MobSpawnerTileInfo());
        TILE_DATA_MAP.put(Material.SKULL, new SkullTileInfo());
    }

    public static boolean isType(Material block) {
        return TILE_DATA_MAP.containsKey(block);
    }

    public static TileInfoData toInfoData(BlockState state) {
        TileInfo info = TILE_DATA_MAP.get(state.getType());
        TileInfoData data = new TileInfoData();
        info.save(data, state);
        return data;
    }

    public static void load(TileInfoData info) {
        TILE_DATA_MAP.get(Material.getMaterial(info.getType()))
                .load(info, Bukkit.getWorld(info.getWorld()).getBlockAt(info.getX(), info.getY(), info.getZ()));
    }
}
