package com.panacea.RufusPyramid.map;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    int level;
    Tile[][] mapTiles;
    public Map(int level, Tile[][] mapTiles){
        this.level=level;
        this.mapTiles=mapTiles;
    }
}
