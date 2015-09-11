package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    private int level;
    private MapContainer mapcontainer;
    private GridPoint2 spawnPosition;

    public Map(int level, MapContainer mapcontainer){
        this.level=level;
        this.mapcontainer=mapcontainer;
    }

    public Tile getRandomEnemyPosition(){
        return mapcontainer.getRandomTile(Tile.TileType.Walkable);
    }

    public MapContainer getMapContainer(){
        return mapcontainer;
    }
    public int getLevel(){
        return level;
    }

    public void setSpawnPoint(GridPoint2 spawnPosition){
        this.spawnPosition=spawnPosition;
    }
    public GridPoint2 getSpawnPoint(){
        return this.spawnPosition;
    }
}
