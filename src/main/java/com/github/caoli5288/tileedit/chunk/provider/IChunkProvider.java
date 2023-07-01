package com.github.caoli5288.tileedit.chunk.provider;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface IChunkProvider {

    List<Chunk> getChunks(CommandSender who, World level);
}
