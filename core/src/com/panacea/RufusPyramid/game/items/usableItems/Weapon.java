package com.panacea.RufusPyramid.game.items.usableItems;

import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.creatures.Backpack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lux on 17/09/2015.
 */
public class Weapon extends UsableItem implements Equippable {

    public enum WeaponType implements IItemType{
        //Non dovrebbe essercene più bisogno, usando un'interfaccia, ma...
        /*FIXME: si dovrebbe usare questo ma è un casino:http://blog.pengyifan.com/how-to-extend-enum-in-java/*/
        SWORD,
        AXE,
        DAGGER,
        PALETTE,
        PICK
    }

    private WeaponType type;

    private Weapon() {};

    public Weapon(WeaponType type, List<Effect> effects, String itemName){
        super(type, effects, itemName);
        this.type = type;
    }

    public WeaponType getType(){
        return type;
    }

}
