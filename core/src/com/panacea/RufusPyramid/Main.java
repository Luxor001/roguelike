package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.panacea.RufusPyramid.common.Database;
import com.panacea.RufusPyramid.common.StaticDataProvider;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {
    private Database gameDb;

    public Main(Database gameDb) {
        this.gameDb = gameDb;
        StaticDataProvider.setDatabase(gameDb);
    }

	@Override
	public void create () {
        this.setScreen(new MenuScreen());
        gameDb.onCreate();
	}


	@Override
	public void render () {
        super.render();
    }
}
