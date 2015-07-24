package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.input.OrthoCamController;
import com.panacea.RufusPyramid.game.view.ViewObject;

public class MapDrawer extends ViewObject {

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private OrthoCamController cameraController;
//    private BitmapFont font;
//    private SpriteBatch batch;

    public void create() {
        camera = createCamera();

        cameraController = new OrthoCamController(camera);
        InputManager.get().addProcessor(cameraController);

//        font = new BitmapFont();
//        batch = new SpriteBatch();

        map = new TmxMapLoader().load("data/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f / 32f);
    }

    public OrthographicCamera createCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 10, 10);
        camera.update();

        return camera;
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