package com.panacea.RufusPyramid.game.actions;

import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.items.ChestItem;
import com.panacea.RufusPyramid.game.items.Item;

/**
 * Created by Lux on 13/09/2015.
 */
public class InteractAction implements IAction {

    private Item itemToInteract;
    public InteractAction(Item itemToInteract){
        this.itemToInteract = itemToInteract;
    }

    @Override
    public int getCost() {
        return GameMaster.DEFAULT_ACTION_COST;
    }

    @Override
    public ActionResult perform() {
        if(ChestItem.class.isInstance(itemToInteract)){ //se è un chest..
            //destroy il chest
            //mostra al prox turno
        }
        return null;
    }
}
