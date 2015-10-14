package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.panacea.RufusPyramid.game.view.input.InputManager;
import com.panacea.RufusPyramid.game.view.screens.GameOverScreen;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

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
        }
        facebook.signOut();
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
