package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

/**
 * Created by gio on 22/07/15.
 */
public class MoveAction implements IAction {
    private final Utilities.Directions direction;
    private final IAgent model;

    public MoveAction(IAgent modelToMove, Utilities.Directions direction) {
        this.model = modelToMove;
        this.direction = direction;
    }


    @Override
    public boolean perform() {
        return this.moveOneStep((ICreature)this.model, this.direction);
    }

    @Override
    public int getCost() {
        return 0;
    }

//    public boolean isPerformable() {
//
//    }

    public boolean moveOneStep(ICreature creature, Utilities.Directions direction) {
        Tile startingTile = creature.getPosition();
        Tile arrivalTile = getNextTile(startingTile, direction);
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(startingTile);
        path.add(arrivalTile);
        if (isTileWalkable(arrivalTile)) {
            //TODO Animator.walk(this.hero, startingTile, arrivalTile);
            creature.setPosition(arrivalTile);
            return true;
        }

        return false;
    }

    protected boolean isTileWalkable(Tile tileToCheck) {
        //TODO
        //return tileToCheck.isWalkable();
        return true;
    }


    private static Tile getNextTile(Tile startingTile, Utilities.Directions direction) {
        //TODO da integrare con il metodo fatto da belli per la mappa
        GridPoint2 pos = MoveAction.adjCoords(new GridPoint2(startingTile.getPosition()), direction);


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
