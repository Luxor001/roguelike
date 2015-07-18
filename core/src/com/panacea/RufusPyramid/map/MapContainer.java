package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities.Directions;
import com.panacea.RufusPyramid.map.Tile.TileType;

import java.io.PrintWriter;

/**
 * Created by lux on 10/07/15.
 */
public class MapContainer {

    private Tile[][] tiles;
    private GridPoint2 cursor;

    public MapContainer(int rows, int columns){
        tiles=new Tile[rows][columns];
        cursor=new GridPoint2(0,0);
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
        if(cursor.x > cLenght() -1) {
            cursor.x = 0;
            cursor.y++;
        }
        if(cursor.y > rLenght() - 1)
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

    public Directions getAdjacentWall(GridPoint2 coordinate){ //this method scans the map at the given coordinate to find a possible solid wall
       for (Directions dir : Directions.values()) {
            if(getTile(Directions.adjCoords(coordinate,dir)).getType() == TileType.Solid){
                return dir;
            }
        }
        return null;
    }

    public int getTilesNumber(TileType type){ //this method counts the tiles of a certain tipe in the container.
        int count=0;
        for(Tile[] tile:tiles )
            for(Tile t:tile)
              if(t.getType() == type)
                  count++;
        return count;
    }
    public void setTiles(Tile [][] tiles){
        this.tiles=tiles;
    }

    public MapContainer clone(){

        Tile [][] clonedTiles = new Tile[tiles.length][];
        for(int i = 0; i < tiles.length; i++)
            clonedTiles[i] = tiles[i].clone();

        MapContainer m=new MapContainer(cLenght(),rLenght());
        m.setTiles(clonedTiles);
        return m;
    }

    public void printMap(String fileName){
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for(int x=0; x < cLenght();x++) {
                for (int y = 0; y < rLenght(); y++) {
                    if(y == 0)
                        writer.write("\n");
                    if (getTile(x, y).getType() == TileType.Solid)
                        writer.write('1');
                    if (getTile(x, y).getType() == TileType.Walkable)
                        writer.write('0');
                }
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
