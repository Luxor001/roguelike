package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 22/07/15.
 */
public interface IAction {
    /**
     * Esegue l'azione.
     * @return true se è stata completata con successo, false se non è stato possibile eseguirla.
     */
    ActionResult perform();

    int getCost();
}
