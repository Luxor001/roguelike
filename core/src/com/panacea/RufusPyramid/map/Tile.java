package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by lux on 08/07/15.
 */
public class Tile {
    GridPoint2 position;
    //Items items;
    private TileType type;
    private boolean isOpen = false;

    public Tile(GridPoint2 position, TileType type){ //manca items!
        this.position=position;
        this.type=type;
    }

    private Tile(){

    }

    public TileType getType(){
        return type;
    }

    public enum TileType{
        Solid,
        Walkable,
        Door,
        Destructible,
        Hidden,
        NextLevel
    }

    public GridPoint2 getPosition() {
        return this.position;
    }

    public class DestructibleTile extends Tile{
        int hp;

        public DestructibleTile(GridPoint2 position, Texture texture, TileType type, int hp){
            super(position, type);
            this.hp=hp;
        }
    }

    public class HiddenTile extends Tile{
        Texture altImg;

        public HiddenTile(GridPoint2 position, Texture texture, TileType type, Texture altImg){
            super(position, type);
            this.altImg=altImg;
        }
    }

    public boolean getDoorState(){ //FIXME: non è un gran chè, forse è meglio astrarre la porta come "object" della mappa?
        return isOpen;
    }
    public void setDoorState(boolean value){ //FIXME: non è un gran chè, forse è meglio astrarre la porta come "object" della mappa?
        isOpen=value;
    }
}