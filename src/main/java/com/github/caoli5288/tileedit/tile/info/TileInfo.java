package com.github.caoli5288.tileedit.tile.info;

import com.github.caoli5288.tileedit.tile.TileInfoData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public abstract class TileInfo {

    public final void save(TileInfoData data, BlockState state) {
        data.setWorld(state.getWorld().getName());
        data.setX(state.getX());
        data.setY(state.getY());
        data.setZ(state.getZ());
        data.setData(state.getRawData());
        data.setType(state.getType().name());
        saveState(data, state);
    }

    public final void load(TileInfoData data, Block block) {
        String old = block.getType().name();
        if (!old.equals(data.getType())) {
            block.setType(Material.getMaterial(data.getType()));
        }
        if (block.getData() != data.getData()) {
            block.setData(data.getData());
        }
        BlockState state = block.getState();
        if (loadState(data, state)) {
            state.update();
        }
    }

    protected abstract void saveState(TileInfoData data, BlockState state);

    protected abstract boolean loadState(TileInfoData data, BlockState state);
}
