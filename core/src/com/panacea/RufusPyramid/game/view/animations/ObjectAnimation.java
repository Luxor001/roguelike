package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.view.GameBatch;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

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
        this.stateTime = 0;
    }

    public void create() {
        frames = SpritesProvider.get().getStaticSprites(type);

        animation = new Animation(frameDuration, frames);
        spriteBatch = GameBatch.get();
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        animation.getKeyFrameIndex(stateTime);
        currentFrame = animation.getKeyFrame(stateTime,true);

        spriteBatch.begin();
        if((!currentFrame.isFlipX() && flipX) || (currentFrame.isFlipX() && !flipX))
            currentFrame.flip(true, false);

        spriteBatch.draw(currentFrame, absolutePosition.x, absolutePosition.y, 64, 64);
        spriteBatch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
