package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Random;

/**
 * Created by lux on 10/07/15.
 */
public class Utilities {

    public static int randInt(int min, int max,int seed) {
        Random rand = new Random(seed);
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public enum Directions{
        NORTH,
        EAST,
        SOUTH,
        WEST;

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
}
