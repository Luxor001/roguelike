package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.Gdx;
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
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.MusicPlayer;

import java.util.List;

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

        List<Item> items = GameModel.get().getItems();
        if(itemToInteract instanceof ChestItem){

            Item itemStored = ((ChestItem) itemToInteract).getItemStored();
            items.remove(itemToInteract);
            itemStored.setPosition(itemToInteract.getPosition());
            items.add(itemStored);
        }

        Diary diario= GameModel.get().getDiary();
        DefaultHero hero = GameModel.get().getHero();
        if (UsableItem.class.isAssignableFrom(itemToInteract.getClass())) {
            UsableItem convItem = (UsableItem)itemToInteract;
            ActionResult result = new MoveAction(hero, Utilities.getDirectionFromCoords(hero.getPosition().getPosition(), convItem.getPosition())).perform();
            if(result.hasSuccess()){
                String diaryText = "";
                if (convItem instanceof MiscItem || convItem instanceof Wearable || convItem instanceof Weapon) {
                    //TODO anche se è una Weapon o un Wearable dovrebbe essere aggiunto all'inventario..?
                    boolean success = hero.getEquipment().addItemToStorage(convItem);
                    if (success) {
                        items.remove(convItem);
                        diaryText = "Hai raccolto " + convItem.getItemName() + "!";
                    } else {
                        diaryText = "Non hai più spazio nell'inventario!";
                    }
                } else {
                    Gdx.app.error(InteractAction.class.toString(), "Non so cosa fare con l'oggetto: " + convItem);
                    hero.addEffects(((UsableItem) itemToInteract).getEffects());
                    items.remove(convItem);
                }

                diario.addLine(diaryText);
            }
        }
        if(itemToInteract instanceof GoldItem){
            GoldItem convItem = (GoldItem)itemToInteract;
            ActionResult result = new MoveAction(hero, Utilities.getDirectionFromCoords(hero.getPosition().getPosition(), convItem.getPosition())).perform();
            if(result.hasSuccess()){
                int goldAmount = convItem.getGoldAmount();
                hero.addGold(goldAmount);
                GameDrawer.get().getCreaturesDrawer().displayInfo(hero.getPosition().getPosition(), "+" + goldAmount, Color.YELLOW);
                MusicPlayer.playSound(MusicPlayer.SoundType.GOLD_PICK, this.creature.getCreatureType(), this.creature.getID());
            }        
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
