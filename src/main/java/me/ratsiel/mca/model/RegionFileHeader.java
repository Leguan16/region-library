package me.ratsiel.mca.model;

import me.ratsiel.mca.model.chunk.ChunkLocation;

public class RegionFileHeader {

    public static final int CHUNKS_PER_REGION = 1024;

    private final ChunkLocation[] chunkLocations = new ChunkLocation[CHUNKS_PER_REGION];
    private final int[] chunkTimestamps = new int[CHUNKS_PER_REGION];

    public ChunkLocation[] getChunkLocations() {
        return chunkLocations;
    }

    public int[] getChunkTimestamps() {
        return chunkTimestamps;
    }

}
