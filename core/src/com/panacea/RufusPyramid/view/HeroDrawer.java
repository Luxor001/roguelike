package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.creatures.AbstractCreature;
import com.panacea.RufusPyramid.creatures.DefaultHero;

import java.util.ArrayList;

/**
 * Created by gio on 16/07/15.
 */
public class HeroDrawer extends ViewObject {

    private DefaultHero heroModel;
    private AbstractCreature.PositionChangeListener posChangeListener;
    private HeroState currentState;

    private GridPoint2 spritePosition;
    private Texture sprite;

    public HeroDrawer(DefaultHero heroModel) {
        this.heroModel = heroModel;
        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
            @Override
            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                //Faccio l'animazione di camminata in base ai dati dell'evento
                Gdx.app.log(HeroDrawer.class.toString(), "Catturato un evento PositionChangeEvent.");
                HeroDrawer.this.walkAnimation((DefaultHero) source, event.getPath());
                //Poi renderizzo l'eroe in setStanding (da impostare al termine dell'animazione)
//                HeroDrawer.this.setStanding();
            }
        };
        this.heroModel.addChangeListener(this.posChangeListener);
        Gdx.app.log(HeroDrawer.class.toString(), "creazione di HeroDrawer");
        sprite = getHeroSprite(heroModel);
        this.setStanding();
    }

    private static Texture getHeroSprite(DefaultHero heroModel) {
        //TODO ritornare la texture corretta a seconda del modello di eroe scelto.
        return new Texture(Gdx.files.internal("data/spritesheet2_single.png"));
    }

    private void walkAnimation(DefaultHero source, ArrayList<GridPoint2> path) {
        //TODO animate
        currentState = HeroState.WALKING;
    }

    private void setStanding() {
        currentState = HeroState.STANDING;
    }

    @Override
    public void render() {
        super.render();

        switch(this.currentState) {
            case WALKING:
                //TODO fare l'animazione di camminata
//                break;
            case STANDING:
                GridPoint2 pos = this.heroModel.getPosition().getPosition();
//                Gdx.app.log(HeroDrawer.class.toString(), "Eroe in camminata alla posizione: " + pos.x + "-" + pos.y);
                spritePosition = this.heroModel.getPosition().getPosition();
                SpriteBatch batch = GameBatch.get();

                batch.begin();
                batch.draw(this.sprite, spritePosition.x, spritePosition.y, 32, 32);
                batch.end();

                break;
            default:
                Gdx.app.debug(HeroDrawer.class.toString(), "Non so che sprite disegnare! Stato corrente: " + this.currentState);
                break;
        }
    }

    private enum HeroState {
        STANDING,
        WALKING
    }
}
