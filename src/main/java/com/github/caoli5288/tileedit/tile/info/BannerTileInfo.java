package com.github.caoli5288.tileedit.tile.info;

import org.bukkit.DyeColor;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;

import java.util.Objects;

public class BannerTileInfo extends AbstractTileInfo {

    private DyeColor color;

    @Override
    protected void saveState(BlockState state) {
        Banner ban = (Banner) state;
        color = ban.getBaseColor();
    }

    @Override
    protected boolean loadState(BlockState state) {
        Banner ban = (Banner) state;
        if (!Objects.equals(ban.getBaseColor(), color)) {
            ban.setBaseColor(color);
            return true;
        }
        return false;
    }
}
