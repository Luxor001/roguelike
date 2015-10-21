package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 17/09/2015.
 */
public class Weapon extends UsableItem {

    public enum WeaponType{ /*FIXME: si dovrebbe usare questo ma è un casino:http://blog.pengyifan.com/how-to-extend-enum-in-java/*/
        SWORD,
        AXE,
        DAGGER,
        PALETTE,
        PICK
    }

    private WeaponType type;

    public Weapon(WeaponType type, List<Effect> effects, String itemName){
        super(type.name(), effects, itemName);
        this.type = type;
    }

    public WeaponType getType(){
        return type;
    }

}
