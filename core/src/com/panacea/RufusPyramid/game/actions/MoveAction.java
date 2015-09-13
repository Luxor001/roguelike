package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

/**
 * Azione di movimento. Al lancio di perform() muove la creatura e
 * ritorna una ActionResult con true se la creatura si è spostata,
 * false se per qualche motivo non è stato possibile effettuare il movimento.
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
    public ActionResult perform() {
        boolean success = this.moveOneStep((ICreature)this.model, this.direction);
        return new ActionResult(success);
    }

    @Override
    public int getCost() {
        return GameMaster.DEFAULT_ACTION_COST;
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
            GameDrawer.get().getCreaturesDrawer().startWalk(creature, startingTile.getPosition(), arrivalTile.getPosition());
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
        GridPoint2 pos = Utilities.Directions.adjCoords(new GridPoint2(startingTile.getPosition()), direction);
        return new Tile(pos, Tile.TileType.Solid);
    }

}
