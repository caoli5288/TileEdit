package com.github.caoli5288.tileedit.tile;

import com.github.caoli5288.tileedit.tile.info.AbstractTileInfo;
import com.github.caoli5288.tileedit.tile.info.AirTileInfo;
import com.github.caoli5288.tileedit.tile.info.ChestTileInfo;
import com.github.caoli5288.tileedit.tile.info.CommandTileInfo;
import com.github.caoli5288.tileedit.tile.info.MobSpawnerTileInfo;
import com.github.caoli5288.tileedit.tile.info.SignTileInfo;
import com.github.caoli5288.tileedit.tile.info.SkullTileInfo;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.util.Map;

public class TileInfoMap {

    private static final Map<Material, Class<? extends AbstractTileInfo>> TYPE_MAP;

    static {
        TYPE_MAP = Maps.newEnumMap(Material.class);
        TYPE_MAP.put(Material.AIR, AirTileInfo.class);
        TYPE_MAP.put(Material.CHEST, ChestTileInfo.class);
        TYPE_MAP.put(Material.MOB_SPAWNER, MobSpawnerTileInfo.class);
        TYPE_MAP.put(Material.SKULL, SkullTileInfo.class);
        TYPE_MAP.put(Material.SIGN_POST, SignTileInfo.class);
        TYPE_MAP.put(Material.WALL_SIGN, SignTileInfo.class);
        TYPE_MAP.put(Material.COMMAND, CommandTileInfo.class);
    }

    public static boolean isTile(Material type) {
        return TYPE_MAP.containsKey(type);
    }

    @SneakyThrows
    public static AbstractTileInfo toTileInfo(BlockState state) {
        AbstractTileInfo info = TYPE_MAP.get(state.getType()).newInstance();
        info.save(state);
        return info;
    }

    @SneakyThrows
    public static void load(Gson gson, Map<String, String> infoMap) {
        Class<? extends AbstractTileInfo> type = TYPE_MAP.get(Material.getMaterial(infoMap.get("type")));
        AbstractTileInfo info = gson.fromJson(gson.toJsonTree(infoMap), type);
        info.load(Bukkit.getWorld(info.getWorld()).getBlockAt(info.getX(), info.getY(), info.getZ()));
    }
}
