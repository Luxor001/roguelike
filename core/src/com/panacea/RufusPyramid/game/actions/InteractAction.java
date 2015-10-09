package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Diary;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.items.ChestItem;
import com.panacea.RufusPyramid.game.items.GoldItem;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.SoundsProvider;

import java.util.ArrayList;

/**
 * Created by Lux on 13/09/2015.
 */
public class InteractAction implements IAction {

    private Item itemToInteract;
    private ICreature creature;
    private Sound goldPickSound;

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

        Diary diario= GameModel.get().getDiary();
        DefaultHero hero = GameModel.get().getHero();
        if(UsableItem.class.isAssignableFrom(itemToInteract.getClass())){
            UsableItem convItem = (UsableItem)itemToInteract;
            items.remove(convItem);
            hero.fireActionChosenEvent(new MoveAction(hero, Utilities.getDirectionFromCoords(hero.getPosition().getPosition(), convItem.getPosition())));
   //         hero.setPosition(GameModel.get().getCurrentMap().getMapContainer().getTile(convItem.getPosition()));
            hero.addEffects(((UsableItem) itemToInteract).getEffects());

            diario.addLine("Hai raccolto " + convItem.getItemName() + "!");
     //       diario.addLine("+"+attackBonus+" Attack!");
        }
        if(itemToInteract instanceof GoldItem){
            GoldItem convItem = (GoldItem)itemToInteract;
            hero.fireActionChosenEvent(new MoveAction(hero, Utilities.getDirectionFromCoords(hero.getPosition().getPosition(), convItem.getPosition())));
            int goldAmount = convItem.getGoldAmount();
            hero.addGold(goldAmount);
            GameDrawer.get().getCreaturesDrawer().displayInfo(hero.getPosition().getPosition(), "+" + goldAmount, Color.YELLOW);
            int randSound = Utilities.randInt(0,SoundsProvider.Sounds.GOLD_PICKUP.getValue()-1);
            goldPickSound = SoundsProvider.get().getSound(SoundsProvider.Sounds.GOLD_PICKUP)[randSound];
            goldPickSound.play(1f);
            items.remove(convItem);
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
