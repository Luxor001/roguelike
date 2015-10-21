package com.panacea.RufusPyramid;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.AssetsProvider;
import com.panacea.RufusPyramid.common.Database;
import com.panacea.RufusPyramid.common.StaticDataProvider;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.SpritesProvider;
import com.panacea.RufusPyramid.game.view.screens.MenuScreen;
import com.panacea.RufusPyramid.game.view.screens.SplashScreen;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.save.SaveLoadHelper;

/* "Game" permette di suddividere l'applicazione in più "Screen" (main menù, gioco, highscores, etc.) */
public class Main extends Game implements ApplicationListener {
    private Database gameDb;

    public Main(Database gameDb) {
        this.gameDb = gameDb;
    }

	@Override
	public void create () {
        this.setScreen(new SplashScreen());
//        gameDb.onCreate();    //Chiamato automaticamente se necessario
        StaticDataProvider.setDatabase(gameDb);

        SoundsProvider.requestAssets();
        SpritesProvider.requestAssets();
        AssetsProvider.get().finishLoading();   //TODO: Chiamata bloccante, andrà usata una chiamata asincrona se si vuole visualizzare una barra di caricamento

        SoundsProvider.get().loadAllSounds();
//        AssetsProvider.finishLoading();
        this.setScreen(new MenuScreen());
        SaveLoadHelper sl = new SaveLoadHelper();

        try {
        /*    sl.startLoad();
            GameModel loaded = sl.loadObject(GameModel.class);
            Gdx.app.log("", loaded.getHero().getName());
            int a = 0;
            sl.stopLoad();*/
        }
        catch (Exception e){
            e.printStackTrace();
        }
	}

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
        if(GameModel.get() != null){
            SaveLoadHelper sl = new SaveLoadHelper();
            sl.startSave();
            sl.saveObject(GameModel.get());
            sl.stopSave();
        }

        //saveAll
    }

    @Override
    public void resume () {
        Gdx.app.log("sada","resumewewwewewe");
    }

    @Override
    public void dispose() {
        Gdx.app.log("sada", "disposewewewewe");
    }

	@Override
	public void render () {
        super.render();
    }
}
