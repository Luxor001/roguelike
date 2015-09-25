package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

import java.util.ArrayList;
import java.util.List;

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
    private boolean flipX;//the texture should be mirrored?

    private Sound slashSound;
    private int randSound;

    public AnimStrike() {
        this.frameDuration = 0.33f;
    }
    public AnimStrike(Class modelClass, GridPoint2 position, boolean flipX) {
        super();
        this.frameDuration = 0.05f;
        this.modelClass = modelClass;
        this.absolutePosition = Utilities.convertToAbsolutePos(position);
        this.flipX = flipX;
    }

    public void create() {
        randSound = Utilities.randInt(0, SoundsProvider.Sounds.COMBAT_SLICE.getValue()-1);
        SoundsProvider.get().loadSound(SoundsProvider.Sounds.COMBAT_SLICE);

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
        if(SoundsProvider.get().isLoaded(SoundsProvider.Sounds.COMBAT_SLICE)) {
            if(slashSound == null) {
                slashSound = SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_SLICE)[randSound];
                slashSound.play();
            }
        }
        stateTime += delta;
        animation.getKeyFrameIndex(stateTime);
        currentFrame = animation.getKeyFrame(stateTime, false);

        spriteBatch.begin();
        if((!currentFrame.isFlipX() && flipX) || (currentFrame.isFlipX() && !flipX))
            currentFrame.flip(true, false);
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
