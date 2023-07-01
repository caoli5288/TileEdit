package com.github.caoli5288.tileedit.chunk;

import com.github.caoli5288.tileedit.chunk.provider.BorderChunkProvider;
import com.github.caoli5288.tileedit.chunk.provider.IChunkProvider;
import com.github.caoli5288.tileedit.chunk.provider.LoadedChunkProvider;
import com.github.caoli5288.tileedit.chunk.provider.WorldEditChunkProvider;
import com.google.common.collect.Maps;

import java.util.Map;

public class ChunkProviderMap {

    public static final Map<ChunkProviderMode, IChunkProvider> PROVIDER_MAP = Maps.newEnumMap(ChunkProviderMode.class);

    static {
        PROVIDER_MAP.put(ChunkProviderMode.BORDER, new BorderChunkProvider());
        PROVIDER_MAP.put(ChunkProviderMode.LOADED, new LoadedChunkProvider());
        PROVIDER_MAP.put(ChunkProviderMode.WE, new WorldEditChunkProvider());
    }

    public static IChunkProvider getProvider(ChunkProviderMode mode) {
        return PROVIDER_MAP.get(mode);
    }
}
