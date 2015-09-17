package com.panacea.RufusPyramid.game.items.equipItems;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.items.Item;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class EquipmentItem extends Item {


    public EquipmentItem(String type){
        super(type);
    }

    public void detach(){

    }

    public void equip(){
        //TODO: equipaggiamento dell'oggetto
    }
}
