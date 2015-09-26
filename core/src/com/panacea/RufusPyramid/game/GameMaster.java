package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.ActionChosenListener;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gestore delle turnazioni.
 * Ogni creatura ha una quantità di energia usata per effettuare le azioni.
 * Ogni azione ha un costo fisso, per variare la velocità delle agent
 * aumenta o diminuisce la quantità di energia guadagnata ad ogni loop.
 * del GameMaster.
 * Created by gio on 22/07/15.
 */
public class GameMaster {
    private static final int BASE_ENERGY_AT_EVERY_TURN = 200;
    private static final int MIN_ENERGY_TO_ACT = 1000;
    private final ActionChosenListener commonActionPerformedListener;
    public static final int DEFAULT_ACTION_COST = 200;
    /**
     * Lista ordinata delle agent che prendono parte alla turnazione.
     */
    private final ArrayList<IAgent> agentsPlaying;
    private int currentAgent;
    private boolean someoneIsPlaying;

    private ActionResult lastResult;    //viene impostato a null ad ogni cambio di turno

    public GameMaster() {
        this.agentsPlaying = new ArrayList<IAgent>();
        this.commonActionPerformedListener = getActionChosenListener();
        this.someoneIsPlaying = false;
        this.currentAgent = -1;
    }

    public void addAgent(IAgent newAgent) {
        this.agentsPlaying.add(newAgent);
        newAgent.addActionChosenListener(this.commonActionPerformedListener);
        Gdx.app.log("GM", "Aggiunto agente: " + newAgent);
    }

    public void addAllAgents(Collection<IAgent> agents) {
        for (IAgent agent : agents) {
            this.addAgent(agent);
        }
    }

    public void step() {
        if (someoneIsPlaying)   return;
        someoneIsPlaying = true;



        if (thereIsSomeonePlaying()) {  //Se c'è qualche creatura che deve eseguire azioni (controllo anti-esplosione)
            //Al primo avvio this.currentAgent == -1, viene inizializzata dalla prima chiamata a turnToNextAgent

            IAgent agentOnTurn;
            do {
                //Se la creatura corrente non ha più energia per continuare a fare azioni passa automaticamente il turno
                agentOnTurn = turnToNextAgent();
            } while (agentOnTurn.getEnergy() < MIN_ENERGY_TO_ACT);

            agentsPlaying.get(currentAgent).chooseNextAction(lastResult);
        }
    }

    /* Turnazioni - vers. ad eventi */
    private ActionChosenListener getActionChosenListener() {
        return new ActionChosenListener() {
            public void performed(ActionChosenEvent event, IAgent source) {
                /* Controllo che la creatura che richiede di effettuare l'azione sia di turno. */
                if (!source.equals(agentsPlaying.get(currentAgent))) {
                    Gdx.app.error(
                            this.getClass().getName(),
                            "ERRORE! Un agent non autorizzato ha appena cercato di eseguire un'azione: " +
                                    "CurrentAgentIndex: " + currentAgent + "\n" +
                                    "wtfAgentIndex: " + agentsPlaying.indexOf(source) + "\n" +
                                    "wtfAgentToString: " + source.toString() + "\n"
                    );
                    return;
                }

                if(source instanceof AbstractCreature)
                    checkEffects((AbstractCreature)source);

                IAgent agentOnTurn = source;
                IAction actionChosen = event.getChosenAction();

                //TODO il risultato (success) deve essere passato tramite l'evento o
                //TODO deve essere il GameMaster lanciare IAction.perform() ?
                //Eseguo l'azione scelta dalla creatura
                ActionResult result = actionChosen.perform();

                if (result.hasSuccess()) {
                    //La creatura "paga" il prezzo per l'azione effettuata
                    payForAction(agentOnTurn, actionChosen);
                }

                //Fine del ciclo di controllo
                GameMaster.this.lastResult = result;
                GameMaster.this.someoneIsPlaying = false;
            }
        };
    }

    /**
     * Sposta il turno alla nuova creatura.
     * Le fornisce anche l'energia bonus di inizio turno.
     * @return la nuova creatura di turno
     */
    private IAgent turnToNextAgent() {
        this.lastResult = null;
        //Mi sposto alla creatura successiva, in ordine
        this.currentAgent = (this.currentAgent+1) % this.agentsPlaying.size();
        IAgent agentOnTurn = agentsPlaying.get(currentAgent);

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
         for(Effect effect: creature.getEffects()){
             if(effect instanceof TemporaryEffect){
                 TemporaryEffect tempEffect = (TemporaryEffect)effect;
                 if(tempEffect.getTurns() == 0){
                     creature.getEffects().remove(effect);
                 }
                 else
                    tempEffect.setTurns(tempEffect.getTurns()-1);
             }
         }
    }
}
