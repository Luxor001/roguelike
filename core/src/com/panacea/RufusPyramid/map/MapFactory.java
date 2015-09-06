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
    private int MAX_AMISSIBLE_ROOMS = 15;
    private int MIN_ROOM_WIDTH=5;
    private int MIN_ROOM_HEIGHT=5;
    private int MAX_ROOM_WIDTH = 10;
    private int MAX_ROOM_HEIGHT= 10;
    private int MAX_MAP_WIDTH = 40;
    private int MAX_MAP_HEIGHT = 40;

    private List<Rectangle> rooms;
    public MapFactory(){}

    public Map generateMap(int seed){
        rooms=new ArrayList<Rectangle>();
        this.seed=seed;
        random=new Random(seed);
        mapContainer =new MapContainer(MAX_MAP_HEIGHT,MAX_MAP_WIDTH);
        initializeMap();

        boolean succeed;
        do{
            succeed=addRoom();
        }while(succeed && rooms.size() <= MAX_AMISSIBLE_ROOMS);

        GridPoint2 spawnPoint;
        int randX=Utilities.randInt((int)rooms.get(0).getX(), (int)(rooms.get(0).getX()+ rooms.get(0).width - 1),seed);
        int randY=Utilities.randInt((int)rooms.get(0).getY(), (int)(rooms.get(0).getY()+ rooms.get(0).height - 1),seed);
        spawnPoint = new GridPoint2(1,1);


        System.out.println(spawnPoint.x + " " + spawnPoint.y +" "+ seed);
        Map newMap=new Map(1,mapContainer);
        newMap.setSpawnPoint(spawnPoint);
        return newMap; //TODO: impostare logica livello (qui è 1, perchè? )
    }


    private void initializeMap(){ //fill the mapContainer with solid and creates a starting room on casual position
     /*step 1, riempimento della mappa con tile solide*/
        for(int row=0; row < mapContainer.rLenght();row++)
            for(int column=0; column < mapContainer.cLenght();column++)
                mapContainer.insertTile(new Tile(new GridPoint2(column,row), Tile.TileType.Solid));


        /*step 2, creazione di uno square iniziale, probabilmente da mettere sotto metodo*/
        int height=Utilities.randInt(MIN_ROOM_HEIGHT,MAX_ROOM_HEIGHT,seed);
        int width=Utilities.randInt(MIN_ROOM_WIDTH,MAX_ROOM_WIDTH,seed);
        int startx= Utilities.randInt(0, mapContainer.cLenght() - width, seed); /*5 supposto come grandezza della stanza iniziale..da sistemare!*/
        int starty=Utilities.randInt(0, mapContainer.rLenght() - height,seed);
        for(int row=0; row < height;row++)
            for(int column=0; column < width;column++) {
                GridPoint2 newCords=new GridPoint2(startx + column, starty + row);
                mapContainer.insertTile(new Tile(newCords, Tile.TileType.Walkable), newCords.y, newCords.x);/*da controllare se Tile(y,x) va bene o va invertito*/
            }
        rooms.add(new Rectangle(startx,starty,width,height));//cache the room

    }

    private boolean addRoom(){
         Collections.shuffle(rooms, new Random(System.nanoTime()));
            //Collections.shuffle(rooms, new Random(System.nanoTime()));
            for (Rectangle room : rooms) {
                ArrayList<BorderCord> coordsToScan = extractBordersCoords(room); //extract the borders from a random room..
                for (BorderCord coord : coordsToScan) {
                    boolean valid = mapContainer.getAdjacentWall(coord); //this method questions: the tile "after" this one is a solid?

                    if (valid) { //a wall has been found
                        GridPoint2 doorCords = Directions.adjCoords(coord.getCoord(), coord.getBound()); //get the coordinates of the wall
                        GridPoint2 startingCoords = Directions.adjCoords(doorCords, coord.getBound()); //do it 2x times. So we can create a "Door"
                       // int newRoomHeight = Utilities.randInt(5, 10, new Random(System.nanoTime()).nextInt());
                       // int newRoomWidth = Utilities.randInt(5, 10, new Random(System.nanoTime()).nextInt());
                         int newRoomHeight = Utilities.randInt(MIN_ROOM_HEIGHT, MAX_ROOM_HEIGHT, seed);
                         int newRoomWidth = Utilities.randInt(MIN_ROOM_WIDTH, MAX_ROOM_WIDTH, seed);
                        boolean succeeds = fillRect(startingCoords, coord.getBound(), new Rectangle(startingCoords.x, startingCoords.y, newRoomWidth, newRoomHeight));
                        if (succeeds) {
                            mapContainer.insertTile(new Tile(doorCords, Tile.TileType.Door), doorCords.y, doorCords.x);

                            //cache the room based on the lower-west bound coordinate
                            if (coord.getBound() == Directions.NORTH)
                                rooms.add(new Rectangle(startingCoords.x - (newRoomWidth / 2), startingCoords.y, newRoomWidth, newRoomWidth));
                            if (coord.getBound() == Directions.EAST)
                                rooms.add(new Rectangle(startingCoords.x, startingCoords.y - (newRoomHeight / 2), newRoomWidth, newRoomWidth));
                            if (coord.getBound() == Directions.SOUTH)
                                rooms.add(new Rectangle(startingCoords.x - (newRoomWidth / 2), startingCoords.y - newRoomHeight + 1, newRoomWidth, newRoomWidth));
                            if (coord.getBound() == Directions.WEST)
                                rooms.add(new Rectangle(startingCoords.x - newRoomWidth + 1, startingCoords.y - (newRoomHeight / 2), newRoomWidth, newRoomWidth));
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
                    for(int y=startPosition.y; y < (startPosition.y+room.height);y++){
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
                    for(int y=(int)(startPosition.y - room.height + 1); y <= startPosition.y;y++) {
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
                for(int x=(int)(startPosition.x - room.width + 1); x <= startPosition.x; x++)
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

    private ArrayList<BorderCord> extractBordersCoords(Rectangle inputRectangle){
        ArrayList<BorderCord> coords=new ArrayList<BorderCord>();
        if(inputRectangle == null || inputRectangle.width == 0 || inputRectangle.height == 0)
            return null;

        BorderCord newCord;
        for(int i=0; i < inputRectangle.width;i++){ //northern bound
            newCord=new BorderCord(new GridPoint2((int)inputRectangle.getX()+i,(int)(inputRectangle.getY()+inputRectangle.height-1)),Directions.NORTH);
            coords.add(newCord);
        }
        for(int i=0; i < inputRectangle.height;i++){//eastern bound
            newCord=new BorderCord(new GridPoint2((int)(inputRectangle.getX()+inputRectangle.width-1),(int)inputRectangle.getY()+i),Directions.EAST);
            coords.add(newCord);
        }
        for(int i=0; i < inputRectangle.width;i++){//lower bound
            newCord=new BorderCord(new GridPoint2((int)inputRectangle.getX()+i,(int)inputRectangle.getY()),Directions.SOUTH);
            coords.add(newCord);
        }
        for(int i=0; i < inputRectangle.width;i++){//western bound
            newCord=new BorderCord(new GridPoint2((int)inputRectangle.getX(),(int)inputRectangle.getY()+i),Directions.WEST);
            coords.add(newCord);
        }
        return coords;
    }

    public class BorderCord{
        private GridPoint2 coord;
        private Directions BorderCoordPosition; //to which bound this coordinate belongs? Lower bound?East Bound?

        public BorderCord(GridPoint2 coord, Directions dir){
            this.coord=coord;
            BorderCoordPosition = dir;
        }

        public GridPoint2 getCoord(){
            return coord;
        }
        public Directions getBound(){
            return BorderCoordPosition;
        }
    }

}
