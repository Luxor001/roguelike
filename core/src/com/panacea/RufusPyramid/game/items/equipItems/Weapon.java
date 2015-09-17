package com.panacea.RufusPyramid.game.items.equipItems;

import com.panacea.RufusPyramid.game.items.equipItems.EquipmentItem;

/**
 * Created by Lux on 17/09/2015.
 */
public class Weapon extends EquipmentItem {

    public static enum WeaponType{ /*FIXME: si dovrebbe usare questo ma è un casino:http://blog.pengyifan.com/how-to-extend-enum-in-java/*/
        SWORD,
        AXE,
        DAGGER,
        PALETTE,
        PICK
    }

    private WeaponType type;
    public Weapon(WeaponType type){
        super(type.name());
        this.type = type;
    }

    public WeaponType getType(){
        return type;
    }
}
