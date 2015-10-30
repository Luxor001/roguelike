package com.panacea.RufusPyramid.map;

import com.panacea.RufusPyramid.game.items.Item;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;

import java.util.List;

/**
 * Created by lux on 08/07/15.
 */
public class Map {

    private int level;
    private MapContainer mapcontainer;
    private Tile spawnPosition;
    private Tile nextLevelPosition;
    private MapType type;
    private List<Item> itemsInMap;

    private GridCell[][] pathGrid;
    private NavigationGrid<GridCell> navGrid;

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
    public Map(){
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

    public void setNextLevelPoint(Tile nextLevelposition){
        this.nextLevelPosition = nextLevelposition;
    }
    public Tile getNextLevelPoint(){
        return this.nextLevelPosition;
    }
    public List<Item> getItems(){
        return itemsInMap;
    }
    public GridCell[][] getPathGrid(){
        return pathGrid;
    }
    public void setPathGrid(GridCell[][] pathGrid){
        this.pathGrid= pathGrid;
        this.navGrid = new NavigationGrid<GridCell>(pathGrid);
    }
    public NavigationGrid<GridCell> getNavGrid(){
        return this.navGrid;

    }
}
