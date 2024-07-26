package com.github.caoli5288.tileedit;

import com.github.caoli5288.tileedit.chunk.ChunkProviderMap;
import com.github.caoli5288.tileedit.chunk.ChunkProviderMode;
import com.github.caoli5288.tileedit.tile.TileInfoMap;
import com.github.caoli5288.tileedit.tile.TileInfoPrinter;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class TileEdit extends JavaPlugin {

    public static final Gson GSON = new Gson();
    private String levelName;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(CommandSender who, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        switch (args[0]) {
            case "save":
                if (args.length < 2) {
                    save(who, ChunkProviderMode.LOADED, Collections.emptySet());
                } else if (args.length < 3) {
                    save(who, ChunkProviderMode.valueOf(args[1].toUpperCase()), Collections.emptySet());
                } else {
                    // example: [save, loaded, air]
                    Set<Material> types = Sets.newHashSet();
                    for (int i = 2; i < args.length; i++) {
                        Material type = Material.getMaterial(args[i].toUpperCase());
                        Objects.requireNonNull(type, "Material not found: %s");
                        types.add(type);
                    }
                    save(who, ChunkProviderMode.valueOf(args[1].toUpperCase()), types);
                }
                return true;
            case "load":
                if (args.length < 2) {
                    return false;
                }
                load(who, args[1]);
                return true;
            case "select":
                if (args.length < 2) {
                    return false;
                }
                levelName = args[1];
                who.sendMessage("select " + levelName);
                return true;
            case "lookup": {
                lookup(who, args);
                return true;
            }
        }
        return false;
    }

    private Queue<Location> lookupDeque;

    private void lookup(CommandSender who, String[] list) {
        // te lookup <mode> [types]
        Preconditions.checkArgument(who instanceof Player);
        Player entity = (Player) who;
        if (list.length == 1) {
            // goto
            if (lookupDeque == null || lookupDeque.isEmpty()) {
                entity.sendMessage("empty");
            } else {
                entity.teleport(lookupDeque.poll());
            }
        } else {
            Set<Material> types = Sets.newHashSet();
            for (int i = 2; i < list.length; i++) {
                Material type = Material.getMaterial(list[i].toUpperCase());
                Objects.requireNonNull(type, "Material not found: %s");
                types.add(type);
            }
            ChunkProviderMode mode = ChunkProviderMode.valueOf(list[1].toUpperCase());
            List<Chunk> chunks = ChunkProviderMap.getProvider(mode).getChunks(who, entity.getWorld());
            lookupDeque = new LinkedList<>();
            for (Chunk chunk : chunks) {
                for (BlockState state : chunk.getTileEntities()) {
                    if (test(types, state.getType())) {
                        lookupDeque.add(state.getLocation());
                    }
                }
            }
            entity.sendMessage("lookup " + lookupDeque.size());
        }
    }

    @SneakyThrows
    private void save(CommandSender who, ChunkProviderMode mode, Collection<Material> types) {
        World level = Bukkit.getWorlds().get(0);
        if (who instanceof Player) {
            Player p = (Player) who;
            level = p.getWorld();
        }
        if (StringUtils.isNotEmpty(levelName)) {
            level = Bukkit.getWorld(levelName);
        }
        // Csv out
        File file = new File(getDataFolder(), toFilename(level, types) + ".csv");
        if (!file.isFile()) {
            Preconditions.checkState(file.createNewFile(), "Create file error: %s", file);
        }
        TileInfoPrinter printer = new TileInfoPrinter(file);
        List<Chunk> chunks = ChunkProviderMap.getProvider(mode).getChunks(who, level);
        for (Chunk chunk : chunks) {
            for (BlockState tile : chunk.getTileEntities()) {
                Material type = tile.getType();
                if (test(types, type)) {
                    printer.buffer(TileInfoMap.toTileInfo(tile));
                }
            }
        }
        printer.flush();
        who.sendMessage(String.format("save " +
                mode.name() +
                " chunks to %s", file.getPath()));
    }

    private static boolean test(Collection<Material> types, Material type) {
        if (types.isEmpty()) {
            return TileInfoMap.isTile(type);
        }
        return types.contains(type);
    }

    private static String toFilename(World level, Collection<Material> types) {
        String levelName = level.getName();
        if (types.isEmpty()) {
            return levelName;
        }
        return levelName + "-" + StringUtils.join(types, "-");
    }

    @SneakyThrows
    private void load(CommandSender who, String filename) {
        File file = new File(getDataFolder(), filename + ".csv");
        Preconditions.checkState(file.isFile(),  "File not found: %s", file);
        CSVFormat.EXCEL.withFirstRecordAsHeader()
                .parse(Files.newReader(file, Charset.forName("GBK")))
                .forEach(it -> TileInfoMap.load(GSON, it.toMap()));
        who.sendMessage("load success");
    }
}
