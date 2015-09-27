package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimWalk extends AbstractAnimation {

    private Animation animation;
    private TextureRegion[] frames;
    private SpriteBatch spriteBatch; /*agent di libgdx per disegnare*/
    private TextureRegion currentFrame;

    private GridPoint2 startPoint;
    private GridPoint2 endPoint;

    private float speed;
    private float stateTime;
    private float frameDuration = 0;

    private Vector2 currentPos;
    private Vector2 velocity;
    private Vector2 deltaMovement;
    private Vector2 endPos;
    private Vector2 direction;
    private boolean flipX;//the texture should be mirrored?
    private AssetManager soundsManager;

    private Class modelClass;

    Sound footsteps;
    int randSound;

    public AnimWalk() {
        this.frameDuration = 0.33f;
    }

    public AnimWalk(Class modelClass, GridPoint2 startPoint, GridPoint2 endPoint, float speed, boolean flipX) {
        super();
        this.frameDuration = 0.1f;  //TODO imposta frameDuration in base alla velocità! Circa speed/200, ma deve essere in proporzione inversa
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.speed = speed;
        this.modelClass = modelClass;
        this.flipX = flipX;
    }

    public void create() {
        if (this.modelClass == DefaultHero.class) {
            frames = SpritesProvider.getSprites(SpritesProvider.Oggetto.HERO1, SpritesProvider.Azione.WALK);
            randSound = Utilities.randInt(0, SoundsProvider.Sounds.FOOTSTEPS_INTERNAL.getValue() - 1);
            SoundsProvider.get().loadSound(SoundsProvider.Sounds.FOOTSTEPS_INTERNAL);

        } else {    //TODO gestire tutti i nemici
            frames = SpritesProvider.getSprites(SpritesProvider.Oggetto.ORC_BASE, SpritesProvider.Azione.WALK);
        }
        animation = new Animation(frameDuration, frames);      // #11
        spriteBatch = GameBatch.get();                // #12
        stateTime = 0f;                         // #13

        // Reference: http://stackoverflow.com/questions/17694076/moving-a-point-vector-on-an-angle-libgdx
        // Declared as fields, so they will be reused
        GridPoint2 absoluteStartPoint = Utilities.convertToAbsolutePos(startPoint);
        currentPos = new Vector2(absoluteStartPoint.x, absoluteStartPoint.y);
        velocity = new Vector2();
        deltaMovement = new Vector2();

        GridPoint2 absoluteEndPoint = Utilities.convertToAbsolutePos(endPoint);
        endPos = new Vector2(absoluteEndPoint.x, absoluteEndPoint.y);
        direction = new Vector2();
        // On touch events, set the touch vector, then do this to get the direction vector
        direction.set(endPos).sub(currentPos).nor();

        // scale it to the speed you want to move, then use it to update your position.
        velocity = new Vector2(direction).scl(speed);
    }

    @Override
    public void render(float delta) {
        if(SoundsProvider.get().isLoaded(SoundsProvider.Sounds.FOOTSTEPS_INTERNAL)) {
            if(footsteps == null) {
                footsteps = SoundsProvider.get().getSound(SoundsProvider.Sounds.FOOTSTEPS_INTERNAL)[randSound];
                footsteps.play(0.1f);
            }
        }
        boolean toDispose = false;
        deltaMovement.set(velocity).scl(delta);

        if(this.modelClass == DefaultHero.class)
            GameModel.get().getHero().setAbsoluteTickPosition(currentPos);
        if (currentPos.dst2(endPos) > deltaMovement.len2()) { //Se la distanza tra la posiz. attuale e la posiz. finale è minore di deltaMovement
            currentPos.add(deltaMovement);
            stateTime += delta;
        } else {
            currentPos.set(endPos);
            stateTime += delta; //FIXME non è proprio delta, andrebbe fatta una proporzione!
            toDispose = true;
        }

        currentFrame = animation.getKeyFrame(stateTime, true);

        spriteBatch.begin();
        if((!currentFrame.isFlipX() && flipX) || (currentFrame.isFlipX() && !flipX))
            currentFrame.flip(true, false);
        spriteBatch.draw(currentFrame, currentPos.x, currentPos.y, 32, 32);
        spriteBatch.end();

        if (toDispose) {
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
