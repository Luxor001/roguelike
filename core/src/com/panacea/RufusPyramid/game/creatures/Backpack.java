package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.items.IItem;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Classe di gestione degli oggetti equipaggiati e trasportati.
 * Contiene un enum pubblico con i tipi di oggetti equipaggiabili.
 */
public class Backpack {
    private static int MAX_STORAGE_CAPACITY = 24;

    private List<UsableItem> storage;
    private LinkedHashMap<Backpack.EquippableType, IItem> equippedItems;

    public Backpack() {
        this.equippedItems = new LinkedHashMap<Backpack.EquippableType, IItem>(EquippableType.values().length);
        this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
        this.addItemToStorage(new Weapon(Weapon.WeaponType.PICK, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.PALETTE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.SWORD, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.DAGGER, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, new ArrayList<Effect>(), "GoldenAxe"));
    }

    public IItem getEquippedItem(Backpack.EquippableType itemToRetrieve) {
        return equippedItems.get(itemToRetrieve);
    }

    /**
     *  Add item to backpack storage.
     *  Returns true if the item has been added successfully, false otherwise (storage full).
     */
    public boolean addItemToStorage(UsableItem newItem) {
        if (this.storage == null) {
            //Lazy initialization of storage arraylist (most creatures will not use this)
            this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
        }
        if (this.storage.size() == MAX_STORAGE_CAPACITY) {
            //Storage full
            return false;
        }

        this.storage.add(newItem);
        return true;
    }

    /*
     * Put an item in an equippable position.
     * Returns old equipped Item, if there is, null otherwise.
     */
    public IItem setEquipItem(EquippableType position, IItem newItem) {
        return this.equippedItems.put(position, newItem);
    }

    public List<UsableItem> getStorage() {
        return this.storage;
    }


    public enum EquippableType {
        HEAD,
        CHEST,
        LEFT_HAND,
        RIGHT_HAND,
        LEFT_RING,
        RIGHT_RING,
        FEET
    }
}
