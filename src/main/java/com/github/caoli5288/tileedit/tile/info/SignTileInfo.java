package com.github.caoli5288.tileedit.tile.info;

import lombok.experimental.ExtensionMethod;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

@ExtensionMethod(StringUtils.class)
public class SignTileInfo extends AbstractTileInfo {

    private String lines;

    @Override
    protected void saveState(BlockState state) {
        lines = ((Sign) state).getLines().join('\n');
    }

    @Override
    protected boolean loadState(BlockState state) {
        Sign sign = (Sign) state;
        String join = sign.getLines().join('\n');
        if (!join.equals(lines)) {
            String[] seg = lines.split('\n');
            for (int i = 0; i < seg.length; i++) {
                sign.setLine(i, seg[i]);
            }
            return true;
        }
        return false;
    }
}
