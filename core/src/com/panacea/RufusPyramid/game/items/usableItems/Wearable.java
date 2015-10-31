package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 17/09/2015.
 */
public class Wearable extends UsableItem implements Equippable {
    public static enum WearableType implements IItemType{
        ARMOR,
        BOOTS,
        HELM,
        SHIELD,
        ARMOR2
    }

    private WearableType type;

    private Wearable() {};

    public Wearable(WearableType type, List<Effect> effects, String itemName){
        super(type, effects, itemName);
        this.type = type;
    }

    public WearableType getType(){
        return type;
    }
}
