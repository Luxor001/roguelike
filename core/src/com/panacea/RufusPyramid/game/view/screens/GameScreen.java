package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.creatures.ThiefEnemy;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    GameDrawer objectsDrawer;
    GameMaster gm;

    public void show() {
        Gdx.input.setInputProcessor(InputManager.get());
        GameModel.createInstance();
        this.gm = new GameMaster();

        ICreature e1 = new Enemy("Enemy", "", 10, 1, 1, 1);
        e1.setPosition(new Tile(new GridPoint2(64, 0), Tile.TileType.Solid));
        GameModel.get().addCreature(e1);
        ICreature e2 = new ThiefEnemy("Thief", "", 10, 1, 1, 1);
        e2.setPosition(new Tile(new GridPoint2(0, 64), Tile.TileType.Solid));
        GameModel.get().addCreature(e2);
        gm.addAgent(e1);
        gm.addAgent(e2);
        gm.addAgent(GameModel.get().getHero());

        objectsDrawer = GameDrawer.get();
        objectsDrawer.create();
//        Gdx.app.log(GameScreen.class.toString(), "show() di GameScreen");
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //richiama uno step della turnazione
        this.gm.step();

//        objectsDrawer.render(Gdx.graphics.getDeltaTime());
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
        objectsDrawer.pause();
    }

    public void dispose() {
        objectsDrawer.dispose();
    }
}
