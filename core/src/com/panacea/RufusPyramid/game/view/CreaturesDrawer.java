package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.animations.AbstrAnimation;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gio on 16/07/15.
 */
public class CreaturesDrawer extends ViewObject {

    private List<ICreature> creaturesList;
    private AbstractCreature.PositionChangeListener posChangeListener;

    //TODO creare un oggetto che contenga Texture, creaturestate e animations in modo da usare una sola hashmap?
    private HashMap<Integer, Texture> sprites;
    private HashMap<Integer, HeroState> currentStates;
    private HashMap<Integer, AbstrAnimation> currentAnimations = null;

    public CreaturesDrawer(List creaturesList) {
        this.creaturesList = creaturesList;
        this.sprites = new HashMap<Integer, Texture>();
        this.currentStates = new HashMap<Integer, HeroState>();
        this.currentAnimations = new HashMap<Integer, AbstrAnimation>();

        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
            @Override
            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
//                AnimationEndedListener listener = new AnimationEndedListener() {
//                    @Override
//                    public void ended(AnimationEndedEvent event, Object source) {
//                        //Poi renderizzo l'eroe in setStanding e riabilito l'input
//                        CreaturesDrawer.this.setStanding();
//                    }
//                };
//                CreaturesDrawer.this.walkAnimation((DefaultHero) source, event.getPath(), listener);
            }
        };
        for (ICreature creature : this.creaturesList) {
//            TODO
//            creature.addChangeListener(this.posChangeListener);
            this.sprites.put(creature.getID(), getCreatureSprite(creature));
            this.setStanding(creature);
        }
    }

    private static Texture getCreatureSprite(ICreature creature) {
        //TODO ritornare la texture corretta a seconda del modello di eroe scelto.
        return new Texture(Gdx.files.internal("data/thf2_rt2.gif"));
    }

    private void walkAnimation(ICreature source, ArrayList<GridPoint2> path, AnimationEndedListener listener) {
        //TODO effettuare l'animazione di punto in punto con un ciclo.
//        this.startWalk(path.get(0), path.get(1));
//        currentAnimation.addListener(listener);
//        GridPoint2 a = path.get(0), b = path.get(1);
//        Gdx.app.log(HeroDrawer.class.toString(), "Inizio animazione, hero walking. Da " + a.x + "," + a.y + " a " + b.x + "," + b.y);
        currentStates.put(source.getID(), HeroState.WALKING);
    }

    private void setStanding(ICreature source) {
        currentStates.put(source.getID(), HeroState.STANDING);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        for (ICreature creature : this.creaturesList) {
            switch (this.currentStates.get(creature.getID())) {
                case WALKING:
                    //TODO fare l'animazione di camminata
//                if (currentAnimation != null) {
//                    currentAnimation.render(delta);
////                    Gdx.app.log(HeroDrawer.class.toString(), "Hero walking, animazione in corso.");
//                }
//                break;
                case STANDING:
                    GridPoint2 pos = creature.getPosition().getPosition();
//                Gdx.app.log(HeroDrawer.class.toString(), "Eroe in camminata alla posizione: " + pos.x + "-" + pos.y);
                    GridPoint2 spritePosition = creature.getPosition().getPosition();
                    SpriteBatch batch = GameBatch.get();

                    batch.begin();
                    batch.draw(this.sprites.get(creature.getID()), spritePosition.x, spritePosition.y, 32, 32);
                    batch.end();

                    break;
                default:
                    Gdx.app.error(CreaturesDrawer.class.toString(), "Non so che sprite disegnare! Stato corrente: " + this.currentStates.get(creature.getID()));
                    break;
            }
        }
    }

    private void startWalk(GridPoint2 startPos, GridPoint2 endPos) {
//        currentAnimation = new AnimWalk(startPos, endPos, 40.0f);
//        currentAnimation.create();
    }

    private enum HeroState {
        STANDING,
        WALKING
    }
}
