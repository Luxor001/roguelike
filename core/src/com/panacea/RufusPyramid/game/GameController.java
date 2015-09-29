package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.screens.GameOverScreen;
import com.panacea.RufusPyramid.game.view.screens.GameScreen;

/**
 * Permette dall'esterno di recuperare tutti i controller istanziati
 * Created by gio on 20/07/15.
 */
public class GameController {

    private static GameMaster gm;

    public static void initializeGame() {
        //TODO
        GameModel.createInstance();
        gm = new GameMaster();
    }

    public static void endGame() {
        gm.disposeGame();
        GameModel.get().disposeAll();
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen());
    }

    public static void step() {
        gm.step();
    }
}
