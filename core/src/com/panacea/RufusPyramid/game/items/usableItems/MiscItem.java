package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 13/09/2015.
 */
public class MiscItem extends UsableItem{

    public enum MiscItemType implements IItemType{
        INVINCIBILITY_POTION,
        HEALTH_POTION,
        RAW_MEAT
    }
    private MiscItem(){
        super();
    }

    private MiscItemType type;
    public MiscItem(MiscItemType type, List<Effect> effects, String itemName){
        super(type, effects, itemName);
        this.type=type;
    }
    public MiscItem(MiscItemType type, Effect effect, String itemName){
        super(type, effect, itemName);
        this.type=type;
    }
}
