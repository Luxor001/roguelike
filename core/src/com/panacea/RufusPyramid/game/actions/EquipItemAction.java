package com.panacea.RufusPyramid.game.actions;

import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.items.usableItems.Equippable;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;

/**
 * Created by gioele.masini on 28/10/2015.
 */
public class EquipItemAction implements IAction {

    private final Equippable itemToEquip;
    private final ICreature creature;

    public EquipItemAction(ICreature creature, Equippable item/*, equipPosition*/) {
        this.creature = creature;
        this.itemToEquip = item;
    }

    @Override
    public ActionResult perform() {
//        if (creature.getEquipment().getStorage().contains(itemToEquip) || equipaggiato        ) {
            Equippable itemThatWasEquipped = creature.getEquipment().setEquipItem(itemToEquip);
            creature.getEquipment().getStorage().remove(itemToEquip);
            if (itemThatWasEquipped != null) {
                //FIXME c'è qualcosa che non va! Unchecked cast, anche se per ora funziona sempre...
                creature.getEquipment().addItemToStorage((UsableItem)itemThatWasEquipped);
            }
//        }
        return new ActionResult(true);
    }

    @Override
    public int getCost() {
        return 0;
    }
}
