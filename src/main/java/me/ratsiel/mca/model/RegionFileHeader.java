package me.ratsiel.mca.model;

import me.ratsiel.mca.model.chunk.RegionChunkLocation;

public class RegionFileHeader {

    public static final int CHUNKS_PER_REGION = 1024;

    private final RegionChunkLocation[] regionChunkLocations = new RegionChunkLocation[CHUNKS_PER_REGION];
    private final int[] chunkTimestamps = new int[CHUNKS_PER_REGION];

    public RegionChunkLocation[] getChunkLocations() {
        return regionChunkLocations;
    }

    public int[] getChunkTimestamps() {
        return chunkTimestamps;
    }

}
