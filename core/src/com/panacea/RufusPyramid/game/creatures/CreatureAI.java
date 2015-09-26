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

        List<GridCell> path = getPath(creature, hero);

        if(path != null) { //se il giocatore è raggiungibile..
            int distance = path.size();
            if (path.size() < creature.sigthLength)
                System.out.println("ti vedo!");
            if (currentState == State.FOLLOWING || currentState == State.STANDING || currentState == State.ROAMING) {
                if (distance <= 1) {
                    currentState = State.ATTACKING;
                    //doattack()
                } else {
                    currentState = State.FOLLOWING;
                    //moveOneStep
                }
            }
        }
        else{
            //passturn
        }
    }

    private List<GridCell> getPath(ICreature startingCreature, ICreature arrivalCreature){

        AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class, pathFinderOptions);

        GridPoint2 startingCreaturePos = startingCreature.getPosition().getPosition();
        GridPoint2 arrivalCreaturePos = arrivalCreature.getPosition().getPosition();

        GridCell[][] gridcells = GameModel.get().getCurrentMap().getPathGrid();
        NavigationGrid<GridCell> grid = new NavigationGrid<GridCell>(gridcells);
        List<GridCell> pathToEnd = finder.findPath( startingCreaturePos.x, startingCreaturePos.y, arrivalCreaturePos.x, arrivalCreaturePos.y, grid);
        if(pathToEnd == null)
            System.out.println("FALLITO!");
        else
            for(int i=0; i < pathToEnd.size(); i++){ /*rinizializzazione della grid! riporto tutto allo stato originario, sennò stà libreria mi scasina tutto secondo calcoli suoi!*/
                GridCell currCell = pathToEnd.get(i);
                List<GridCell> neighbors = grid.getNeighbors(currCell, pathFinderOptions );
                for(int j = 0; j < neighbors.size();j++) { //riinizializzo anche i "vicini" dei nodi toccati, che a quanto pare vengono anch'essi scasinati
                    GridCell neighbor = neighbors.get(j);
                    gridcells[neighbor.getX()][neighbor.getY()] = new GridCell(neighbor.getX(),neighbor.getY(),neighbor.isWalkable());
                }
                gridcells[currCell.getX()][currCell.getY()] = new GridCell(currCell.getX(), currCell.getY(), currCell.isWalkable());
            }

        return pathToEnd;
    }
}
