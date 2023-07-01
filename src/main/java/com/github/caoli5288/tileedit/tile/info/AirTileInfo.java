package com.github.caoli5288.tileedit.tile.info;

import com.github.caoli5288.tileedit.tile.TileInfoData;
import com.google.common.collect.ImmutableList;
import org.bukkit.block.BlockState;

import java.util.List;

public class AirTileInfo extends TileInfo {

    public static final List<String> FIELDS = ImmutableList.of("type", "data", "world", "x", "y", "z");
    // Used to remove tile block
    @Override
    protected void saveState(TileInfoData data, BlockState state) {

    }

    @Override
    protected boolean loadState(TileInfoData data, BlockState state) {
        return false;
    }
}
