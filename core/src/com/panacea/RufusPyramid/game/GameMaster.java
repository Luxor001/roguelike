package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.ActionChosenListener;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadEvent;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadListener;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.input.HeroInputManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gestore delle turnazioni.
 * Ogni creatura ha una quantità di energia usata per effettuare le azioni.
 * Ogni azione ha un costo fisso, per variare la velocità delle agent
 * aumenta o diminuisce la quantità di energia guadagnata ad ogni loop.
 * del GameMaster.
 * Created by gio on 22/07/15.
 */
public class GameMaster{
    private static final int BASE_ENERGY_AT_EVERY_TURN = 200;
    private static final int MIN_ENERGY_TO_ACT = 1000;
    private final ActionChosenListener commonActionPerformedListener;
    private final CreatureDeadListener removeFromTurnWhenDeadListener;
    public static final int DEFAULT_ACTION_COST = 200;
    /**
     * Lista ordinata delle agent che prendono parte alla turnazione.
     */
    private transient final ArrayList<IAgent> agentsPlaying;    //FIXMEABSOLUTELY: togliere transient? Sembra andare
    private int currentAgentIndex;
    private boolean someoneIsPlaying;

    private HeroController heroController;
    public transient HeroInputManager heroInput;

    private transient ActionResult lastResult;    //viene impostato a null ad ogni cambio di turno
  //  private SaveLoadHelper sl;

    public GameMaster() {
        this.agentsPlaying = new ArrayList<IAgent>();
        this.commonActionPerformedListener = getActionChosenListener();
        this.removeFromTurnWhenDeadListener = getDeadListener();
        this.someoneIsPlaying = false;
        this.currentAgentIndex = -1;

        this.init();
    }

    private void init() {

        if(GameModel.get() != null) {
            for (ICreature creature : GameModel.get().getCreatures()) {
                if (creature instanceof Enemy) {
                    this.addAgent(creature);
                }
            }


            DefaultHero hero = GameModel.get().getHero();
            this.addAgent(hero);

            this.heroController = new HeroController(hero);
            this.heroInput = new HeroInputManager(heroController);
        }
    }

    public void addAgent(IAgent newAgent) {
        this.agentsPlaying.add(newAgent);
        if (newAgent instanceof ICreature) {
            ((ICreature)newAgent).addCreatureDeadListener(this.removeFromTurnWhenDeadListener);
        }
        newAgent.addActionChosenListener(this.commonActionPerformedListener);
        Gdx.app.log(GameMaster.class.toString(), "Aggiunto agente alla turnazione: " + newAgent);
    }

    public void addAllAgents(List<ICreature> agents) {
        for (IAgent agent : agents) {
            this.addAgent(agent);
        }
    }

    private void removeAgent(IAgent agent) {
        this.agentsPlaying.remove(agent);
        Gdx.app.log(GameMaster.class.toString(), "Rimosso agente dalla turnazione: " + agent);
    }


    public void step() {
        if (someoneIsPlaying || GameController.isGameEnded() || GameDrawer.get().getCreaturesDrawer().isPlayingAnimations())
            return;

//        this.sl.startLoad();
//        Gdx.app.log(GameMaster.class.toString(), "READ STRING: " + this.sl.loadObject(String.class));
//        Gdx.app.log(GameMaster.class.toString(), "READ STRING: " + this.sl.loadObject(String.class));
//        Stats hero = this.sl.loadObject(Stats.class);
//        Gdx.app.log(GameMaster.class.toString(), "Hero name: " + hero.getMaximumHP());
//        this.sl.stopLoad();

        someoneIsPlaying = true;


        if (thereIsSomeonePlaying()) {  //Se c'è qualche creatura che deve eseguire azioni (controllo anti-esplosione)
            //Al primo avvio this.currentAgentIndex == -1, viene inizializzata dalla prima chiamata a turnToNextAgent

            IAgent agentOnTurn;
            do {
                //Se la creatura corrente non ha più energia per continuare a fare azioni passa automaticamente il turno
                agentOnTurn = turnToNextAgent();
            } while (agentOnTurn.getEnergy() < MIN_ENERGY_TO_ACT);

            agentsPlaying.get(currentAgentIndex).chooseNextAction(lastResult);
            if (agentOnTurn instanceof DefaultHero) {
                this.heroInput.setPaused(false);
            }
        }
    }

    /* Turnazioni - vers. ad eventi */
    private ActionChosenListener getActionChosenListener() {
        PerformAndPayActionChosenListener listener = new PerformAndPayActionChosenListener();
        return listener;
    }

    /**
     * Restituisce un listener da aggiungere alle creature
     * che le rimuove dalla turnazione al momento della morte.
     * @return il listener
     */
    private CreatureDeadListener getDeadListener() {
        RemoveOnDeathListener listener = new RemoveOnDeathListener();
        return listener;
    }

    /**
     * Sposta il turno alla nuova creatura.
     * Le fornisce anche l'energia bonus di inizio turno.
     * @return la nuova creatura di turno
     */
    private IAgent turnToNextAgent() {
        this.lastResult = null;
        //Mi sposto alla creatura successiva, in ordine
        this.currentAgentIndex = (this.currentAgentIndex +1) % this.agentsPlaying.size();
        IAgent agentOnTurn = agentsPlaying.get(currentAgentIndex);

        //Controllo che la nuova creatura dei turno non dovrebbe essere già morta (e quindi rimmossa dalla turnazione)
        if (agentOnTurn instanceof ICreature && ((ICreature)agentOnTurn).getHPCurrent() <= 0) {
            Gdx.app.error(GameMaster.class.toString(), "ERRORE: passato il turno ad una creatura con hp <= 0: " + agentOnTurn);
        }


        //Alla nuova creatura viene assegnata l'energia di inizio turno
        this.addBonusEnergy(agentOnTurn);
        return agentOnTurn;
    }

    private void payForAction(IAgent payer, IAction action) {
        payer.setEnergy(payer.getEnergy() - action.getCost());
    }

    private void addBonusEnergy(IAgent agentOnTurn) {
        //TODO moltiplica per la velocità della creatura
        int turnBonusEnergy = GameMaster.BASE_ENERGY_AT_EVERY_TURN;
        agentOnTurn.setEnergy(agentOnTurn.getEnergy() + turnBonusEnergy);
    }

    private boolean thereIsSomeonePlaying() {
        return this.agentsPlaying != null && this.agentsPlaying.size() > 0;
    }

    private void checkEffects(AbstractCreature creature){ //remove or updates temporary effects
        Iterator<Effect> it = creature.getEffects().iterator();
        Effect effect;
        while(it.hasNext()) {
            effect = it.next();
            if(effect instanceof TemporaryEffect){
                TemporaryEffect tempEffect = (TemporaryEffect)effect;
                if(tempEffect.getTurns() == 0){
                    it.remove();
                }
                else {
                    tempEffect.setTurns(tempEffect.getTurns() - 1);
                }
            }
        }
    }

    public void checkEnemiesNearby(ICreature currCreature, DefaultHero hero){ //check if there is an enemy near hte player bounds, so we can active the quick attack button

        ICreature firstTarget = null;
            GridPoint2 absoluteDist = Utilities.absoluteDistance(currCreature.getPosition().getPosition(),  hero.getPosition().getPosition());
            if(absoluteDist.x == 1 || absoluteDist.y == 1) {
                Utilities.Directions[] directions = {Utilities.Directions.NORTH, Utilities.Directions.EAST, Utilities.Directions.SOUTH, Utilities.Directions.WEST};
                for (Utilities.Directions dir : directions) {
                    GridPoint2 currPos = Utilities.Directions.adjCoords(hero.getPosition().getPosition(), dir);
                    if (currCreature.getPosition().getPosition().x == currPos.x && currCreature.getPosition().getPosition().y == currPos.y) {
                        firstTarget = currCreature;
                        break;
                    }
                }
        }
        hero.setFirstTarget(firstTarget);
    }
    public void checkEnemiesNearby(){ //check if there is an enemy near hte player bounds, so we can active the quick attack button
        ICreature firstTarget = null;
        DefaultHero hero = GameModel.get().getHero();
        for (ICreature currCreature : GameModel.get().getCreatures()) {
            GridPoint2 absoluteDist = Utilities.absoluteDistance(currCreature.getPosition().getPosition(),  hero.getPosition().getPosition());
            if(absoluteDist.x == 1 || absoluteDist.y == 1) {
                Utilities.Directions[] directions = {Utilities.Directions.NORTH, Utilities.Directions.EAST, Utilities.Directions.SOUTH, Utilities.Directions.WEST};
                for (Utilities.Directions dir : directions) {
                    GridPoint2 currPos = Utilities.Directions.adjCoords(hero.getPosition().getPosition(), dir);
                    if (currCreature.getPosition().getPosition().x == currPos.x && currCreature.getPosition().getPosition().y == currPos.y) {
                        firstTarget = currCreature;
                        break;
                    }
                }
            }
        }
        hero.setFirstTarget(firstTarget);
    }

    public void disposeGame() {
        //TODO
//        this.currentAgentIndex = -100;
        this.agentsPlaying.clear();
    }

//    public HeroController getHeroController() {
//        return this.heroController;
//    }

    private static class PerformAndPayActionChosenListener implements ActionChosenListener {

        public PerformAndPayActionChosenListener() {}

        public void performed(ActionChosenEvent event, IAgent source) {
            /* Controllo che la creatura che richiede di effettuare l'azione sia di turno. */
            GameMaster gm = GameController.getGm();
            if (!source.equals(gm.agentsPlaying.get(gm.currentAgentIndex))
                    || (source instanceof ICreature && ((ICreature)source).getHPCurrent() <= 0)) {
                Gdx.app.error(
                        this.getClass().getName(),
                        "ERRORE! Un agent non autorizzato ha appena cercato di eseguire un'azione: " +
                                "CurrentAgentIndex: " + gm.currentAgentIndex + "\n" +
                                "wtfAgentIndex: " + gm.agentsPlaying.indexOf(source) + "\n" +
                                "wtfAgentToString: " + source.toString() + "\n"
                );
                return;
            }

            if (source instanceof DefaultHero) {
                gm.heroInput.setPaused(true);
            }

            if(source instanceof AbstractCreature)
                gm.checkEffects((AbstractCreature) source);

            IAgent agentOnTurn = source;
            IAction actionChosen = event.getChosenAction();

            //TODO il risultato (success) deve essere passato tramite l'evento o
            //TODO deve essere il GameMaster lanciare IAction.perform() ?
            //Eseguo l'azione scelta dalla creatura
            ActionResult result = actionChosen.perform();

            if (result.hasSuccess()) {
                //La creatura "paga" il prezzo per l'azione effettuata
                gm.payForAction(agentOnTurn, actionChosen);
            }

            //Fine del ciclo di controllo
            gm.lastResult = result;
            gm.someoneIsPlaying = false;

            if (!GameController.isGameEnded()) {
                //TODO questa funzione dovrebbe essere lanciata solo subito prima di passare il turno all'eroe
                //altrimenti viene lanciata ad ogni turno di una creatura!
                gm.checkEnemiesNearby();
            }
        }
    }

    private static class RemoveOnDeathListener implements CreatureDeadListener {

        @Override
        public void changed(CreatureDeadEvent event, Object source) {
            if (source instanceof IAgent) {
                GameController.getGm().removeAgent((IAgent) source);
            }
        }
    }
}
