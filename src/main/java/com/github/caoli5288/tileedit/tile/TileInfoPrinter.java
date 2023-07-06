package com.github.caoli5288.tileedit.tile;

import com.github.caoli5288.tileedit.tile.info.AbstractTileInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.bukkit.Material;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class TileInfoPrinter {

    private final Map<Material, Class<?>> typeMap = Maps.newLinkedHashMap();
    private final File file;
    private final List<AbstractTileInfo> buffer = Lists.newArrayList();

    public TileInfoPrinter(File file) {
        this.file = file;
        typeMap.put(Material.AIR, AbstractTileInfo.class);
    }

    public void buffer(AbstractTileInfo info) {
        buffer.add(info);
        if (!typeMap.containsKey(info.getType())) {
            typeMap.put(info.getType(), info.getClass());
        }
    }

    @SneakyThrows
    public void flush() {
        List<String> list = listFields();
        CSVPrinter printer = CSVFormat.EXCEL.withHeader(list.toArray(new String[0]))
                .print(file, Charset.forName("GBK"));
        Gson gson = new Gson();
        for (AbstractTileInfo info : buffer) {
            JsonObject el = gson.toJsonTree(info).getAsJsonObject();
            for (String fieldName : list) {
                if (el.has(fieldName)) {
                    printer.print(el.get(fieldName).getAsString());
                } else {
                    printer.print(null);
                }
            }
            printer.println();
        }
        printer.close(true);
        // cleanup
        typeMap.clear();
        buffer.clear();
    }

    @SneakyThrows
    private List<String> listFields() {
        Map<String, Object> fieldMap = Maps.newLinkedHashMap();
        for (Class<?> cls : typeMap.values()) {
            for (Field field : cls.getDeclaredFields()) {
                if (!fieldMap.containsKey(field.getName())) {
                    fieldMap.put(field.getName(), "");
                }
            }
        }
        return Lists.newArrayList(fieldMap.keySet());
    }
}
