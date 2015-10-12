package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.actions.PassAction;

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

    GridFinderOptions pathFinderOptions;
    public CreatureAI(AbstractCreature creature, DefaultHero hero){
        currentState = State.STANDING;
        this.creature = creature;
        this.hero = hero;

        pathFinderOptions = new GridFinderOptions();
        pathFinderOptions.allowDiagonal = false;
        pathFinderOptions.isYDown = false;
        pathFinderOptions.orthogonalMovementCost = 0;
        pathFinderOptions.diagonalMovementCost = 0;

    }

    public IAction chooseNextAction(){
        PerformanceCounter we= new PerformanceCounter("ASsd");
        we.start();
        GridPoint2 absoluteDistance = Utilities.absoluteDistance(creature.getPosition().getPosition(),hero.getPosition().getPosition());

        IAction chosenAction = new PassAction(creature);
        if(absoluteDistance.y <= creature.sigthLength || absoluteDistance.x <= creature.sigthLength) {
            List<GridCell> path = getPath(creature, hero);
            if (path != null) { //se il giocatore è raggiungibile..
                int distance = path.size();
                if (path.size() < creature.sigthLength) {
                    if (currentState == State.FOLLOWING || currentState == State.STANDING || currentState == State.ROAMING || currentState == State.ATTACKING) {
                        if (distance <= 1) {
                            currentState = State.ATTACKING;
                            chosenAction = new AttackAction(creature, hero);
                        } else {
                            currentState = State.FOLLOWING;
                            Utilities.Directions directionToMove = Utilities.getDirectionFromCoords(creature.getPosition().getPosition(), new GridPoint2(path.get(0).getX(), path.get(0).getY()));
                            chosenAction = new MoveAction(creature, directionToMove);
                        }
                    }
                } else {
                    //currentState = State.LOSTSIGHT;
                }
            }
        }

        we.stop();
        Gdx.app.log(we.current+"","");
        return  chosenAction;
    }

    private List<GridCell> getPath(ICreature startingCreature, ICreature arrivalCreature){
        List<GridCell> pathToEnd = null;
        try {
            AStarGridFinder<GridCell> finder = new AStarGridFinder(GridCell.class, pathFinderOptions);

            GridPoint2 startingCreaturePos = startingCreature.getPosition().getPosition();
            GridPoint2 arrivalCreaturePos = arrivalCreature.getPosition().getPosition();

            GridCell[][] gridcells = GameModel.get().getCurrentMap().getPathGrid();
            NavigationGrid<GridCell> grid = new  NavigationGrid<GridCell>(gridcells, true);
            pathToEnd = finder.findPath(startingCreaturePos.x, startingCreaturePos.y, arrivalCreaturePos.x, arrivalCreaturePos.y, grid);

             if (pathToEnd == null)
                System.out.println("FALLITO!");
            else {
                 GridCell currCell;
                 for (int i = 0; i < pathToEnd.size(); i++) { /*rinizializzazione della grid! riporto tutto allo stato originario, sennò stà libreria mi scasina tutto secondo calcoli suoi!*/
                     currCell = pathToEnd.get(i);
                     reinitializeNeighbors(currCell, grid);
                     currCell = new GridCell(currCell.getX(), currCell.getY(), currCell.isWalkable());
                 }
                 currCell = grid.getCell(startingCreaturePos.x, startingCreaturePos.y);
                 currCell = new GridCell(startingCreaturePos.x, startingCreaturePos.y, currCell.isWalkable());
                 reinitializeNeighbors(currCell, grid);

                 currCell = grid.getCell(arrivalCreaturePos.x, arrivalCreaturePos.y);
                 currCell = new GridCell(arrivalCreaturePos.x, arrivalCreaturePos.y, currCell.isWalkable());
                 reinitializeNeighbors(currCell, grid);
             }
        }
        catch (Exception e){

        }
        return pathToEnd;
    }

    private void reinitializeNeighbors(GridCell currCell, NavigationGrid<GridCell> grid){
        List<GridCell> neighbors = grid.getNeighbors(currCell, pathFinderOptions);
        for (int j = 0; j < neighbors.size(); j++) { //riinizializzo anche i "vicini" dei nodi toccati, che a quanto pare vengono anch'essi scasinati
            GridCell neighbor = neighbors.get(j);
            grid.setCell(neighbor.getX(), neighbor.getY(),new GridCell(neighbor.getX(), neighbor.getY(), neighbor.isWalkable()));
        }
    }

    public State getCurrentState(){
        return currentState;
    }
}
