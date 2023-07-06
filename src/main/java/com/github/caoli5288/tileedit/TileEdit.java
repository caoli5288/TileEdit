package com.github.caoli5288.tileedit;

import com.github.caoli5288.tileedit.chunk.ChunkProviderMap;
import com.github.caoli5288.tileedit.chunk.ChunkProviderMode;
import com.github.caoli5288.tileedit.tile.TileInfoMap;
import com.github.caoli5288.tileedit.tile.TileInfoPrinter;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class TileEdit extends JavaPlugin {

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
                    save(who, ChunkProviderMode.LOADED);
                } else {
                    save(who, ChunkProviderMode.valueOf(args[1].toUpperCase()));
                }
                return true;
            case "load":
                if (args.length < 2) {
                    return false;
                }
                load(who, args[1]);
                return true;
        }
        return false;
    }

    @SneakyThrows
    private void save(CommandSender who, ChunkProviderMode mode) {
        World level = Bukkit.getWorlds().get(0);
        if (who instanceof Player) {
            Player p = (Player) who;
            level = p.getWorld();
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
                if (TileInfoMap.isTile(tile.getType())) {
                    printer.buffer(TileInfoMap.toTileInfo(tile));
                }
            }
        }
        printer.flush();
        getLogger().info(String.format("save " +
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
