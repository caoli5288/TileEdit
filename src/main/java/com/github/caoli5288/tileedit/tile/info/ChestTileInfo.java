package com.github.caoli5288.tileedit.tile.info;

import com.github.caoli5288.tileedit.tile.TileInfoData;
import com.google.common.collect.ImmutableList;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.List;
import java.util.Objects;

public class ChestTileInfo extends TileInfo {

    public static final List<String> FIELDS = ImmutableList.of("customName");

    @Override
    protected void saveState(TileInfoData data, BlockState state) {
        Chest chest = (Chest) state;
        data.setCustomName(chest.getCustomName());
        data.setFields(FIELDS);
    }

    @Override
    protected boolean loadState(TileInfoData data, BlockState state) {
        Chest chest = (Chest) state;
        if (!Objects.equals(chest.getCustomName(), data.getCustomName())) {
            chest.setCustomName(data.getCustomName());
            return true;
        }
        return false;
    }
}
