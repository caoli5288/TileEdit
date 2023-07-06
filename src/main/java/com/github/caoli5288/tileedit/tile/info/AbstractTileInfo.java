package com.github.caoli5288.tileedit.tile.info;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

@Getter
public abstract class AbstractTileInfo {
    // common
    protected Material type;
    protected byte data;
    protected String world;
    protected int x;
    protected int y;
    protected int z;

    public final void save(BlockState state) {
        world = state.getWorld().getName();
        x = state.getX();
        y = state.getY();
        z = state.getZ();
        data = state.getRawData();
        type = state.getType();
        saveState(state);
    }

    public final void load(Block block) {
        if (block.getType() != type) {
            block.setType(type);
        }
        if (block.getData() != data) {
            block.setData(data);
        }
        BlockState state = block.getState();
        if (loadState(state)) {
            state.update();
        }
    }

    protected abstract void saveState(BlockState state);

    protected abstract boolean loadState(BlockState state);
}
