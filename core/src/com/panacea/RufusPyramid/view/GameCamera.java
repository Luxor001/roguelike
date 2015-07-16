package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.panacea.RufusPyramid.common.InputManager;

/**
 * Created by gio on 16/07/15.
 */
public class GameCamera {
    private static OrthographicCamera camera = null;
    private static OrthoCamController cameraController;

    private GameCamera() {};

    public static OrthographicCamera getInstance() {
        if (camera == null) {
            camera = createCamera();
            cameraController = new OrthoCamController(camera);
            InputManager.getInstance().addProcessor(cameraController);
        }
        return camera;
    }


    public static OrthographicCamera createCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 150, 150);
//        camera.setToOrtho(false, 800, 480);
        camera.position.set(0, 0, 0);
        camera.update();

        return camera;
    }
}
