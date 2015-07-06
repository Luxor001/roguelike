package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.panacea.RufusPyramid.common.InputManager;
import com.panacea.RufusPyramid.common.ViewObject;
import com.panacea.RufusPyramid.test.GdxTest;
import com.panacea.RufusPyramid.map.OrthoCamController;

public class MapDrawer extends ViewObject {

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private OrthoCamController cameraController;
//    private BitmapFont font;
//    private SpriteBatch batch;

    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 10, 10);
        camera.update();

        cameraController = new OrthoCamController(camera);
        InputManager.getInstance().addProcessor(cameraController);

//        font = new BitmapFont();
//        batch = new SpriteBatch();

        map = new TmxMapLoader().load("data/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
    }

    public void render() {
        camera.update();
        renderer.setView(camera);
        renderer.render();
//        batch.begin();
//        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
//        batch.end();
    }

    public void dispose() {
        map.dispose();
    }
}