package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.common.Utilities.Directions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lux on 08/07/15.
 */
public class MapFactory { /*http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm#The_algorithm*/

    private MapContainer map;
    Texture texture;
    int seed;
    Random random;

    private List<Rectangle> rooms;
    public MapFactory(){}

    public Map generateMap(int seed){
        rooms=new ArrayList<Rectangle>();
        this.seed=seed;
        random=new Random(seed);
        map=new MapContainer(50,50);
        initializeMap();
        map.printMap("mapFirst.txt");
        addRoom();
        map.printMap("mapafter.txt");
        addRoom();
        map.printMap("mapafterThird.txt");
        return null;
    }


    private void initializeMap(){ //fill the map with solid and creates a starting room on casual position
     /*step 1, riempimento della mappa con tile solide*/
        texture= new Texture(Gdx.files.internal("data/wall23.gif"));
        for(int row=0; row < map.rLenght();row++)
            for(int column=0; column < map.cLenght();column++)
                map.insertTile(new Tile(new GridPoint2(column,row), Tile.TileType.Solid));


        /*step 2, creazione di uno square iniziale, probabilmente da mettere sotto metodo*/
        int startx= Utilities.randInt(0, map.cLenght() - 5, seed); /*5 supposto come grandezza della stanza iniziale..da sistemare!*/
        int starty=Utilities.randInt(0,map.rLenght()-5,seed);
        for(int row=0; row < 5;row++)
            for(int column=0; column < 5;column++) {
                GridPoint2 newCords=new GridPoint2(startx + column, starty + row);
                map.insertTile(new Tile(newCords, Tile.TileType.Walkable),newCords.x,newCords.y);/*da controllare se Tile(y,x) va bene o va invertito*/
            }
        rooms.add(new Rectangle(startx,starty,5,5));//cache the room

    }
    private boolean fillRect(GridPoint2 startPosition, Directions directionToExpand,Rectangle room){
        MapContainer newMap=map.clone();
        boolean succeeds=true;
        try{

        switch (directionToExpand){
            case NORTH:{
                for(int x=(int)(startPosition.x - (room.width/2)); x <room.width+startPosition.x; x++)
                    for(int y=startPosition.y; y < room.height+startPosition.y;y++){
                        if(x < 0 || x > map.cLenght() || y > map.rLenght())
                            throw new IndexOutOfBoundsException();
                        if(map.getTile(y,x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x,y), Tile.TileType.Walkable), x,y); //insert new tiles of the new room
                    }
                break;
            }
            case SOUTH:
                for(int x=(int)(startPosition.x - (room.width/2)); x <room.width+startPosition.x; x++)
                    for(int y=(int)(startPosition.y - room.height); y < room.height+startPosition.y;y++) {
                        if (x < 0 || x > map.cLenght() || y < 0)
                            throw new IndexOutOfBoundsException();
                        if (map.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), x,y); //insert new tiles of the new room
                }
                break;
            case EAST:
                for(int x=startPosition.x; x <room.width+startPosition.x; x++)
                    for(int y=(int)(startPosition.y - (room.height/2)); y < room.height+startPosition.y;y++) {
                        if (x > map.cLenght() || y < 0 || y > map.rLenght())
                            throw new IndexOutOfBoundsException();
                        if (map.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), x,y); //insert new tiles of the new room
                    }
                break;
            case WEST:
                for(int x=(int)(startPosition.x - room.width); x <room.width+startPosition.x; x++)
                    for(int y=(int)(startPosition.y - (room.height/2)); y < room.height+startPosition.y;y++) {
                        if (x < 0 || y < 0 || y > map.rLenght())
                            throw new IndexOutOfBoundsException();
                        if (map.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), x,y); //insert new tiles of the new room
                    }
                break;
        }
        }
        catch (Exception e){
            succeeds=false;
        }
        if(succeeds)
            map = newMap;

        return succeeds;
    }

    private void addRoom(){
            int height=Utilities.randInt(1, 11,seed);
            int width=Utilities.randInt(1,11,seed);

            ArrayList<GridPoint2> coordsToScan = extractBordersCoords(rooms.get(Utilities.randInt(0, rooms.size()-1, seed))); //extract the borders from a random room..
            for(GridPoint2 coord: coordsToScan){
                Utilities.Directions dir=map.getAdjacentWall(coord);
                if(dir != null){
                    GridPoint2 startingCoords=Directions.adjCoords(coord,dir);
                    boolean succeeds = fillRect(startingCoords,dir,new Rectangle(startingCoords.x,startingCoords.y,5,5));
                    if(succeeds)
                        return; //room succesfully created, exit
                }
            }
    }

    private ArrayList<GridPoint2> extractBordersCoords(Rectangle inputRectangle){
        ArrayList<GridPoint2> coords=new ArrayList<GridPoint2>();
        if(inputRectangle == null || inputRectangle.width == 0 || inputRectangle.height == 0)
            return null;

        for(int i=0; i < inputRectangle.width;i++) //northern bound
            coords.add(new GridPoint2((int)inputRectangle.getX()+i,(int)inputRectangle.getY()));
        for(int i=0; i < inputRectangle.height;i++)//eastern bound
            coords.add(new GridPoint2((int)(inputRectangle.getX()+inputRectangle.width-1),(int)inputRectangle.getY()+i));
        for(int i=0; i < inputRectangle.width;i++)//lower bound
            coords.add(new GridPoint2((int)inputRectangle.getX()+i,(int)(inputRectangle.getY()+inputRectangle.height-1)));
        for(int i=0; i < inputRectangle.width;i++)//western bound
            coords.add(new GridPoint2((int)inputRectangle.getX(),(int)inputRectangle.getY()+i));

        return coords;
    }


}
