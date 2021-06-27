package me.ratsiel.mca.model;

import me.ratsiel.mca.enums.CompressionType;
import me.ratsiel.mca.model.chunk.ChunkLocation;
import me.ratsiel.mca.model.chunk.Chunk;
import me.ratsiel.nbt.NBTFactory;
import me.ratsiel.nbt.model.CompoundTag;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;


public class RegionFile {

    private int x;
    private int z;
    private final List<Chunk> chunks = new ArrayList<>();

    /**
     * Creates a object of {@link RegionFile}
     * @param file input of {@link File}
     * @throws IOException if something went wrong
     */
    public RegionFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);

        String[] strings = file.getName().split("\\.");

        // Check if "file" has the correct file type!
        if(strings.length >= 4 && !strings[3].equalsIgnoreCase("mca")) {
            return;
        }

        // Read the "x" and "z" coordinate for RegionFile from "file" name
        if (strings[0].equalsIgnoreCase("r"))
        {
            x = Integer.parseInt(strings[1]);
            z = Integer.parseInt(strings[2]);
        }

        RegionFileHeader regionFileHeader = new RegionFileHeader();

        // Read the chunks location header by RegionFileHeader.CHUNKS_PER_REGION.
        for (int i = 0; i < RegionFileHeader.CHUNKS_PER_REGION; i++) {
            byte[] bytes = new byte[4];
            inputStream.read(bytes);

            byte[] offsetBytes = new byte[]{bytes[0], bytes[1], bytes[2]};
            int offset = ((offsetBytes[0] & 0xF) << 16) | ((offsetBytes[1] & 0xFF) << 8) | (offsetBytes[2] & 0xFF);;
            int sector = bytes[3];
            if(isValidChunkData(bytes)) {
                regionFileHeader.getChunkLocations()[i] = new ChunkLocation(offset, sector);
            }

        }

        // Read the chunks timestamp header by RegionFileHeader.CHUNKS_PER_REGION.
        for (int i = 0; i < RegionFileHeader.CHUNKS_PER_REGION; i++) {
            byte[] bytes = new byte[4];
            inputStream.read(bytes);
            if(isValidChunkData(bytes)) {
                int timestamp = ByteBuffer.wrap(bytes).getInt();
                regionFileHeader.getChunkTimestamps()[i] = timestamp;
            }
        }


        // Read all chunks by RegionFileHeader.CHUNKS_PER_REGION.
        for (int i = 0; i < RegionFileHeader.CHUNKS_PER_REGION; i++) {
            ChunkLocation chunkLocation = regionFileHeader.getChunkLocations()[i];

            if(chunkLocation == null) {
                continue;
            }

            int offset = chunkLocation.getChunkOffset();
            int sector = chunkLocation.getSector();

            if(offset != 0) {
                // Jump to given chunk offset in file!
                inputStream.getChannel().position((long) offset * 4 * RegionFileHeader.CHUNKS_PER_REGION);

                byte[] lengthBytes = new byte[4];
                inputStream.read(lengthBytes);

                int dataLength = ByteBuffer.wrap(lengthBytes).getInt();

                // Check if the data length of chunk is over sector * 4 * RegionFileHeader.CHUNKS_PER_REGION or 0.
                if(dataLength > sector * 4 * RegionFileHeader.CHUNKS_PER_REGION) {
                    throw new IOException("Could not parse MCA File because chunk data is corrupted!");
                } else if(dataLength == 0) {
                    throw new IOException("Could not parse MCA File because chunk data is empty!");
                }

                // Read compression type from inputStream and CompressionType.
                int compressionType = inputStream.read();
                CompressionType compression = CompressionType.getCompression(compressionType);

                // Check if compression is valid.
                if(compression == null) {
                    throw new IOException(String.format("Given compression \"%s\" method is not valid!", compressionType));
                }

                byte[] compressedChunkData = new byte[dataLength - 1];
                inputStream.read(compressedChunkData);

                // Decompress compressedChunkData by compression
                byte[] chunkData = decompress(compressedChunkData, compression);

                CompoundTag compoundTag = NBTFactory.loadNBT(new ByteArrayInputStream(chunkData));
                CompoundTag levelTag = compoundTag.get("Level");

                // Add Chunk with levelTag to chunks
                chunks.add(new Chunk(levelTag));
            }
        }
    }

    /**
     * Decompressed given data if is compressed by {@link CompressionType}
     * @param data given data
     * @param compressionType given compression type
     * @return decompressed data
     * @throws IOException if something went wrong while decompressing
     */
    public byte[] decompress(byte[] data, CompressionType compressionType) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream inputStream = null;
        byte[] bytes = new byte[1024];
        int read;

        switch (compressionType) {
            case GZIP: {
                inputStream = new GZIPInputStream(byteArrayInputStream);
                break;
            }
            case ZLIB: {
                inputStream = new InflaterInputStream(byteArrayInputStream);
                break;
            }
            case UNCOMPRESSED:
                return bytes;
        }

        while ((read = inputStream.read(bytes, 0, bytes.length)) != -1)
        {
            buffer.write(bytes, 0, read);
        }
        buffer.flush();


        return buffer.toByteArray();
    }

    /**
     * Check if read chunk data has values
     * @param bytes given chunk data
     * @return true or false
     */
    public boolean isValidChunkData(byte[] bytes) {
        byte firstByte = bytes[0];
        byte secondByte = bytes[1];
        byte thirdByte = bytes[2];
        byte fourthByte = bytes[3];
        return !(firstByte == 0 && secondByte == 0 && thirdByte == 0 && fourthByte == 0);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }
}
