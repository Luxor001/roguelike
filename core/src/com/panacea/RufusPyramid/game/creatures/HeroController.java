package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
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

    public void setPosition(MapContainer spawnMap, GridPoint2 spawnPosition) {

    }

    public void moveToPosition(GridPoint2 finalPosition) {

    }

    /**
     * Move one step to input directory.
     * @param direction direction to move
     * @return true if moved successfully, false otherwise
     */
    public boolean moveOneStep(Utilities.Directions direction) {
        Tile startingTile = this.hero.getPosition();
        Tile arrivalTile = getNextTile(startingTile, direction);
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(startingTile);
        path.add(arrivalTile);
        if (isTileWalkable(arrivalTile)) {
            //TODO Animator.walk(this.hero, startingTile, arrivalTile);
            this.hero.setPosition(arrivalTile, path);
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
