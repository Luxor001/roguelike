package com.panacea.RufusPyramid.game.items.usableItems;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class UsableItem extends Item {

    private String itemName; //item name that will be shown in the diary.
    private ArrayList<Effect> effects; //effects of the item on the creature.

    public UsableItem(String type, ArrayList<Effect> effects, String itemName){
        super(type);
        this.itemName = itemName;
        this.effects = effects;
    }

    public String getItemName(){
        return itemName;
    }
    public List<Effect> getEffects(){
        return effects;
    }
}
