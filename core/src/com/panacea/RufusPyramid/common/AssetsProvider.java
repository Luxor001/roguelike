package com.panacea.RufusPyramid.common;

/**
 * Created by gio on 12/10/15.
 */
public class AssetsProvider extends com.badlogic.gdx.assets.AssetManager {
    private static AssetsProvider SINGLETON = null;

    private AssetsProvider() {

    }

    public static AssetsProvider get() {
        if (SINGLETON == null) {
            SINGLETON = new AssetsProvider();
        }
        return SINGLETON;
    }
}
