package com.panacea.RufusPyramid.game.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameCamera;
import com.panacea.RufusPyramid.game.view.SpritesProvider;
import com.panacea.RufusPyramid.game.view.animations.AbstractAnimation;
import com.panacea.RufusPyramid.game.view.animations.AnimatedImage;

/**
 * Created by gio on 30/07/15.
 */
public class LoadScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private AbstractAnimation animation;
    TextureRegion sprite;
    private SpriteBatch batch;
    ICreature creature;

    public static final float VIEWPORT_HEIGHT = 640f;
    private float w = (float)Gdx.graphics.getWidth();
    private float h = (float)Gdx.graphics.getHeight();


    public LoadScreen() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        float fontScale = 1;
        float Maxx= GameCamera.get().viewportWidth;
        this.stage = new Stage(new FitViewport((w / h) * VIEWPORT_HEIGHT, VIEWPORT_HEIGHT));
        this.skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.getAtlas().getTextures().iterator().next().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        skin.getFont("default-font").getData().markupEnabled = true;
        skin.getFont("default-font").getData().setScale(fontScale);
        Label label = new Label("Loading..,",skin);
        label.setPosition(stage.getWidth()/2-30, 90);
        this.stage.addActor(label);


        TextureRegion[] frames = SpritesProvider.get().getStaticSprites(SpritesProvider.OggettoStatico.FIRE);
        Animation animation = new Animation(0.05f, frames);
        AnimatedImage animImg = new AnimatedImage(animation);
        animImg.setPosition(stage.getWidth()/2-30, 110);
        stage.addActor(animImg);

    }

    @Override
    public void hide() {
        dispose();
    }


    private static TextureRegion getCreatureSprite(ICreature creature) {
        return SpritesProvider.get().getSprites(creature.getCreatureType(), SpritesProvider.Azione.STAND)[0];
    }

    private void animate(float delta) {
        if (animation != null) {
            animation.render(delta);
        }
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        try{
            skin.dispose();
            try {
                stage.dispose();
            } catch (IllegalArgumentException ex) {}
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}