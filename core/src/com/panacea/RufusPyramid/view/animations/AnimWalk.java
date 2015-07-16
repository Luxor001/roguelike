package com.panacea.RufusPyramid.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.view.GameCamera;
import com.panacea.RufusPyramid.view.ViewObject;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 *
 * Created by gio on 11/07/15.
 */
public class AnimWalk extends ViewObject {

    private static final int FRAME_COLS = 4;/*Grandezza della matrice degli sprite per l'animazione. Direi che 20 sprites bastano e avanzano*/
    private static final int FRAME_ROWS = 7;

    Animation walkAnimation;
    Texture animationTexture;
    TextureRegion[] walkFrames;
    SpriteBatch spriteBatch; /*agent di libgdx per disegnare*/
    TextureRegion currentFrame;

    GridPoint2 currPoint;
    float stateTime;
    float frameDuration=0;
    public AnimWalk(float frameDuration, TextureRegion... keyFrames){
        this.frameDuration=frameDuration;
        walkFrames=keyFrames;
    }

    @Override
    public void create() {
        animationTexture = new Texture(Gdx.files.internal("data/spritesheet2.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(animationTexture, animationTexture.getWidth()/FRAME_COLS, animationTexture.getHeight()/FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(frameDuration, walkFrames);      // #11
        spriteBatch = new SpriteBatch();                // #12
        spriteBatch.setProjectionMatrix(GameCamera.getInstance().combined);
        stateTime = 0f;                         // #13
        currPoint = new GridPoint2(0,0);
    }

    public void updateLocation(GridPoint2 point){
        /*currPoint=point;*/
    }

    @Override
    public void render() {
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);  /*pulisci lo schermo*/
        OrthographicCamera camera = GameCamera.getInstance();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, currPoint.x,currPoint.y, 32, 32);             // #17
        spriteBatch.end();
    }
}
