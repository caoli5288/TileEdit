package com.github.caoli5288.tileedit.tile.info;

import com.github.caoli5288.tileedit.tile.TileInfoData;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;

import java.util.List;

public class SkullTileInfo extends TileInfo {

    public static final List<String> FIELDS = ImmutableList.of("customName", "rotation");

    @Override
    protected void saveState(TileInfoData data, BlockState state) {
        Skull skull = (Skull) state;
        data.setCustomName(skull.getSkullType().name());
        data.setRotation(skull.getRotation().name());
        data.setFields(FIELDS);
    }

    @Override
    protected boolean loadState(TileInfoData data, BlockState state) {
        Skull skull = (Skull) state;
        boolean bool = false;
        // check if equal
        if (!skull.getSkullType().name().equals(data.getCustomName())) {
            skull.setSkullType(SkullType.valueOf(data.getCustomName()));
            bool = true;
        }
        // check if equal
        if (!skull.getRotation().name().equals(data.getRotation())) {
            skull.setRotation(BlockFace.valueOf(data.getRotation()));
            bool = true;
        }
        return bool;
    }
}
