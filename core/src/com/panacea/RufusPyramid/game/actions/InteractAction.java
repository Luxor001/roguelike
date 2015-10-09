package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Diary;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.items.ChestItem;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;

import java.util.ArrayList;

/**
 * Created by Lux on 13/09/2015.
 */
public class InteractAction implements IAction {

    private Item itemToInteract;
    private ICreature creature;

    public InteractAction(Item itemToInteract, ICreature creature){
        this.itemToInteract = itemToInteract;
        this.creature=creature;
    }

    @Override
    public int getCost() {
        return GameMaster.DEFAULT_ACTION_COST;
    }

    @Override
    public ActionResult perform() {
        boolean success = interact();
        return new ActionResult(success);
    }

    public boolean interact(){
        if(!canInteract(itemToInteract,creature)) {
            return false;
        }

        ArrayList<Item> items = GameModel.get().getItems();
        if(itemToInteract instanceof ChestItem){

            Item itemStored = ((ChestItem) itemToInteract).getItemStored();
            items.remove(itemToInteract);
            itemStored.setPosition(itemToInteract.getPosition());
            items.add(itemStored);
        }

        if(UsableItem.class.isAssignableFrom(itemToInteract.getClass())){
            Diary diario= GameModel.get().getDiary();
            UsableItem convItem = (UsableItem)itemToInteract;
            items.remove(convItem);
            DefaultHero hero = GameModel.get().getHero();
            ActionResult result = new MoveAction(hero, Utilities.getDirectionFromCoords(hero.getPosition().getPosition(), convItem.getPosition())).perform();
            if (result.hasSuccess()) {
                hero.addEffects(((UsableItem) itemToInteract).getEffects());

                diario.addLine("Hai raccolto " + convItem.getItemName() + "!");
            }
        }

        return true;
    }

    public boolean canInteract(Item itemToInteract, ICreature creature){

        GridPoint2 pos1 = itemToInteract.getPosition(),
        pos2 = creature.getPosition().getPosition();

        if(Math.abs(pos1.x - pos2.x) > 1 || Math.abs(pos1.y - pos2.y) > 1)
            return false;
        else
            return true;
    }
}
