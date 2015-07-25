package com.panacea.RufusPyramid.game.actions;

/**
 * Interfaccia da implementare per poter eseguire una IAction.
 * Created by gio on 23/07/15.
 */
public interface IAgent {
    /**
     * Aggiunge un listener per gli eventi ActionPerformedEvent.
     * Sarà sempre usato da GameMaster per gestire le turnazioni se l'oggetto ne deve fare parte.
     * @param listener il listener da aggiungere
     */
    void addActionChosenListener(ActionPerformedListener listener);

    /**
     * Spara un evento di tipo ActionPerformedEvent.
     */
    void fireActionChosenEvent(ActionPerformedEvent event, IAgent source);

    /**
     * Esegue l'azione successiva. L'oggetto dovrà fare le proprie valutazioni,
     * Scegliere la IAction più adeguata e poi eseguirla. Al termine deve essere lanciato un
     * fireActionChosenEvent() (anche se l'azione non è andata a buon fine).
     */
    void chooseNextAction(ActionResult resultPreviuosAction);

    /**
     * Ritorna l'energia a disposizione dell'agente, usata per eseguire azioni.
     * @return l'energia corrente dell'agente.
     */
    int getEnergy();

    /**
     * Imposta l'energia a disposizione dell'agente, usata per eseguire azioni.
     * @param currentEnergy l'energia corrente dell'agente
     */
    void setEnergy(int currentEnergy);
}
