package com.panacea.RufusPyramid.map;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    int level;
    Tile[][] mapTiles;
    public Map(int level){
        this.level=level;
    }

    public Map create(){
        mapTiles=new Tile[100][100];

        //codice di generazione mappa, usare questo!!!!http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm#The_algorithm
        return null;
    }
}
