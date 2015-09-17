package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.items.Item;

import java.util.List;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    private int level;
    private MapContainer mapcontainer;
    private Tile spawnPosition;
    private MapType type;
    private List<Item> itemsInMap;

    public enum MapType{ //defines the "style" of the map, such the texture itself on the drawer
        DUNGEON_COBBLE,
        DUNGEON_SAND,
        DUNGEON_SEWERS,
        DUNGEON_CAVE
    }
    public Map(int level, MapType type, MapContainer mapcontainer){
        this.level=level;
        this.mapcontainer=mapcontainer;
        this.type=type;
    }
    public Map(int level, MapType type){
        this.level=level;
        this.type=type;
    }

    public Tile getRandomEnemyPosition(){
        return mapcontainer.getRandomTile(Tile.TileType.Walkable);
    }
    public Tile getRandomItemLocation(){
        return mapcontainer.getRandomTile(Tile.TileType.Walkable);
    }

    public MapContainer getMapContainer(){
        return mapcontainer;
    }
    public void setMapContainer(MapContainer mapcontainer){ this.mapcontainer=mapcontainer; }

    public MapType getType(){ return type; }
    public int getLevel(){
        return level;
    }

    public void setSpawnPoint(Tile spawnPosition){
        this.spawnPosition=spawnPosition;
    }
    public Tile getSpawnPoint(){
        return this.spawnPosition;
    }

    public List<Item> getItems(){
        return itemsInMap;
    }

}
