package com.panacea.RufusPyramid.game.actions;

import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;

/**
 * Created by gioele.masini on 29/10/2015.
 */
public class ActiveItemAction implements IAction {

    private final UsableItem item;
    private final ICreature creature;

    public ActiveItemAction(UsableItem item, ICreature creatureThatActived) {
        this.item = item;
        this.creature = creatureThatActived;
    }

    @Override
    public ActionResult perform() {
        this.creature.addEffects(item.getEffects());
        this.creature.getEquipment().getStorage().remove(item);

        ActionResult result = new ActionResult(true);
        return result;
    }

    @Override
    public int getCost() {
        return GameMaster.DEFAULT_ACTION_COST;
    }
}
