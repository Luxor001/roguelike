package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class UsableItem extends Item {

    private String itemName; //item name that will be shown in the diary.
    private List<Effect> effects; //effects of the item on the creature.

    public UsableItem(IItemType type, List<Effect> effects, String itemName){
        super(type);
        this.itemName = itemName;
        this.effects = effects;
    }

    public UsableItem(IItemType type, Effect effect, String itemName){
        super(type);
        this.itemName = itemName;
        this.effects = new ArrayList<Effect>();
        this.effects.add(effect);
    }

    public String getItemName(){
        return itemName;
    }
    public List<Effect> getEffects(){
        return effects;
    }
}
