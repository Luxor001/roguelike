package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActiveItemAction;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.EquipItemAction;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.InteractAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.Equippable;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

/**
 * Created by gio on 11/07/15.
 */
public class HeroController {

    private DefaultHero hero;

    public HeroController(DefaultHero hero) {
        this.hero = hero;
        EndGameOnCreatureDeathListener deadListener = new EndGameOnCreatureDeathListener();
        hero.addCreatureDeadListener(deadListener);
    }

    public HeroController(DefaultHero hero, MapContainer spawnMap, Tile startingPosition) {
        this(hero);
        hero.setPosition(startingPosition);
    }


    private HeroController() {
//        this(null);
        //hero.setPosition(new GridPoint2(0,0));
    }

    public void moveToPosition(GridPoint2 finalPosition) {
        //TODO quando avremo un algoritmo di path
    }

    public void chooseTheRightAction(Utilities.Directions direction) {
        Tile nextPos = getNextTile(this.hero.getPosition(), direction);

        if(nextPos == null){ //TODO: sarebbe da costruire i muri esterni..
            return;
        }

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

        if (nextPos.getType() == Tile.TileType.Walkable)
           this.moveOneStep(direction);

        if (nextPos.getType() == Tile.TileType.Door) {
            if (!nextPos.getDoorState())
                this.openDoor(nextPos);
            else
                this.moveOneStep(direction);
        }

        if(nextPos.getType() == Tile.TileType.NextLevel){
        //    GameController.changeLevel(GameModel.get().currentMapIndex+1);
            GameModel.get().changeMap();
        }
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
        GameModel.get().getCurrentMap().getPathGrid()[doorTile.getPosition().x][doorTile.getPosition().y].setWalkable(doorTile.getDoorState()); //FIXME: va bene, ma d'altro canto no. Bisogna rendere l'ai più intelligente, che si basi sul raggio visivo.
    }

    public void use(UsableItem item) {
        IAction action = null;
        if (item instanceof MiscItem) {
            Gdx.app.log(HeroController.class.toString(), "Using item: " + item.getItemType());
            action = new ActiveItemAction(item, this.hero);
        }
        this.hero.fireActionChosenEvent(action);
    }


    private static Tile getNextTile(Tile startingTile, Utilities.Directions direction) {
        GridPoint2 pos = Utilities.Directions.adjCoords(new GridPoint2(startingTile.getPosition()), direction);
        return GameModel.get().getCurrentMap().getMapContainer().getTile(pos);
    }

    private void endGame() {
        //TODO gameOver
        GameController.endGame();
    }

    public void equip(Equippable item) {
        Gdx.app.log(HeroController.class.toString(), "Equipping item: " + item.getItemType());
        IAction action = new EquipItemAction(this.hero, item);
        this.hero.fireActionChosenEvent(action);
    }

    private static class EndGameOnCreatureDeathListener implements CreatureDeadListener {

        private EndGameOnCreatureDeathListener() {};

        @Override
        public void changed(CreatureDeadEvent event, Object source) {
            Gdx.app.log(HeroController.class.toString(), "RUFUS IS REALLY DEAD");
//            GameController.getGm().getHeroController().endGame();
            GameController.endGame();
        }
    }
}
