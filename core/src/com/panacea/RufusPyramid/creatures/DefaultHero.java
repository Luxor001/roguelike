package com.panacea.RufusPyramid.creatures;

/**
 * Created by gio on 09/07/15.
 */
public class DefaultHero extends AbstractCreature {

    private static final String DESCRIPTION = "The hero of the game.";
    private static final int MAX_HP = 25;
    private static final double ATTACK = 5;
    private static final double DEFENCE = 5;
    private static final double SPEED = 5;


//    TODO aggiungere il livello con l'oggetto Level
    public DefaultHero(String name/*, LevelerConfig  */) {
        super(name, DESCRIPTION, MAX_HP, ATTACK, DEFENCE, SPEED);
    };


    /**
     * Classe di configurazione con i valori di incremento delle statistiche e i metodi
     * per il calcolo delle nuove statistiche all'aumento di livello.
     */

}
