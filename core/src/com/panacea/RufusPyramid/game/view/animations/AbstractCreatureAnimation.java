
package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Created by gio on 06/10/15.
 */
public class AbstractCreatureAnimation extends AbstractAnimation {

    private final SpritesProvider.Azione actionType;
    private Animation animation;
    private TextureRegion[] frames;
    private SpriteBatch spriteBatch;
    private TextureRegion currentFrame;

    private float stateTime;
    private float frameDuration = 0;
    private GridPoint2 absolutePosition;

    private ICreature model;
    private boolean flipX;//the texture should be mirrored?

    private Sound sound;
    private float soundVolume = 1f;
    private Long soundId;

    public AbstractCreatureAnimation(ICreature creature, AnimationData data, SpritesProvider.Azione actionType) {
        this.frameDuration = 0.05f;
        this.model = creature;
        this.setAbsolutePosition(Utilities.convertToAbsolutePos(data.position));
        this.flipX = data.flipX;
        this.actionType = actionType;
    }

    public void create() {
        frames = SpritesProvider.getSprites(this.model.getCreatureType(), this.actionType);
        animation = new Animation(frameDuration, frames);
        spriteBatch = GameBatch.get();
        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        if (this.sound != null) {
            if (this.soundId == null) {
                this.soundId = this.sound.play(this.soundVolume);
            } else {
                this.sound.setVolume(this.soundId, this.soundVolume);
            }
        }
        stateTime += delta;

        animation.getKeyFrameIndex(stateTime);
        currentFrame = animation.getKeyFrame(stateTime, false);
        if((!currentFrame.isFlipX() && flipX) || (currentFrame.isFlipX() && !flipX))
            currentFrame.flip(true, false);

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

    public void setAbsolutePosition(GridPoint2 position) {
        this.absolutePosition = position;
    }

    public GridPoint2 getAbsolutePosition() {
        return this.absolutePosition;
    }

    public void setAnimationSound(Sound sound) {
        //TODO: dispose old sound?
//        if (this.sound != null && SoundsProvider.get().isLoaded(this.sound)) {
            //SoundsProvider.get().dispose(this.sound);
//        }
        this.sound = sound;
    }

    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
    }
}
