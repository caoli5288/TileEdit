package com.github.caoli5288.tileedit.tile;

import lombok.Data;

import java.util.List;

@Data
public class TileInfoData {

    // for printer
    private List<String> fields;
    // common
    private String type;
    private byte data;
    private String world;
    private int x;
    private int y;
    private int z;
    // chest
    private String customName;
    // spawner
    private int delay;
    private int max;
    // skull
    private String rotation;
}
