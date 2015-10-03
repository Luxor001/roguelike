package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {

	@Override
	public void create () {
        this.setScreen(new MenuScreen());
	}


	@Override
	public void render () {
        super.render();
    }
}
