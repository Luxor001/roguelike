package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by lux on 08/07/15.
 */
public class Tile {
    GridPoint2 position;
    Texture texture;
    //Items items;
    TileType type;

    public Tile(GridPoint2 position, Texture texture, TileType type){ //manca items!
        this.position=position;
        this.texture=texture;
        this.type=type;
    }

    public enum TileType{
        Solid,
        Walkable,
        Destructible,
        Hidden
    }

    public class DestructibleTile extends Tile{
        int hp;

        public DestructibleTile(GridPoint2 position, Texture texture, TileType type, int hp){
            super(position, texture, type);
            this.hp=hp;
        }
    }

    public class HiddenTile extends Tile{
        Texture altImg;

        public HiddenTile(GridPoint2 position, Texture texture, TileType type, Texture altImg){
            super(position, texture, type);
            this.altImg=altImg;
        }
    }
}
