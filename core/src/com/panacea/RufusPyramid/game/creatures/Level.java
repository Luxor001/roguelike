package com.panacea.RufusPyramid.game.creatures;

/**
 * Created by gio on 09/07/15.
 */
public class Level {

    private int currentLevel;
    private long currentExperience;


    public Level(int currentLevel) {
        this.currentLevel = (currentLevel > 0 ? currentLevel : 1);
    }

    public long getExperience() {
        return this.currentExperience;
    }

    public void addExperience(long newExperience) {
        this.currentExperience += newExperience;

    }

    public void incrementLevelIfNecessary() {

    }

    protected void getLevelFromExperience() {

    }

//TODO
//    public double getIncrementedAttack(double attackValue) {
//        return attackValue + this.incRateoAttack;
//    }
//
//    public double getIncrementedDefence(double defenceValue) {
//        return defenceValue + this.incRateoDefence;
//    }
//
//    public double getIncrementedSpeed(double speedValue) {
//        return speedValue + this.incRateoSpeed;
//    }

    /**
     * Classe di configurazione con i valori di incremento delle statistiche e i metodi
     * per il calcolo delle nuove statistiche all'aumento di livello.
     */
    protected class LevelerConfig {
        /**
         *  Incremento delle statistiche all'aumentare di livello
         */
        public double incRateoAttack, incRateoDefence, incRateoSpeed;
//        private

        public LevelerConfig(double attackRateo, double defenceRateo, double speedRateo) {
            this.incRateoAttack = attackRateo;
            this.incRateoDefence = defenceRateo;
            this.incRateoSpeed = speedRateo;
        }

        public LevelerConfig() {
            this(1.0, 1.0, 1.0);
        }


    }

    protected class ExperienceForLevel {
        private static final long BASE = 1000;

        /**
         * Dato un livello calcola i punti esperienza necessari per ottenerlo.
         * "variationPercent" è un valore percentuale (1.0 == 100%) da usare per aumentare o diminuire
         * automaticamente i punti esperienza calcolati (per salire di livello più o meno velocemente).
         *
         * @param level il livello per il quale si vogliono sapere i punti esperienza
         * @param variationPercent variazione percentuale per aumentare o diminuire i punti esperienza di default (nullabile)
         * @return esperienza necessaria per salire al livello specificato in input.
         */
        public long calculate(int level, Double variationPercent) {
            //TODO come foglio excel su drive
//            variationPercent = (variationPercent == null ? 1.0 : variationPercent);
//            return round(round((BASE * level + BASE * sommaPrimiNNumeriNaturali(level)) * variationPercent));
            return 0;
        }

        private int sommaPrimiNNumeriNaturali(int n) {
            return (n+1)*n/2;
        }
    }
}
