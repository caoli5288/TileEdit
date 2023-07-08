package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;

import java.util.Objects;

public class CommandTileInfo extends AbstractTileInfo {

    private String customName;
    private String command;

    @Override
    protected void saveState(BlockState state) {
        CommandBlock block = (CommandBlock) state;
        customName = block.getName();
        command = block.getCommand();
    }

    @Override
    protected boolean loadState(BlockState state) {
        CommandBlock block = (CommandBlock) state;
        boolean bool = false;
        if (!Objects.equals(block.getName(), customName)) {
            block.setName(customName);
            bool = true;
        }
        if (!Objects.equals(block.getCommand(), command)) {
            block.setCommand(command);
            bool = true;
        }
        return bool;
    }
}
