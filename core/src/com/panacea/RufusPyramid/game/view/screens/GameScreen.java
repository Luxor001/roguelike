package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.MusicPlayer;
import com.panacea.RufusPyramid.game.view.input.InputManager;

public class GameScreen implements Screen {
    private boolean loadSavedGame;
    GameDrawer objectsDrawer;

    public GameScreen(boolean loadSavedGame) {
        this.loadSavedGame = loadSavedGame;
    }

    public GameScreen() {
        this(false);
    }

    public void show() {
        if (loadSavedGame) {
           GameController.resumeGame();
        } else {
            GameController.initializeGame();
        }
        this.reset();
    }

    public void reset() {
        GameDrawer.reset();
        objectsDrawer = GameDrawer.get();
        objectsDrawer.create();
        InputManager.get().addProcessor(objectsDrawer.getUIDrawer().getStage());
        InputManager.get().addProcessor(GameController.getGm().heroInput);

        Gdx.input.setInputProcessor(InputManager.get());
        MusicPlayer.setAmbient(MusicPlayer.AmbientType.GAME);
    }

    public void initialize(boolean loadSavedGame){
        if (loadSavedGame) {
            GameController.resumeGame();
        } else {
            GameController.initializeGame();
        }
    }

    public void render(float delta) {
//        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //richiama uno step della turnazione
        GameController.step();

        objectsDrawer.render(delta);
    }

    public void resize(int width, int height) {
        objectsDrawer.resize(width, height);
    }

    public void pause() {
        objectsDrawer.pause();
    }

    public void resume() {
        objectsDrawer.resume();
    }

    public void hide() {
      //  objectsDrawer.pause();
    }

    public void dispose() {
        objectsDrawer.dispose();
    }
}
