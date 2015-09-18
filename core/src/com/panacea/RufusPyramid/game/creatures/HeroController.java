package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.InteractAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

import javax.rmi.CORBA.Util;

/**
 * Created by gio on 11/07/15.
 */
public class HeroController {

    private DefaultHero hero;


    public HeroController(DefaultHero hero) {
        this.hero = hero;
    }

    public HeroController(DefaultHero hero, MapContainer spawnMap, Tile startingPosition) {
        this(hero);
        hero.setPosition(startingPosition);
    }

    public void moveToPosition(GridPoint2 finalPosition) {
        //TODO quando avremo un algoritmo di path
    }

    public void chooseTheRightAction(Utilities.Directions direction) {
        Tile nextPos = getNextTile(this.hero.getPosition(), direction);

        //Controllo se c'è una creatura attaccabile nella direzione dove voglio andare
        for(ICreature creature : GameModel.get().getCreatures()) {
            GridPoint2 cPos = creature.getPosition().getPosition();
            if (cPos.x == nextPos.getPosition().x && cPos.y == nextPos.getPosition().y) {
                this.attack(creature);
                return;
            }
        }

        for(Item item: GameModel.get().getItems()){
           GridPoint2 itemPos = item.getPosition();
            if(itemPos.x == nextPos.getPosition().x && itemPos.y == nextPos.getPosition().y) {
                this.interact(item);
                return;
            }
        }

        //Altrimenti semplicemente mi sposto lì

        if(nextPos.getType() == Tile.TileType.Walkable)
           this.moveOneStep(direction);

        if(nextPos.getType() == Tile.TileType.Door)
            if(!nextPos.getDoorState())
                this.openDoor(nextPos);
            else
                this.moveOneStep(direction);
        }

    public void attack(ICreature attacked) {
        AttackAction action = new AttackAction(this.hero, attacked);
        this.hero.fireActionChosenEvent(action);
    }

    public void interact(Item interacted){
        InteractAction action = new InteractAction(interacted, hero);
        this.hero.fireActionChosenEvent(action);
    }

    /**
     * Move one step to input directory.
     * @param direction direction to move
     */
    public void moveOneStep(Utilities.Directions direction) {
        MoveAction action = new MoveAction(this.hero, direction);
        this.hero.fireActionChosenEvent(action);
    }

    public void openDoor(Tile doorTile){
        doorTile.setDoorState(true);
    }

    protected boolean isTileWalkable(Tile tileToCheck) {
        //TODO
        //return tileToCheck.isWalkable();
        return true;
    }


    private static Tile getNextTile(Tile startingTile, Utilities.Directions direction) {
        GridPoint2 pos = Utilities.Directions.adjCoords(new GridPoint2(startingTile.getPosition()), direction);
        return GameModel.get().getCurrentMap().getMapContainer().getTile(pos);
    }
}
