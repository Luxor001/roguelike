package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.InputManager;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {

	@Override
	public void create () {
        Gdx.input.setInputProcessor(InputManager.getInstance());
        this.setScreen(new com.panacea.RufusPyramid.view.screens.GameScreen());
	}


	@Override
	public void render () {
        super.render();
    }

}
