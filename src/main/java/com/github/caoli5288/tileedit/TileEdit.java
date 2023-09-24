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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class TileEdit extends JavaPlugin {

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
                    save(who, ChunkProviderMode.LOADED, it -> true);
                } else if (args.length < 3) {
                    save(who, ChunkProviderMode.valueOf(args[1].toUpperCase()), it -> true);
                } else {
                    // example: [save, loaded, air]
                    Set<Material> materials = Sets.newHashSet();
                    for (int i = 2; i < args.length; i++) {
                        Material type = Material.getMaterial(args[i].toUpperCase());
                        Objects.requireNonNull(type, "Material not found: %s");
                        materials.add(type);
                    }
                    save(who, ChunkProviderMode.valueOf(args[1].toUpperCase()), materials::contains);
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
        }
        return false;
    }

    @SneakyThrows
    private void save(CommandSender who, ChunkProviderMode mode, Predicate<Material> materials) {
        World level = Bukkit.getWorlds().get(0);
        if (who instanceof Player) {
            Player p = (Player) who;
            level = p.getWorld();
        }
        if (StringUtils.isNotEmpty(levelName)) {
            level = Bukkit.getWorld(levelName);
        }
        // Csv out
        File file = new File(getDataFolder(), (System.currentTimeMillis() / 1000) + ".csv");
        if (!file.isFile()) {
            Preconditions.checkState(file.createNewFile(), "Create file error: %s", file);
        }
        TileInfoPrinter printer = new TileInfoPrinter(file);
        List<Chunk> chunks = ChunkProviderMap.getProvider(mode).getChunks(who, level);
        for (Chunk chunk : chunks) {
            for (BlockState tile : chunk.getTileEntities()) {
                Material type = tile.getType();
                if (materials.test(type) && TileInfoMap.isTile(type)) {
                    printer.buffer(TileInfoMap.toTileInfo(tile));
                }
            }
        }
        printer.flush();
        who.sendMessage(String.format("save " +
                mode.name() +
                " chunks to %s", file.getPath()));
    }

    @SneakyThrows
    private void load(CommandSender who, String filename) {
        File file = new File(getDataFolder(), filename + ".csv");
        Preconditions.checkState(file.isFile(),  "File not found: %s", file);
        Gson gson = new Gson();
        CSVFormat.EXCEL.withFirstRecordAsHeader()
                .parse(Files.newReader(file, Charset.forName("GBK")))
                .forEach(it -> TileInfoMap.load(gson, it.toMap()));
        who.sendMessage("load success");
    }
}
