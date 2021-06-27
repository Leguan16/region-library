package me.ratsiel.mca.model.chunk;

import me.ratsiel.nbt.model.CompoundTag;
import me.ratsiel.nbt.model.values.CompoundInt;

public class Chunk {

    private final int x;
    private final int z;
    private final CompoundTag levelData;

    public Chunk(CompoundTag levelData) {
        this.levelData = levelData;
        x = levelData.get("xPos", CompoundInt.class).getValue();
        z = levelData.get("zPos", CompoundInt.class).getValue();
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public CompoundTag getLevelData() {
        return levelData;
    }

}
