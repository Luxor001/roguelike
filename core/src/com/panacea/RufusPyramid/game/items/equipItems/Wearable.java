package com.panacea.RufusPyramid.game.items.equipItems;

/**
 * Created by Lux on 17/09/2015.
 */
public class Wearable extends EquipmentItem{
    public static enum WearableType{
        ARMOR,
        BOOTS,
        HELM,
        SHIELD,
        ARMOR2
    }

    private WearableType type;
    public Wearable(WearableType type){
        super(type.name());
        this.type = type;
    }

    public WearableType getType(){
        return type;
    }
}
