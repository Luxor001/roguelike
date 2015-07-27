package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameDrawer;

/**
 * Created by gio on 26/07/15.
 */
public class AttackAction implements IAction {


    private final ICreature attacked;
    private final ICreature attacker;

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

    private boolean attack() {
        // - Controllo che l'attacco sia possibile
        if (!this.canAttack(attacker, attacked)) {
            return false;
        }

        // - Effettuo l'attacco
        int damage = this.getDamage(attacker.getAttackValue(), attacked.getDefenceValue());
        this.attacked.setHPCurrent(this.attacked.getHPCurrent() - damage);

        Gdx.app.log("", "Enemy hp: " + this.attacked.getHPCurrent());

        GameDrawer.get().getCreaturesDrawer().startDamage(this.attacked.getPosition().getPosition(), damage);
        return true;
    }

    @Override
    public int getCost() {
        return 200;
    }

    private boolean canAttack(ICreature attacker, ICreature attacked) {
        GridPoint2 pos1 = attacker.getPosition().getPosition(),
                pos2 = attacked.getPosition().getPosition();

        if (Math.abs(pos1.x - pos2.x) > 32 || Math.abs(pos1.y - pos2.y) > 32) {
            //Se le due creature sono distanti più di un quadretto non è possibile effettuare l'attacco.
            return false;
        }
        return true;
    }

    private int getDamage(double attackerAttack, double attackedDefence) {
        int dmg = Math.round(Math.round(attackerAttack - attackedDefence));
        return dmg < 1 ? 1 : dmg;
    }
}
