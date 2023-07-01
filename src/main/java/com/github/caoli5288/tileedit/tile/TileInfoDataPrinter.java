package com.github.caoli5288.tileedit.tile;

import com.github.caoli5288.tileedit.tile.info.AirTileInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class TileInfoDataPrinter {

    private final Map<String, List<String>> typeMap = Maps.newLinkedHashMap();
    private final File file;
    private final List<TileInfoData> list = Lists.newArrayList();

    public TileInfoDataPrinter(File file) {
        this.file = file;
        typeMap.put("AIR", AirTileInfo.FIELDS);
    }

    public void buffer(TileInfoData data) {
        list.add(data);
        if (!typeMap.containsKey(data.getType())) {
            typeMap.put(data.getType(), data.getFields());
        }
    }

    @SneakyThrows
    public void flush() {
        Map<String, Field> fields = toFields();
        CSVPrinter printer = CSVFormat.EXCEL.withHeader(fields.keySet().toArray(new String[0]))
                .print(file, Charset.forName("GBK"));
        for (TileInfoData data : list) {
            flush(printer, data, fields);
        }
        printer.close(true);
        // cleanup
        typeMap.clear();
        list.clear();
    }

    @SneakyThrows
    private void flush(CSVPrinter printer, TileInfoData data, Map<String, Field> fields) {
        List<Object> values = Lists.newArrayListWithCapacity(fields.size());
        for (Field field : fields.values()) {
            values.add(field.get(data));
        }
        printer.printRecord(values);
    }

    @SneakyThrows
    private Map<String, Field> toFields() {
        Map<String, Field> fields = Maps.newLinkedHashMap();
        for (List<String> it : typeMap.values()) {
            for (String fieldName : it) {
                if (!fields.containsKey(fieldName)) {
                    Field field = TileInfoData.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    fields.put(fieldName, field);
                }
            }
        }
        return fields;
    }
}
