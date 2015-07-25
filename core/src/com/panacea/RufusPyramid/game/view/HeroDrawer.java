package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.view.animations.AbstrAnimation;
import com.panacea.RufusPyramid.game.view.animations.AnimWalk;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedEvent;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;
import com.panacea.RufusPyramid.game.view.input.InputManager;

import java.util.ArrayList;

/**
 * Created by gio on 16/07/15.
 */
public class HeroDrawer extends ViewObject {

    private DefaultHero heroModel;
    private HeroController heroController;
    private com.panacea.RufusPyramid.game.view.input.HeroInputManager heroInput;
    private AbstractCreature.PositionChangeListener posChangeListener;
    private HeroState currentState;

    private GridPoint2 spritePosition;
    private Texture sprite;
    private AbstrAnimation currentAnimation = null;

    public HeroDrawer(/*HeroController heroController, */DefaultHero heroModel) {
        this.heroModel = heroModel;


        //TODO da spostare! Istanziarlo insieme agli altri, futuri, controllers
        this.heroController = new HeroController(GameModel.get().getHero());
        this.heroInput = new com.panacea.RufusPyramid.game.view.input.HeroInputManager(this.heroController);
        InputManager.get().addProcessor(this.heroInput);

        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
            @Override
            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
                HeroDrawer.this.heroInput.setPaused(true);
                AnimationEndedListener listener = new AnimationEndedListener() {
                    @Override
                    public void ended(AnimationEndedEvent event, Object source) {
                        //Poi renderizzo l'eroe in setStanding e riabilito l'input
                        HeroDrawer.this.setStanding();
                        HeroDrawer.this.heroInput.setPaused(false);
                    }
                };
                HeroDrawer.this.walkAnimation((DefaultHero) source, event.getPath(), listener);
            }
        };
        this.heroModel.addChangeListener(this.posChangeListener);
        sprite = getHeroSprite(heroModel);
        this.setStanding();
    }

    private static Texture getHeroSprite(DefaultHero heroModel) {
        //TODO ritornare la texture corretta a seconda del modello di eroe scelto.
        return new Texture(Gdx.files.internal("data/spritesheet2_single.png"));
    }

    private void walkAnimation(DefaultHero source, ArrayList<GridPoint2> path, AnimationEndedListener listener) {
        //TODO effettuare l'animazione di punto in punto con un ciclo.
        this.startWalk(path.get(0), path.get(1));
        currentAnimation.addListener(listener);
        GridPoint2 a = path.get(0), b = path.get(1);
//        Gdx.app.log(HeroDrawer.class.toString(), "Inizio animazione, hero walking. Da " + a.x + "," + a.y + " a " + b.x + "," + b.y);
        currentState = HeroState.WALKING;
    }

    private void setStanding() {
        currentState = HeroState.STANDING;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        switch(this.currentState) {
            case WALKING:
                //TODO fare l'animazione di camminata
                if (currentAnimation != null) {
                    currentAnimation.render(delta);
//                    Gdx.app.log(HeroDrawer.class.toString(), "Hero walking, animazione in corso.");
                }
                break;
            case STANDING:
                GridPoint2 pos = this.heroModel.getPosition().getPosition();
//                Gdx.app.log(HeroDrawer.class.toString(), "Eroe in camminata alla posizione: " + pos.x + "-" + pos.y);
                spritePosition = this.heroModel.getPosition().getPosition();
                SpriteBatch batch = com.panacea.RufusPyramid.game.view.GameBatch.get();

                batch.begin();
                batch.draw(this.sprite, spritePosition.x, spritePosition.y, 32, 32);
                batch.end();

                break;
            default:
                Gdx.app.error(HeroDrawer.class.toString(), "Non so che sprite disegnare! Stato corrente: " + this.currentState);
                break;
        }
    }

    private void startWalk(GridPoint2 startPos, GridPoint2 endPos) {
        currentAnimation = new AnimWalk(startPos, endPos, 40.0f);
        currentAnimation.create();
    }

    private enum HeroState {
        STANDING,
        WALKING
    }
}
