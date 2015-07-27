package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

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
        GridPoint2 nextPos = getNextTile(this.hero.getPosition(), direction).getPosition();

        //Controllo se c'è una creatura attaccabile nella direzione dove voglio andare
        for(ICreature creature : GameModel.get().getCreatures()) {
            GridPoint2 cPos = creature.getPosition().getPosition();
            if (cPos.x == nextPos.x && cPos.y == nextPos.y) {
                this.attack(creature);
                return;
            }
        }

        //Altrimenti semplicemente mi sposto lì
        this.moveOneStep(direction);
    }

    public void attack(ICreature attacked) {
        AttackAction action = new AttackAction(this.hero, attacked);
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

    protected boolean isTileWalkable(Tile tileToCheck) {
        //TODO
        //return tileToCheck.isWalkable();
        return true;
    }

    private static Tile getNextTile(Tile startingTile, Utilities.Directions direction) {
        //TODO da integrare con il metodo fatto da belli per la mappa
        GridPoint2 pos = HeroController.adjCoords(new GridPoint2(startingTile.getPosition()), direction);


        return new Tile(pos, Tile.TileType.Solid);
    }

    //TODO non credo che la tile debba mantenere la posizione "reale" (32x32 pixels) ma solo quella virtuale (1x1)
    //TODO a quel punto usa il metodo adjCoords in Utilities
    private static GridPoint2 adjCoords(GridPoint2 inputCords, Utilities.Directions direction){
        GridPoint2 newCords = new GridPoint2(inputCords);
        switch(direction) {
            case NORTH:
                newCords.y+=32;
                break;
            case EAST:
                newCords.x+=32;
                break;
            case SOUTH:
                newCords.y-=32;
                break;
            case WEST:
                newCords.x-=32;
                break;
            case NORTH_EAST:
                newCords.y+=32;
                newCords.x+=32;
                break;
            case NORTH_WEST:
                newCords.y+=32;
                newCords.x-=32;
                break;
            case SOUTH_EAST:
                newCords.y-=32;
                newCords.x+=32;
                break;
            case SOUTH_WEST:
                newCords.y-=32;
                newCords.x-=32;
                break;
        }
        return newCords;
    }
}
