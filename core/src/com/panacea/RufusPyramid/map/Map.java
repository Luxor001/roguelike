package com.panacea.RufusPyramid.map;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    private int level;
    private MapContainer mapcontainer;
    public Map(int level, MapContainer mapcontainer){
        this.level=level;
        this.mapcontainer=mapcontainer;
    }

    public MapContainer getMapContainer(){
        return mapcontainer;
    }
    public int getLevel(){
        return level;
    }
}
