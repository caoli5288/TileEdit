package com.github.caoli5288.tileedit.chunk.provider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import lombok.SneakyThrows;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldEditChunkProvider implements IChunkProvider {

    @Override
    @SneakyThrows
    public List<Chunk> getChunks(CommandSender who, World level) {
        Preconditions.checkState(who instanceof Player, "Only player can get chunks");
        LocalSession session = WorldEdit.getInstance().getSession(who.getName());
        Preconditions.checkNotNull(session, "Player not in WorldEdit session");
        Region selection = session.getSelection(session.getSelectionWorld());
        List<Chunk> list = Lists.newArrayList();
        for (Vector2D chunk : selection.getChunks()) {
            // convert and add to list
            list.add(level.getChunkAt(chunk.getBlockX(), chunk.getBlockZ()));
        }
        return list;
    }
}
