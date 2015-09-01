package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameScreen;
import com.panacea.RufusPyramid.map.MapFactory;

import java.util.Random;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {

	@Override
	public void create () {
        Gdx.input.setInputProcessor(InputManager.getInstance());
        this.setScreen(new GameScreen());
        MapFactory a=new MapFactory();
        a.generateMap(new Random(System.nanoTime()).nextInt());
	}


	@Override
	public void render () {
        super.render();
    }

}
