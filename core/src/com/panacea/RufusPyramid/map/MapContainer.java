package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.common.Utilities.Directions;
import com.panacea.RufusPyramid.map.Tile.TileType;

import java.io.PrintWriter;
import java.util.Random;

import javax.rmi.CORBA.Util;

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
    public Tile getTile(int row, int col){
        if(row > rLenght() || col > cLenght())
            return null;
        else
            return tiles[row][col];
    }
    public Tile getTile(GridPoint2 position){
        if(position.y > rLenght() -1  || position.x > cLenght()-1 || position.y < 0 || position.x < 0)
            return null;
        else
            return tiles[position.y][position.x];
    }

    public boolean getAdjacentWall(MapFactory.BorderCord coordinate){ //this method scans the map at the given coordinate to find a possible solid wall
        Tile tile=getTile(Directions.adjCoords(coordinate.getCoord(),coordinate.getBound()));
        if(tile != null && tile.getType() == TileType.Solid)
                return true;
        return false;
    }

    public int getTilesNumber(TileType type){ //this method counts the tiles of a certain type in the container.
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

    public Tile getRandomTile(TileType type){

        if(getTilesNumber(type) != 0){ //TODO: non è efficiente, lo so, ma non abbiamo molto tempo! probabilmente è meglio fare un "getTilesbyType" ed ottenere tutte le tiles di un certo tipo, e poi randomizzare su quello
            Tile randomTile = null;
            int tries=0;
            do {
                int seed = (int)System.nanoTime();
                int randx = Utilities.randInt(0,cLenght()-1,seed);
                int randy = Utilities.randInt(0,rLenght()-1,seed);
                if(getTile(randy,randx).getType() == type)
                    randomTile=getTile(randy,randx);
                tries++;
            }while(randomTile == null || tries < 5000);
            return randomTile;
        }
        else
            return null;
    }

    public void printMap(String fileName){
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for(int y=rLenght()-1; y >= 0;y--){
                for(int x=0; x < cLenght();x++){
                    if (getTile(y, x).getType() == TileType.Solid)
                        writer.write('1');
                    if (getTile(y, x).getType() == TileType.Walkable)
                        writer.write('0');
                    if(getTile(y, x).getType() == TileType.Door)
                        writer.write('2');
                }
                if(y != 0)
                    writer.write("\n");
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
