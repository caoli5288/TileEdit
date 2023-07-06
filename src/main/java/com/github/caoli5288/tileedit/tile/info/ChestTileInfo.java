package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import java.util.Objects;

public class ChestTileInfo extends AbstractTileInfo {

    protected String customName;

    @Override
    protected void saveState(BlockState state) {
        customName = ((Chest) state).getCustomName();
    }

    @Override
    protected boolean loadState(BlockState state) {
        Chest chest = (Chest) state;
        if (!Objects.equals(customName, chest.getCustomName())) {
            chest.setCustomName(customName);
            return true;
        }
        return false;
    }
}
