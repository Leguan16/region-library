package me.ratsiel.mca.model.chunk;

public class RegionChunkLocation {

    private final int chunkOffset;
    private final int sector;

    public RegionChunkLocation(int chunkOffset, int sector) {
        this.chunkOffset = chunkOffset;
        this.sector = sector;
    }

    public int getChunkOffset() {
        return chunkOffset;
    }

    public int getSector() {
        return sector;
    }
}
