package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.MusicPlayer;
import com.panacea.RufusPyramid.game.view.animations.ObjectAnimation;

import java.util.ArrayList;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

/**
 * Created by gio on 30/07/15.
 */
public class GameOverScreen implements Screen {

    /**
     * Altezza del viewport, la larghezza viene determinata usando larghezza e altezza reali del dispositivo:
     * VIEWPORT_WIDTH = (w / h) * VIEWPORT_HEIGHT
     */
    private static final float VIEWPORT_HEIGHT = 640f;

    private float w = (float)Gdx.graphics.getWidth();
    private float h = (float)Gdx.graphics.getHeight();

    private Stage stage = new Stage(new FitViewport((w / h) * VIEWPORT_HEIGHT, VIEWPORT_HEIGHT));
    private Table table = new Table();

    Skin skin;
    private TextButton buttonPlay;
    private TextButton buttonMenu;
//    private Label title;
    private Texture texture;
    private Image title;
    private ArrayList<ObjectAnimation> menuAnimations;
    private ObjectAnimation fireAnim;
    private  boolean notificationTextVisible;
    private BitmapFont notificationText;
    private int notificationTextWidth;
//    private Music introMusic;

    private final static String decalsDirectory = "data/deco/gameover";
    private TextureRegion[] bloodDecals;
    private SpriteBatch batch;

    @Override
    public void show() {

        batch = new SpriteBatch();
//        introMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sfx/death_music.ogg"));
        MusicPlayer.setAmbient(MusicPlayer.AmbientType.GAMEOVER);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.getFont("default-font").getData().markupEnabled = true;
        float scale = 1;
        skin.getFont("default-font").getData().setScale(scale);

        buttonPlay = new TextButton("Restart", skin);
        buttonMenu = new TextButton("Menu", skin);

//        title = new Label("Game Title", skin);
        texture = new Texture(Gdx.files.internal("data/gameover.png"));
        title = new Image(texture);
        stage.addActor(title);


        notificationText = new BitmapFont();
        notificationText.setColor(Color.WHITE);
        notificationText.getData().setScale(2.5f);

        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(notificationText, "Score on facebook posted!");
        notificationTextWidth = (int) layout.width;// contains the width of the current set text
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go

//                ((Game)Gdx.app.getApplicationListener()).setScreen(new SplashScreen());

                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                dispose();
            }
        });


        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
                dispose();
            }
        });
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonMenu).size(150, 60).padBottom(20).row();

        table.setFillParent(true);

        // Settaggio delle texture con le macchie di sangue
        int numBloodDecals = 2;

        this.bloodDecals = this.loadDecals();
        for (int i = 0; i < numBloodDecals; i++) {
            int rand = MathUtils.random(this.bloodDecals.length - 1);
            TextureRegion chosenDecal = this.bloodDecals[rand];
            GridPoint2 randomCoords = new GridPoint2(MathUtils.random(Math.round(this.stage.getWidth())), MathUtils.random(Math.round(this.stage.getHeight())));

            Image blood1 = new Image(chosenDecal);
            blood1.setX(randomCoords.x - blood1.getWidth()/2);
            blood1.setY(randomCoords.y - blood1.getHeight()/2);
            stage.addActor(blood1);
        }

        //Aggiunta della table con titolo e pulsanti allo stage
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
//        introMusic.play();
//        introMusic.setLooping(true);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(notificationTextVisible)
            notificationText.draw(batch, "Score on facebook posted!", GameCamera.get().viewportWidth/2-notificationTextWidth/3, 250 );

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
//        introMusic.dispose();
        texture.dispose();

        this.bloodDecals[0].getTexture().dispose();
    }

    private TextureRegion[] loadDecals() {
        String rootDirectory = this.decalsDirectory;
        ArrayList<TextureRegion> allTextures = new ArrayList<TextureRegion>();
        FileHandle files = Gdx.files.internal(rootDirectory);
        Texture texture;
        for (FileHandle file: files.list()) {
            texture = new Texture(file);
            TextureRegion[][] reg =  TextureRegion.split(texture, texture.getWidth(), texture.getHeight());
            allTextures.add(reg[0][0]);
        }
        TextureRegion[] values = new TextureRegion[allTextures.size()];
        return allTextures.toArray(values);
    }
}
