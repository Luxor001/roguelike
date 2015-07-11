package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapDrawer extends ViewObject {

    private TiledMap map;
    private TiledMapRenderer renderer;
//    private BitmapFont font;
//    private SpriteBatch batch;

    public void create() {

//        font = new BitmapFont();
//        batch = new SpriteBatch();

        map = new TmxMapLoader().load("data/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
    }


    public void render() {
//        renderer.setView(camera);
//        renderer.render();
//        batch.begin();
//        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
//        batch.end();
    }

    public void dispose() {
        map.dispose();
    }
}