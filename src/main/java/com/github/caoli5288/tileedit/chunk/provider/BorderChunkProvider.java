package com.github.caoli5288.tileedit.chunk.provider;

import com.google.common.collect.Lists;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BorderChunkProvider implements IChunkProvider {
    @Override
    public List<Chunk> getChunks(CommandSender who, World level) {
        WorldBorder border = level.getWorldBorder();
        double size = border.getSize();
        Location locate = border.getCenter();
        Location max = locate.clone().add(size / 2, 0, size / 2);
        Location min = locate.clone().subtract(size / 2, 0, size / 2);
        List<Chunk> out = Lists.newArrayList();
        for (int x = min.getBlockX(); x < max.getBlockX(); x = x + 16) {
            for (int z = min.getBlockZ(); z < max.getBlockZ(); z = z + 16) {
                out.add(level.getBlockAt(x, 0, z).getChunk());
            }
        }
        return out;
    }
}
