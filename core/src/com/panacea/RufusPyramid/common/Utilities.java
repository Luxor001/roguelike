package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Random;

/**
 * Created by lux on 10/07/15.
 */
public class Utilities {

    public static int DEFAULT_BLOCK_WIDTH = 32;
    public static int DEFAULT_BLOCK_HEIGHT = 32;
    public static String ITEMS_XML_PATH = "data/items.xml";

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static int randInt(int min, int max, long seed) {
        Random rand = new Random(seed);
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public enum Directions{
        NORTH,
        EAST,
        SOUTH,
        WEST,
        NORTH_WEST,
        NORTH_EAST,
        SOUTH_EAST,
        SOUTH_WEST,
        HORIZONTAL,/*used for user input Recognition*/
        VERTICAL;

        public static GridPoint2 adjCoords(GridPoint2 inputCords,Utilities.Directions direction){
            GridPoint2 newCords=new GridPoint2(inputCords.x,inputCords.y);
            if(direction == Utilities.Directions.NORTH)
                newCords.y++;
            if(direction == Utilities.Directions.EAST)
                newCords.x++;
            if(direction == Utilities.Directions.SOUTH)
                newCords.y--;
            if(direction == Utilities.Directions.WEST)
                newCords.x--;

            return newCords;
        }
    }

    public static Directions getDirectionFromCoords(GridPoint2 startingPos, GridPoint2 arrivalPos){

        Directions direction = null;
        if(startingPos.x == arrivalPos.x){
            if(startingPos.y > arrivalPos.y)
                direction = Directions.SOUTH;
            else
                direction = Directions.NORTH;
        }
        else{
            if(startingPos.x > arrivalPos.x)
                direction = Directions.WEST;
            else
                direction = Directions.EAST;
        }
        return direction;
    }
    public static GridPoint2 convertToAbsolutePos(GridPoint2 position){
        GridPoint2 newPosition= new GridPoint2(position.x * DEFAULT_BLOCK_WIDTH, position.y * DEFAULT_BLOCK_HEIGHT);
        return newPosition;
    }
    public static GridPoint2 convertToRelativePos(GridPoint2 position){
        GridPoint2 newPosition= new GridPoint2(position.x / DEFAULT_BLOCK_WIDTH, position.y / DEFAULT_BLOCK_HEIGHT);
        return newPosition;
    }
    public static GridPoint2 absoluteDistance(GridPoint2 first, GridPoint2 second){
        int x = Math.abs(first.x - second.x);
        int y = Math.abs(first.y - second.y);
        return new GridPoint2(x,y);
    }
}
