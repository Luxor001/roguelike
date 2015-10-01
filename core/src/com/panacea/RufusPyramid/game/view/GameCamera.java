package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.panacea.RufusPyramid.game.view.input.InputManager;

/**
 * Created by gio on 16/07/15.
 */
public class GameCamera {
    private static OrthographicCamera camera = null;
    private static com.panacea.RufusPyramid.game.view.input.OrthoCamController cameraController;

    private GameCamera() {};

    /**
     * Ritorna l'unica istanza della camera del gioco, creandola se necessario.
     * @return l'unica istanza della camera.
     */
    public static OrthographicCamera get() {
        //TODO istanziare di default all'avvio del gioco
        if (camera == null) {
            camera = createCamera();
            cameraController = new com.panacea.RufusPyramid.game.view.input.OrthoCamController(camera);
            InputManager.get().addProcessor(cameraController);
        }
        return camera;
    }


    public static OrthographicCamera createCamera() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 1024, 1024);       // 360x640
        //camera.setToOrtho(false, (w / h) * 640f, 640f);       // 360x640
//        camera.setToOrtho(false, 800, 480);
        camera.position.set(0, 0, 0);
        camera.zoom = .5f;
        camera.update();

        return camera;
    }

}
