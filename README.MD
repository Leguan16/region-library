[![Java CI with Gradle](https://github.com/Ratsiiel/region-library/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/Ratsiiel/region-library/actions/workflows/gradle.yml)

# region-library

This is a small library written in Java for the **MCA REGION** file format used for **Minecraft: Java Edition**.

# Features:
- [x] Reading **MCA REGION** file's 
- [ ] Writing **MCA REGION** file's

# Usage:

## Reading RegionFile
```java
File file = new File("./r.0.0.mca");
RegionFile regionFile = RegionFactory.loadRegion(file);
```

## Reading RegionFiles
```java
File regionFolder = new File("./region/");
List<RegionFile> regionFiles RegionFactory.loadRegions(regionFolder);
```

