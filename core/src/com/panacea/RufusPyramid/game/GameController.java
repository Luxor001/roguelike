package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameOverScreen;
import com.panacea.RufusPyramid.game.view.screens.GameScreen;
import com.panacea.RufusPyramid.save.SaveLoadHelper;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;

/**
 * Permette dall'esterno di recuperare tutti i controller istanziati
 * Created by gio on 20/07/15.
 */
public class GameController {

    private static GameMaster gm;
    private static boolean gameInPlay;
    public static boolean gameInUI;

    public static GDXFacebook facebook;
    public static Array<String> loginPermissions = new Array<String>();
    public static Array<String> permissionsPublish = new Array<String>();
    public static String facebook_APPID = "1343627892330664"; //FIXME: va messo nei res!


    public static void initializeGame() {
        GameController.gameInPlay = true;
        GameController.resetAll();

        GameModel.createInstance();
        gm = new GameMaster();


        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            GDXFacebookConfig config = new GDXFacebookConfig();
            config.PREF_FILENAME = ".facebookSessionData"; // optional
            config.APP_ID = facebook_APPID; // required
            facebook = GDXFacebookSystem.install(config);
            loginPermissions.add("public_profile");
            permissionsPublish.add("publish_actions");
            facebook.signOut();
        }
    }

    public static void resumeGame() {
        GameController.gameInPlay = true;
        GameController.resetAll();

//        if(GameModel.get() != null){
            SaveLoadHelper sl = SaveLoadHelper.getIstance();
            Gdx.app.log(GameController.class.toString(), "Inizio caricamento");
            sl.startLoad();
            GameModel.set(sl.loadObject(GameModel.class));
            gm = sl.loadObject(GameMaster.class);
            sl.stopLoad();
//           model.addAllAgents(GameModel.get().getCreatures());
            Gdx.app.log(GameController.class.toString(), "Caricamento completato correttamente");
//        }
    }

    public static void toNextMap() {
        GameController.gameInPlay = true;
        GameController.resetAll();

        GameModel.get().changeToNextMap();
        GameController.gm = new GameMaster();

//        GameDrawer.reset();
//        GameDrawer.get().create();
        GameController.initializeGame();

        GameScreen gameScreen = ((GameScreen) ((Game) Gdx.app.getApplicationListener()).getScreen());
        gameScreen.reset();
    }

    private static void resetAll() {
        //TODO
        InputManager.reset();
    }

    public static void endGame() {
        GameController.gameInPlay = false;
        gm.disposeGame();
        GameModel.get().disposeAll();
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen());
    }

    public static void step() {
        gm.step();
    }

    public static GameMaster getGm(){
        return gm;
    }

    public static boolean isGameEnded() {
        return !GameController.gameInPlay;
    }
}
