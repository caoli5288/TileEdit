package com.github.caoli5288.tileedit.chunk.provider;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class LoadedChunkProvider implements IChunkProvider {
    @Override
    public List<Chunk> getChunks(CommandSender who, World level) {
        return Arrays.asList(level.getLoadedChunks());
    }
}
