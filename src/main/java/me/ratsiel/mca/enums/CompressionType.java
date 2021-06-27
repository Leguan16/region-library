package me.ratsiel.mca.enums;

public enum CompressionType {

    GZIP,
    ZLIB,
    UNCOMPRESSED;

    public static CompressionType getCompression(int type) {
        if(type > 3) return null;

        if(type == 1) {
            return GZIP;
        } else if(type == 2) {
            return ZLIB;
        } else {
            return UNCOMPRESSED;
        }
    }

}
