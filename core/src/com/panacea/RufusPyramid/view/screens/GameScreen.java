package com.panacea.RufusPyramid.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.panacea.RufusPyramid.view.Animator;
import com.panacea.RufusPyramid.view.ViewObject;
import com.panacea.RufusPyramid.map.MapDrawer;

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen {

    MapDrawer map;
    List<ViewObject> viewList = new LinkedList<ViewObject>();

    public void show() {
        map = new MapDrawer();  //map.create richiamato automaticamente da ViewObject
        viewList.add(map);
        viewList.add(new Animator());
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (ViewObject view : viewList) {
            view.render();
        }
    }

    public void resize(int width, int height) {
        for (ViewObject view : viewList) {
            view.resize(width, height);
        }
    }

    public void pause() {
        for (ViewObject view : viewList) {
            view.pause();
        }
    }

    public void resume() {
        for (ViewObject view : viewList) {
            view.resume();
        }
    }

    public void hide() {
        for (ViewObject view : viewList) {
            view.pause();
        }
    }

    public void dispose() {
        for (ViewObject view : viewList) {
            view.dispose();
        }
    }
}
