package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.panacea.RufusPyramid.game.view.MusicPlayer;
import com.panacea.RufusPyramid.game.view.SpritesProvider;
import com.panacea.RufusPyramid.game.view.animations.AnimatedImage;

/**
 * Created by gio on 30/07/15.
 */
public class MenuScreen implements Screen {

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
    private TextButton buttonResume;
    private TextButton buttonPlay;
    private TextButton buttonExit;
    private Texture texture;
    private Image title;
//    private Music introMusic;

    AssetManager manager;

    @Override
    public void show() {
        stage = new Stage(new FitViewport((w / h) * VIEWPORT_HEIGHT, VIEWPORT_HEIGHT));
        table = new Table();
        manager = new AssetManager();
        manager.load("data/sfx/intro.mp3", Music.class);

//        introMusic = Gdx.audio.newMusic(Gdx.files.internal("data/sfx/intro.mp3"));
        MusicPlayer.setAmbient(MusicPlayer.AmbientType.MENU);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.getFont("default-font").getData().markupEnabled = true;
        float scale = 1;
        skin.getFont("default-font").getData().setScale(scale);

        buttonResume = new TextButton("Resume", skin);
        buttonPlay = new TextButton("New Game", skin);
        buttonExit = new TextButton("Exit", skin);

//        title = new Label("Game Title", skin);
        texture = new Texture(Gdx.files.internal("data/title.png"));
        title = new Image(texture);
        stage.addActor(title);

        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go

                //FIXME sembra che per poter cliccare il pulsante bisogna fare un doppioclick.... perchè?
//                ((Game)Gdx.app.getApplicationListener()).setScreen(new SplashScreen());

                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(false));
                dispose();
            }
        });
        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Same way we moved here from the Splash Screen
                //We set it to new Splash because we got no other screens
                //otherwise you put the screen there where you want to go

                //FIXME sembra che per poter cliccare il pulsante bisogna fare un doppioclick.... perchè?
//                ((Game)Gdx.app.getApplicationListener()).setScreen(new SplashScreen());

                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(true));
                dispose();
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                // or System.exit(0);
            }
        });
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonResume).size(150, 60).padBottom(20).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

        /* Aggiungo i fuochi animati */
        TextureRegion[] frames = SpritesProvider.get().getStaticSprites(SpritesProvider.OggettoStatico.FIRE);

        Animation animation = new Animation(0.05f, frames);
        AnimatedImage animImg = new AnimatedImage(animation);
        animImg.setPosition(20, VIEWPORT_HEIGHT - 140);
        stage.addActor(animImg);

//        Animation animation2 = new Animation(0.05f, frames);
        AnimatedImage animImg2 = new AnimatedImage(animation);
        animImg2.setPosition(stage.getWidth() - 80, VIEWPORT_HEIGHT - 140);
        stage.addActor(animImg2);

        //Pulsanti per attivare/disattivare il muto
        Stack stackSpeakersBox = new Stack();

        TextureRegion[] textures = SpritesProvider.get().getStaticSprites(SpritesProvider.OggettoStatico.SPEAKERS);
        final ImageButton btnSpeakerOn = new ImageButton(new TextureRegionDrawable(textures[0]));
        final ImageButton btnSpeakerOff = new ImageButton(new TextureRegionDrawable(textures[1]));
        btnSpeakerOn.setVisible(!MusicPlayer.isMute());
        btnSpeakerOff.setVisible(MusicPlayer.isMute());

        btnSpeakerOn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MusicPlayer.setMute(true);
                btnSpeakerOn.setVisible(false);
                btnSpeakerOff.setVisible(true);
            }
        });
        btnSpeakerOff.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MusicPlayer.setMute(false);
                btnSpeakerOn.setVisible(true);
                btnSpeakerOff.setVisible(false);
            }
        });

        stackSpeakersBox.addActor(btnSpeakerOn);
        stackSpeakersBox.addActor(btnSpeakerOff);

        stackSpeakersBox.setWidth(textures[0].getRegionWidth());
        stackSpeakersBox.setHeight(textures[0].getRegionHeight());

        stackSpeakersBox.setPosition(stage.getWidth() - stackSpeakersBox.getWidth() - 20, 20);
        stage.addActor(stackSpeakersBox);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
//        introMusic.play();
//        introMusic.setLooping(true);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}
