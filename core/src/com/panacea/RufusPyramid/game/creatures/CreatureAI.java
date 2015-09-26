package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.GameModel;

import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.util.List;

/**
 * Created by Lux on 25/09/2015.
 */
public class CreatureAI {
    public enum State{
        STANDING,
        FOLLOWING,
        ATTACKING,
        LOSTSIGHT,
        ROAMING
    }
    private State currentState;
    private AbstractCreature creature;
    private DefaultHero hero;
    private ICreature currentTarget;

    GridFinderOptions pathFinderOptions;
    public CreatureAI(AbstractCreature creature, DefaultHero hero){
        currentState = State.STANDING;
        this.creature = creature;
        this.hero = hero;

        pathFinderOptions = new GridFinderOptions();
        pathFinderOptions.allowDiagonal = false;
        pathFinderOptions.isYDown = false;
    }

    public void chooseNextAction(){

        AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class, pathFinderOptions);

        GridPoint2 currentCreaturePos = creature.getPosition().getPosition();
        GridPoint2 currentHeroPos = hero.getPosition().getPosition();

        NavigationGrid<GridCell> grid = new NavigationGrid<GridCell>(GameModel.get().getCurrentMap().getPathGrid());
        List<GridCell> pathToEnd = finder.findPath( currentCreaturePos.x, currentCreaturePos.y, currentHeroPos.x, currentHeroPos.y, grid);
        if(pathToEnd == null)
            System.out.println("FALLITO!");

        if(currentState == State.FOLLOWING){
            int distance;
        }
    }
}
