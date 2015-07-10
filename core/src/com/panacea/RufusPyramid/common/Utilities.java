package com.panacea.RufusPyramid.common;

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
}
