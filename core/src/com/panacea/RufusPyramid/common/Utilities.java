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
        WEST,
        NORTH_EAST,
        NORTH_WEST,
        SOUTH_EAST,
        SOUTH_WEST;

        public static GridPoint2 adjCoords(GridPoint2 inputCords, Utilities.Directions direction){
            GridPoint2 newCords = new GridPoint2(inputCords);
            switch(direction) {
                case NORTH:
                    newCords.y++;
                    break;
                case EAST:
                    newCords.x++;
                    break;
                case SOUTH:
                    newCords.y--;
                    break;
                case WEST:
                    newCords.x--;
                    break;
                case NORTH_EAST:
                    newCords.y++;
                    newCords.x++;
                    break;
                case NORTH_WEST:
                    newCords.y++;
                    newCords.x--;
                    break;
                case SOUTH_EAST:
                    newCords.y--;
                    newCords.x++;
                    break;
                case SOUTH_WEST:
                    newCords.y--;
                    newCords.x--;
                    break;
            }
            return newCords;
        }
    }
}
