package com.panacea.RufusPyramid.map;

import java.awt.Point;

/**
 * Created by lux on 10/07/15.
 */
public class MapContainer {

    private Tile[][] tiles;
    private Point cursor;


    public MapContainer(int rows, int columns){
        tiles=new Tile[rows][columns];
    }

    public void insertTile(Tile tile, int row, int column){
        tiles[row][column]=tile;
    }
    public void insertTile(Tile tile){
        tiles[cursor.y][cursor.x]=tile;
        moveCursor();
    }

    private void moveCursor(){ /*x= column, y=row*/
        cursor.x++;
        if(cursor.x > cLenght()) {
            cursor.x = 0;
            cursor.y++;
        }
        if(cursor.y > rLenght())
            cursor.y=0;
    }

    public int rLenght(){
        return tiles.length;
    }
    public int cLenght(){ /*no performance issues http://stackoverflow.com/questions/6626856/performance-wise-is-it-better-to-call-the-length-of-a-given-array-every-time-or*/
        return tiles[0].length;
    }
    public Tile getTile(int row, int col){
        return tiles[row][col];
    }
}
