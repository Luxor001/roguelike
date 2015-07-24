package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.actions.ActionPerformedEvent;
import com.panacea.RufusPyramid.game.actions.ActionPerformedListener;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.IAgent;

import java.util.ArrayList;

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
    private final ActionPerformedListener commonActionPerformedListener;

    /**
     * Lista ordinata delle agent che prendono parte alla turnazione.
     */
    private final ArrayList<IAgent> agentsPlaying;
    private int currentAgent;

    public GameMaster() {
        this.agentsPlaying = new ArrayList<IAgent>();
        this.commonActionPerformedListener = getActionProcessedListener();
    }

    public void addAgent(IAgent newAgent) {
        this.agentsPlaying.add(newAgent);
        newAgent.addActionProcessedListener(this.commonActionPerformedListener);
    }

    /**
     * Metodo da utilizzare all'avvio del gioco per far iniziare
     * i turni. Il GameMaster avvia tutto il sistema di turnazione
     * dicendo alla prima creatura in lista di eseguire un'azione.
     * Questo metodo ritornerà quasi immediatamente in quanto tutta
     * la gestione è ad eventi.
     */
    public void startTurns() {
        this.currentAgent = -1;
        turnToNextAgent().performNextAction();
    }

    /* Turnazioni - vers. ad eventi */
    private ActionPerformedListener getActionProcessedListener() {
        return new ActionPerformedListener() {
            public void performed(ActionPerformedEvent event, IAgent source) {
                /* Controllo che la creatura che ha effettuato l'azione sia di turno. */
                if (!source.equals(agentsPlaying.get(currentAgent))) {
                    Gdx.app.error(
                            this.getClass().getName(),
                            "ERRORE! Una agent non autorizzato ha appena cercato di eseguire un'azione: " +
                                    "CurrentAgentIndex: " + currentAgent + "\n" +
                                    "wtfAgentIndex: " + agentsPlaying.indexOf(source) + "\n" +
                                    "wtfAgentToString: " + source.toString() + "\n"
                    );
                    return;
                }

                IAgent agentOnTurn = source;
                IAction actionPerformed = event.getPerformedAction();

                //TODO il risultato (success) deve essere passato tramite l'evento o
                //TODO deve essere il GameMaster lanciare IAction.perform() ?
                boolean success = event.getActionResult();

                if (success) {
                    //La creatura "paga" il prezzo per l'azione effettuata
                    payForAction(agentOnTurn, actionPerformed);
                }

                if (agentOnTurn.getEnergy() < MIN_ENERGY_TO_ACT) {
                    //Se la creatura corrente non ha più energia per continuare a fare azioni passa automaticamente il turno
                    turnToNextAgent();
                }

                //Fine del ciclo di controllo
                //La creatura che deve ora deve eseguire un'azione è segnata da this.currentAgent
                agentsPlaying.get(currentAgent).performNextAction();
            }
        };
    }

    /**
     * Sposta il turno alla nuova creatura.
     * Le fornisce anche l'energia bonus di inizio turno.
     * @return la nuova creatura di turno
     */
    private IAgent turnToNextAgent() {
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
}
