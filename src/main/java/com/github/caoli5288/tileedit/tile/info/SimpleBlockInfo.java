package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.block.BlockState;

public class SimpleBlockInfo extends AbstractTileInfo {
    @Override
    protected void saveState(BlockState state) {

    }

    @Override
    protected boolean loadState(BlockState state) {
        return false;
    }
}
