package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.common.Utilities.Directions;
import com.panacea.RufusPyramid.map.Tile.TileType;

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
    public Tile getTile(int col, int row){
        if(row > rLenght() || col > cLenght())
            return null;
        else
            return tiles[row][col];
    }
    public Tile getTile(GridPoint2 position){
        if(position.y > rLenght() || position.x > cLenght())
            return null;
        else
            return tiles[position.y][position.x];
    }

    public boolean getAdjacentWall(Tile inputTile){
        for (Directions dir : Directions.values()) {
            if(getTile(adjCoords(inputTile,dir)).type == TileType.Walkable){

            }
        }
  /*      if(getTile(adjCoords(inputTile, Directions.NORTH)).type == TileType.Walkable){

        }*/
        return true;
    }


    public GridPoint2 adjCoords(Tile inputTile,Utilities.Directions direction){
        GridPoint2 newCords=inputTile.position;
        if(direction == Utilities.Directions.NORTH)
            newCords.y--;
        if(direction == Utilities.Directions.EAST)
            newCords.x++;
        if(direction == Utilities.Directions.SOUTH)
            newCords.y++;
        if(direction == Utilities.Directions.WEST)
            newCords.x--;

        /*mettere controlle se fuori dalla matrice?*/
        return newCords;
    }
}
