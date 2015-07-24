package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Contiene tutti gli oggetti da renderizzare a video durante una sessione di gioco.
 * E' eventualmente possibile aggiungerne e rimuoverne di altri.
 * Created by gio on 11/07/15.
 */
public class GameDrawer extends com.panacea.RufusPyramid.game.view.ViewObject {
    private List<com.panacea.RufusPyramid.game.view.ViewObject> viewList;
    MapView map;

    public GameDrawer() {
        map = new MapView(new MapContainer(30, 30));  //map.create richiamato automaticamente da ViewObject
        this.viewList = new LinkedList<com.panacea.RufusPyramid.game.view.ViewObject>();
        this.viewList.add(map);
        this.viewList.add(new Animator());
        this.viewList.add(new HeroDrawer(GameModel.get().getHero()));
        List<ICreature> creatures = new ArrayList<ICreature>();
        ICreature e1 = new Enemy("Thief", "", 10, 1, 1, 1);
        e1.setPosition(new Tile(new GridPoint2(0, 0), Tile.TileType.Solid));
        creatures.add(e1);
        ICreature e2 = new Enemy("Thief", "", 10, 1, 1, 1);
        e2.setPosition(new Tile(new GridPoint2(1,1), Tile.TileType.Solid));
        creatures.add(e2);
        GameMaster gm = new GameMaster();
        gm.addAgent(e1);
        gm.addAgent(e2);
//        gm.addAgent(new DefaultHero("Ciao"));
        this.viewList.add(new CreaturesDrawer(creatures));
        gm.startTurns();
    }

    public void add(com.panacea.RufusPyramid.game.view.ViewObject toAdd) {
        this.viewList.add(toAdd);
    }

    public void remove(com.panacea.RufusPyramid.game.view.ViewObject toRemove) {
        this.viewList.remove(toRemove);
    }

    @Override
    public void create() {
        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.create();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resize(width, height);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        GameCamera.get().update();
        GameBatch.get().setProjectionMatrix(GameCamera.get().combined);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.render(delta);
        }
    }

    @Override
    public void pause() {
        super.pause();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.dispose();
        }
    }
}
