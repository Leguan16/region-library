package me.ratsiel.mca.model.chunk;

public class ChunkLocation {

    private final int chunkOffset;
    private final int sector;

    public ChunkLocation(int chunkOffset, int sector) {
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
