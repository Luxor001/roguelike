package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameScreen;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;
import com.panacea.RufusPyramid.game.view.screens.SplashScreen;
import com.panacea.RufusPyramid.map.MapFactory;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {

	@Override
	public void create () {
        this.setScreen(new MenuScreen());
        MapFactory a=new MapFactory();
        a.generateMap(2);
	}


	@Override
	public void render () {
        super.render();
    }

}
