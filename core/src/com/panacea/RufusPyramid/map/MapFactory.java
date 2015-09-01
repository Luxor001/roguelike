package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.common.Utilities.Directions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by lux on 08/07/15.
 */
public class MapFactory { /*http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm#The_algorithm*/

    private MapContainer mapContainer;
    Texture texture;
    int seed;
    Random random;

    boolean flag=false;
    private List<Rectangle> rooms;
    public MapFactory(){}

    public Map generateMap(int seed){
        rooms=new ArrayList<Rectangle>();
        this.seed=seed;
        random=new Random(seed);
        mapContainer =new MapContainer(50,50);
        initializeMap();

        int count=1;

        mapContainer.printMap("first" + count + ".txt");
        boolean succeed=false;
        do{
            succeed=addRoom();
            if(succeed) {
                mapContainer.printMap("map" + count + ".txt");
                count++;
            }

        }while(count < 10);

        return new Map(1, mapContainer);
    }


    private void initializeMap(){ //fill the mapContainer with solid and creates a starting room on casual position
     /*step 1, riempimento della mappa con tile solide*/
        for(int row=0; row < mapContainer.rLenght();row++)
            for(int column=0; column < mapContainer.cLenght();column++)
                mapContainer.insertTile(new Tile(new GridPoint2(column,row), Tile.TileType.Solid));


        /*step 2, creazione di uno square iniziale, probabilmente da mettere sotto metodo*/
        int height=Utilities.randInt(2,10,seed);
        int width=Utilities.randInt(2,10,seed);
        int startx= Utilities.randInt(0, mapContainer.cLenght() - width, seed); /*5 supposto come grandezza della stanza iniziale..da sistemare!*/
        int starty=Utilities.randInt(0, mapContainer.rLenght()-height,seed);
        for(int row=0; row < height;row++)
            for(int column=0; column < width;column++) {
                GridPoint2 newCords=new GridPoint2(startx + column, starty + row);
                mapContainer.insertTile(new Tile(newCords, Tile.TileType.Walkable), newCords.y, newCords.x);/*da controllare se Tile(y,x) va bene o va invertito*/
            }
        rooms.add(new Rectangle(startx,starty,width,height));//cache the room

    }

    private boolean addRoom(){
        int height=Utilities.randInt(5, 11,seed);
        int width=Utilities.randInt(5,11,seed);

            Collections.shuffle(rooms, new Random(seed));
            for (Rectangle room : rooms) {
                ArrayList<GridPoint2> coordsToScan = extractBordersCoords(room); //extract the borders from a random room..
                for (GridPoint2 coord : coordsToScan) {
                    Utilities.Directions dir = mapContainer.getAdjacentWall(coord);

                    if (dir != null) { //a wall has been found
                        GridPoint2 doorCords = Directions.adjCoords(coord, dir); //get the coordinates of the wall
                        GridPoint2 startingCoords = Directions.adjCoords(doorCords, dir); //do it 2x times. So we can create a "Door"
                        int newRoomHeight = Utilities.randInt(2, 10, seed);
                        int newRoomWidth = Utilities.randInt(2, 10, seed);
                        boolean succeeds = fillRect(startingCoords, dir, new Rectangle(startingCoords.x, startingCoords.y, newRoomWidth, newRoomHeight));
                        if (succeeds) {
                            mapContainer.insertTile(new Tile(doorCords, Tile.TileType.Door), doorCords.y, doorCords.x);

                            //cache the room based on the lower-west bound coordinate
                            if (dir == Directions.NORTH)
                                rooms.add(new Rectangle(startingCoords.x - (newRoomWidth / 2), startingCoords.y, newRoomWidth, newRoomWidth));
                            if (dir == Directions.EAST)
                                rooms.add(new Rectangle(startingCoords.x, startingCoords.y - (newRoomHeight / 2), newRoomWidth, newRoomWidth));
                            if (dir == Directions.SOUTH)
                                rooms.add(new Rectangle(startingCoords.x - (newRoomWidth / 2), startingCoords.y - newRoomHeight, newRoomWidth, newRoomWidth));
                            if (dir == Directions.WEST)
                                rooms.add(new Rectangle(startingCoords.x, startingCoords.y - (newRoomHeight / 2), newRoomWidth, newRoomWidth));
                            return true; //room succesfully created, exit
                        }
                    }
                }
            }
        return false; //no enough space found, room can't be created
    }

    private boolean fillRect(GridPoint2 startPosition, Directions directionToExpand,Rectangle room){
        MapContainer newMap= mapContainer.clone();
        boolean succeeds=true;

        try{

        switch (directionToExpand){
            case NORTH:{
                for(int x=(int)(startPosition.x - (room.width/2)); x <(room.width/2)+startPosition.x; x++)
                    for(int y=(int)(startPosition.y); y < (startPosition.y+room.height);y++){
                        if(x < 0 || x > mapContainer.cLenght() || y > mapContainer.rLenght())
                            throw new IndexOutOfBoundsException();
                        if(mapContainer.getTile(y,x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x,y), Tile.TileType.Walkable), y,x); //insert new tiles of the new room
                    }
                break;
            }
            case SOUTH:
                for(int x=(int)(startPosition.x - (room.width/2)); x <(room.width/2)+startPosition.x; x++)
                    for(int y=(int)(startPosition.y - room.height); y < startPosition.y+1;y++) {
                        if (x < 0 || x > mapContainer.cLenght() || y < 0)
                            throw new IndexOutOfBoundsException();
                        if (mapContainer.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), y,x); //insert new tiles of the new room
                }
                break;
            case EAST:
                for(int x=startPosition.x; x <room.width+startPosition.x; x++)
                    for(int y=(int)(startPosition.y - (room.height/2)); y < (room.height/2)+startPosition.y;y++) {
                        if (x > mapContainer.cLenght() || y < 0 || y > mapContainer.rLenght())
                            throw new IndexOutOfBoundsException();
                        if (mapContainer.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), y,x); //insert new tiles of the new room
                    }
                break;
            case WEST:
                for(int x=(int)(startPosition.x - room.width); x < startPosition.x; x++)
                    for(int y=(int)(startPosition.y - (room.height/2)); y < (room.height/2)+startPosition.y;y++) {
                        if (x < 0 || y < 0 || y > mapContainer.rLenght())
                            throw new IndexOutOfBoundsException();
                        if (mapContainer.getTile(y, x).getType() == Tile.TileType.Walkable)
                            throw new UnsupportedOperationException();
                        else
                            newMap.insertTile(new Tile(new GridPoint2(x, y), Tile.TileType.Walkable), y,x); //insert new tiles of the new room
                    }
                break;
        }
        }
        catch (Exception e){
            succeeds=false;
        }
        if(succeeds)
            mapContainer = newMap;
        return succeeds;
    }

    private ArrayList<GridPoint2> extractBordersCoords(Rectangle inputRectangle){
        ArrayList<GridPoint2> coords=new ArrayList<GridPoint2>();
        if(inputRectangle == null || inputRectangle.width == 0 || inputRectangle.height == 0)
            return null;

        for(int i=0; i < inputRectangle.width;i++) //northern bound
            coords.add(new GridPoint2((int)inputRectangle.getX()+i,(int)(inputRectangle.getY()+inputRectangle.height-1)));
        for(int i=0; i < inputRectangle.height;i++)//eastern bound
            coords.add(new GridPoint2((int)(inputRectangle.getX()+inputRectangle.width-1),(int)inputRectangle.getY()+i));
        for(int i=0; i < inputRectangle.width;i++)//lower bound
            coords.add(new GridPoint2((int)inputRectangle.getX()+i,(int)inputRectangle.getY()));
        for(int i=0; i < inputRectangle.width;i++)//western bound
            coords.add(new GridPoint2((int)inputRectangle.getX(),(int)inputRectangle.getY()+i));

        return coords;
    }


}
