package com.panacea.RufusPyramid;

import com.badlogic.gdx.Game;
import com.panacea.RufusPyramid.common.AssetsProvider;
import com.panacea.RufusPyramid.common.Database;
import com.panacea.RufusPyramid.common.StaticDataProvider;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;
import com.panacea.RufusPyramid.game.view.screens.SplashScreen;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game {
    private Database gameDb;

    public Main(Database gameDb) {
        this.gameDb = gameDb;
    }

	@Override
	public void create () {
        this.setScreen(new SplashScreen());
//        gameDb.onCreate();    //Chiamato automaticamente se necessario

        SoundsProvider.requestAssets();
        AssetsProvider.get().finishLoading();   //TODO: Chiamata bloccante, andrà usata una chiamata asincrona se si vuole visualizzare una barra di caricamento

        StaticDataProvider.setDatabase(gameDb);
        SoundsProvider.get().loadAllSounds();
//        AssetsProvider.finishLoading();
        this.setScreen(new MenuScreen());
	}


	@Override
	public void render () {
        super.render();
    }
}
