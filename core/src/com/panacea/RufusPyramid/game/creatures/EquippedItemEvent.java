package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.items.usableItems.Equippable;

import java.util.List;

/**
 * Created by gioele.masini on 29/10/2015.
 */
public class EquippedItemEvent {
    private final Equippable equippedItem;
    private final Backpack.EquippableType type;
    private final List<Equippable> oldEquippedItems;

    public EquippedItemEvent(Equippable equippedItem, List<Equippable> oldEquippedItems, Backpack.EquippableType type) {
        this.equippedItem = equippedItem;
        this.oldEquippedItems = oldEquippedItems;
        this.type = type;
    }

    public Equippable getEquippedItem() {
        return this.equippedItem;
    }

    public Backpack.EquippableType getEquippedType() {
        return this.type;
    }

    public List<Equippable> getUnequippedItem() {
        return this.oldEquippedItems;
    }
}
