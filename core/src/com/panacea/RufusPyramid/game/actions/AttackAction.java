package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameDrawer;

/**
 * Created by gio on 26/07/15.
 */
public class AttackAction implements IAction {


    private final ICreature attacked;
    private final ICreature attacker;

    public static final double[] attackMultipliers = new double[]      { 0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f}; //probabilità //TODO: da mettere su db!
    public static final double[] attackMultipliersProb = new double[] { 0.5, 0.1, 0.5, 0.2, 0.1, 0.5 };
    public AttackAction(ICreature attacker, ICreature attacked) {
        this.attacker = attacker;
        this.attacked = attacked;
    }

    @Override
    public ActionResult perform() {
        Gdx.app.log("", "Attacking!");

        boolean success = this.attack();
        return new ActionResult(success);
    }
    private AttackAction(){
        attacker = null;
        attacked = null;
    }

    private boolean attack() {
        // - Controllo che l'attacco sia possibile
        if (!this.canAttack(attacker, attacked)) {
            return false;
        }

        // - Effettuo l'attacco
        if (attacked.getPosition().getPosition().x < attacker.getPosition().getPosition().x) //mi sto muovendo a sinistra, l'animazione deve essere invertita..
                attacker.setFlipX(true);
            else
                attacker.setFlipX(false);

        GameDrawer.get().getCreaturesDrawer().startStrike(attacker);
        int damage = this.getDamage(attacker.getCurrentStats().getAttack(), attacked.getCurrentStats().getDefence());

        if (attacker.equals(GameModel.get().getHero())) {
            GameModel.get().getDiary().addLine("Hai attaccato " + attacked.getName() + "!");
        } else {
            GameModel.get().getDiary().addLine(attacker.getName() + " ti ha attaccato!");
        }
        GameDrawer.get().getCreaturesDrawer().startDamage(this.attacked.getPosition().getPosition(), damage);
        this.attacked.setHPCurrent(this.attacked.getHPCurrent() - damage);
        return true;
    }

    @Override
    public int getCost() {
        return 200;
    }

    private boolean canAttack(ICreature attacker, ICreature attacked) {
        GridPoint2 pos1 = attacker.getPosition().getPosition(),
        pos2 = attacked.getPosition().getPosition();

        if (Math.abs(pos1.x - pos2.x) > Utilities.DEFAULT_BLOCK_WIDTH
                || Math.abs(pos1.y - pos2.y) > Utilities.DEFAULT_BLOCK_HEIGHT) {
            //Se le due creature sono distanti più di un quadretto non è possibile effettuare l'attacco.
            return false;
        }

        return true;
    }

    private int getDamage(double attackerAttack, double attackedDefence) {
        double dmg = (attackerAttack - attackedDefence);
        double multiplier = Utilities.randWithProb(AttackAction.attackMultipliers,AttackAction.attackMultipliersProb);
        dmg *=multiplier;
        return (int)( dmg < 1 ? 1 : Math.round(dmg));
    }
}
