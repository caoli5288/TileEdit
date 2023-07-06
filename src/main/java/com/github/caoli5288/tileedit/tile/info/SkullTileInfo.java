package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;

public class SkullTileInfo extends AbstractTileInfo {

    private SkullType skullType;
    private BlockFace rotation;

    @Override
    protected void saveState(BlockState state) {
        Skull skull = (Skull) state;
        skullType = skull.getSkullType();
        rotation = skull.getRotation();
    }

    @Override
    protected boolean loadState(BlockState state) {
        Skull skull = (Skull) state;
        boolean bool = false;
        // check if equal
        if (skull.getSkullType() != skullType) {
            skull.setSkullType(skullType);
            bool = true;
        }
        // check if equal
        if (rotation != skull.getRotation()) {
            skull.setRotation(rotation);
            bool = true;
        }
        return bool;
    }
}
