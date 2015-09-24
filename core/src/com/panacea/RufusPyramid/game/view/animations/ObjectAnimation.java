package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SpritesProvider;
import com.sun.org.apache.bcel.internal.generic.ObjectType;

/**
 * classe che indica tutti gli oggetti "statici" che devono essere animati (acqua, fuoco, oggetti della mappa ecc.)
 * Created by Lux on 23/09/2015.
 */
public class ObjectAnimation extends AbstractAnimation {

    private boolean flipX;//the texture should be mirrored?
    private SpritesProvider.OggettoStatico type;
    private GridPoint2 absolutePosition;
    private TextureRegion[] frames;
    private Animation animation;
    private SpriteBatch spriteBatch;

    private TextureRegion currentFrame;

    private float stateTime;
    private float frameDuration = 0;
    private int scaleX;

    public ObjectAnimation(SpritesProvider.OggettoStatico type, GridPoint2 position, boolean flipX){
        super();
        this.frameDuration = 0.05f;
        this.type=type;
        this.absolutePosition=position;
        this.flipX=flipX;
    }

    public void create() {
        frames = SpritesProvider.getStaticSprites(type);

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
        if((!currentFrame.isFlipX() && flipX) || (currentFrame.isFlipX() && !flipX))
            currentFrame.flip(true, false);

        spriteBatch.draw(currentFrame, absolutePosition.x, absolutePosition.y, 64, 64);
        spriteBatch.end();

        if (animation.isAnimationFinished(stateTime)) {
            stateTime = 0f;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
