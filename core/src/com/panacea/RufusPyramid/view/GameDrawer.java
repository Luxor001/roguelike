package com.panacea.RufusPyramid.view;

import com.panacea.RufusPyramid.GameModel;
import com.panacea.RufusPyramid.creatures.HeroController;
import com.panacea.RufusPyramid.map.MapContainer;

import java.util.LinkedList;
import java.util.List;

/**
 * Contiene tutti gli oggetti da renderizzare a video durante una sessione di gioco.
 * E' eventualmente possibile aggiungerne e rimuoverne di altri.
 * Created by gio on 11/07/15.
 */
public class GameDrawer extends ViewObject {
    private List<ViewObject> viewList;
    MapView map;

    public void add(ViewObject toAdd) {
        this.viewList.add(toAdd);
    }

    public void remove(ViewObject toRemove) {
        this.viewList.remove(toRemove);
    }

    @Override
    public void create() {
        super.create();

        map = new MapView(new MapContainer(30, 30));  //map.create richiamato automaticamente da ViewObject
        this.viewList = new LinkedList<ViewObject>();
        this.viewList.add(map);
        this.viewList.add(new Animator());
        this.viewList.add(new HeroDrawer(GameModel.get().getHero()));

        //TODO da spostare! Istanziarlo insieme agli altri, futuri, controllers
        new HeroController(GameModel.get().getHero());
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        for (ViewObject view : viewList) {
            view.resize(width, height);
        }
    }

    @Override
    public void render() {
        super.render();

        GameCamera.get().update();
        GameBatch.get().setProjectionMatrix(GameCamera.get().combined);

        for (ViewObject view : viewList) {
            view.render();
        }
    }

    @Override
    public void pause() {
        super.pause();

        for (ViewObject view : viewList) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();

        for (ViewObject view : viewList) {
            view.resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (ViewObject view : viewList) {
            view.dispose();
        }
    }
}
