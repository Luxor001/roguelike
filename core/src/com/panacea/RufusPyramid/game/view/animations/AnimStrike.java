package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimStrike extends AbstractAnimation {

    private Animation animation;
    private TextureRegion[] frames;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;

    private float stateTime;
    private float frameDuration = 0;
    private GridPoint2 absolutePosition;

    private Class modelClass;

    public AnimStrike() {
        this.frameDuration = 0.33f;
    }

    public AnimStrike(Class modelClass, GridPoint2 position) {
        super();
        this.frameDuration = 0.05f;
        this.modelClass = modelClass;
        this.absolutePosition = Utilities.convertToAbsolutePos(position);
    }

    public void create() {
        if (this.modelClass == DefaultHero.class) {
            frames = SpritesProvider.getSprites(SpritesProvider.Oggetto.HERO1, SpritesProvider.Azione.STRIKE);
        } else {    //TODO gestire tutti i nemici
            frames = SpritesProvider.getSprites(SpritesProvider.Oggetto.ORC_BASE, SpritesProvider.Azione.STRIKE);
        }
        animation = new Animation(frameDuration, frames);
        spriteBatch = GameBatch.get();
        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        animation.getKeyFrameIndex(stateTime);
        currentFrame = animation.getKeyFrame(stateTime, false);

        spriteBatch.begin();
        spriteBatch.draw(currentFrame, absolutePosition.x, absolutePosition.y, 32, 32);
        spriteBatch.end();

        if (animation.isAnimationFinished(stateTime)) {
            this.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //TODO libera tutte le risorse
        this.fireAnimationEndedEvent();
    }
}
