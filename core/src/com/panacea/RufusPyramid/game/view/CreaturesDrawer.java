package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.animations.AbstractAnimation;
import com.panacea.RufusPyramid.game.view.animations.AnimDamage;
import com.panacea.RufusPyramid.game.view.animations.AnimStrike;
import com.panacea.RufusPyramid.game.view.animations.AnimWalk;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedEvent;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;
import com.panacea.RufusPyramid.game.view.input.InputManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gio on 16/07/15.
 */
public class CreaturesDrawer extends ViewObject {

    private List<ICreature> creaturesList;
    private AbstractCreature.PositionChangeListener posChangeListener;

    ////////////////////////
    private HeroController heroController;
    private com.panacea.RufusPyramid.game.view.input.HeroInputManager heroInput;
    ///////////////////////

    //TODO creare un oggetto che contenga Texture, creaturestate e animations in modo da usare una sola hashmap?
    private HashMap<Integer, TextureRegion> sprites;
    private HashMap<Integer, CreatureState> currentStates;
    private HashMap<Integer, AbstractAnimation> currentAnimations = null;
    private DelayedRemovalArray<AbstractAnimation> generalAnimations = null;

    public CreaturesDrawer(List creaturesList) {
        this.creaturesList = creaturesList;
        this.sprites = new HashMap<Integer, TextureRegion>();
        this.currentStates = new HashMap<Integer, CreatureState>();
        this.currentAnimations = new HashMap<Integer, AbstractAnimation>();
        this.generalAnimations = new DelayedRemovalArray<AbstractAnimation>();

//        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
//            @Override
//            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
//                AnimationEndedListener listener = new AnimationEndedListener() {
//                    @Override
//                    public void ended(AnimationEndedEvent event, Object source) {
//                        //Poi renderizzo l'eroe in setStanding e riabilito l'input
//                        CreaturesDrawer.this.setStanding();
//                    }
//                };
//                CreaturesDrawer.this.walkAnimation((DefaultHero) source, event.getPath(), listener);
//            }
//        };
        for (ICreature creature : this.creaturesList) {
            this.sprites.put(creature.getID(), getSprite(creature));
            this.setStanding(creature.getID());

            if (creature instanceof DefaultHero) {
                //TODO da spostare! Istanziarlo insieme agli altri, futuri, controllers
                this.heroController = new HeroController(GameModel.get().getHero());
                this.heroInput = new com.panacea.RufusPyramid.game.view.input.HeroInputManager(this.heroController);
                InputManager.get().addProcessor(this.heroInput);
            }
        }


    }

    private static TextureRegion getSprite(ICreature creature) {
        if (creature instanceof DefaultHero) {
            return getHeroSprite((DefaultHero)creature);
        }
        return getCreatureSprite(creature);
    }

    private static TextureRegion getCreatureSprite(ICreature creature) {
        //TODO ritornare la texture corretta a seconda del modello di "cattivo" scelto.
        return SpritesProvider.getSprites(SpritesProvider.Oggetto.ORC_BASE, SpritesProvider.Azione.STAND)[0];
    }

    private static TextureRegion getHeroSprite(DefaultHero heroModel) {
        //TODO ritornare la texture corretta a seconda del modello di eroe scelto.
        return SpritesProvider.getSprites(SpritesProvider.Oggetto.HERO1, SpritesProvider.Azione.STAND)[0];
    }

    private void setStanding(int creatureID) {
        currentStates.put(creatureID, CreatureState.STANDING);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        for (ICreature creature : this.creaturesList) {
            switch (this.currentStates.get(creature.getID())) {

                case WALKING:
                    //TODO fare l'animazione di camminata
                    AbstractAnimation currentAnimation = this.currentAnimations.get(creature.getID());
                    if (currentAnimation != null) {
                        currentAnimation.render(delta);
                    }
                    break;
                case STANDING:
                    GridPoint2 pos = creature.getPosition().getPosition();
                    GridPoint2 spritePosition = creature.getPosition().getPosition();
                    SpriteBatch batch = GameBatch.get();
                    batch.begin();
                    GridPoint2 newPos=Utilities.convertToAbsolutePos(spritePosition);
                    TextureRegion currentFrame = this.sprites.get(creature.getID());
                    if((!currentFrame.isFlipX() && creature.getFlipX()) || (currentFrame.isFlipX() && !creature.getFlipX()))
                        currentFrame.flip(true, false);
                    batch.draw(currentFrame,newPos.x,newPos.y,Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT);
                    batch.end();

                    break;
                default:
                    Gdx.app.error(CreaturesDrawer.class.toString(), "Non so che sprite disegnare! Stato corrente: " + this.currentStates.get(creature.getID()));
                    break;
            }



            Vector2 position = creature.getAbsoluteTickPosition();
            if(position != null) {
                SpriteBatch batch = GameBatch.get();
                batch.begin();
                creature.getHealthBar().setX(position.x + 5);
                creature.getHealthBar().setY(position.y - 10);
                creature.getHealthBar().draw(batch, 1);
                batch.end();
            }
        }

        // Dato che la chiamata a render può provocare la rimozione dell'elemento stesso su cui è chiamata
        // (tramite lancio dell'evento AnimationEndedEvent, la sua cattura e la conseguente rimozione)
        // Utilizzo un tipo di lista appositamente implementato per effettuare rimozioni in un ciclo foreach.
        this.generalAnimations.begin();
        for (AbstractAnimation anim : this.generalAnimations) {
            anim.render(delta);
        }
        this.generalAnimations.end();
    }

    public void startWalk(final ICreature creature, GridPoint2 startPoint, GridPoint2 endPoint) {
        //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
        CreaturesDrawer.this.heroInput.setPaused(true);
        ArrayList<GridPoint2> path = new ArrayList<GridPoint2>(2);
        path.add(startPoint);
        path.add(endPoint);
        AnimationEndedListener listener = new AnimationEndedListener() {
            @Override
            public void ended(AnimationEndedEvent event, Object source) {
                //Poi renderizzo l'eroe in setStanding e riabilito l'input
                CreaturesDrawer.this.setStanding(creature.getID());
                CreaturesDrawer.this.heroInput.setPaused(false);
            }
        };
        this.walkAnimation(creature, path, listener);
    }

    public void startStrike(final ICreature attacker) {
        //Faccio l'animazione di camminata in base ai dati dell'evento e metto in pausa l'input utente
        CreaturesDrawer.this.heroInput.setPaused(true);
        AnimationEndedListener listener = new AnimationEndedListener() {
            @Override
            public void ended(AnimationEndedEvent event, Object source) {
                //Poi renderizzo l'eroe in setStanding e riabilito l'input
                CreaturesDrawer.this.setStanding(attacker.getID());
                CreaturesDrawer.this.heroInput.setPaused(false);
            }
        };
        this.strikeAnimation(attacker, listener);
    }

    public void startDamage(GridPoint2 position, int damage) {
        final AbstractAnimation currentAnimation = new AnimDamage(position, damage);
        currentAnimation.create();
        this.generalAnimations.add(currentAnimation);

        AnimationEndedListener listener = new AnimationEndedListener() {
            @Override
            public void ended(AnimationEndedEvent event, Object source) {
                generalAnimations.removeValue(currentAnimation, false);
            }
        };
        currentAnimation.addListener(listener);
    }

    private void walkAnimation(ICreature creature, ArrayList<GridPoint2> path, AnimationEndedListener listener) {
//        this.startWalk(creature, path.get(0), path.get(1));
        AbstractAnimation currentAnimation = new AnimWalk(creature.getClass(), path.get(0), path.get(1), 80.0f, creature.getFlipX());
        currentAnimation.create();
        currentAnimation.addListener(listener);
        this.currentAnimations.put(creature.getID(), currentAnimation);
        this.currentStates.put(creature.getID(), CreatureState.WALKING);
    }

    private void strikeAnimation(ICreature attacker, AnimationEndedListener listener) {
//        this.startWalk(creature, path.get(0), path.get(1));
        AbstractAnimation currentAnimation = new AnimStrike(attacker.getClass(), attacker.getPosition().getPosition(), attacker.getFlipX());
        currentAnimation.create();
        currentAnimation.addListener(listener);
        this.currentAnimations.put(attacker.getID(), currentAnimation);
        this.currentStates.put(attacker.getID(), CreatureState.WALKING);
    }

    private enum CreatureState {
        STANDING,
        WALKING
    }
}
