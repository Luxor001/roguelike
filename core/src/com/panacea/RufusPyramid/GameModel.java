package com.panacea.RufusPyramid;

import com.panacea.RufusPyramid.creatures.DefaultHero;
import com.panacea.RufusPyramid.map.Map;

/**
 * Created by gio on 19/07/15.
 */
public class GameModel {
    private static GameModel SINGLETON = null;

    public static GameModel getCurrent() {
        return SINGLETON;
    }

    public static void createInstance() {

    }

    public static Map getCurrentMap() {

    }

    public static DefaultHero getHero() {

    }
}
