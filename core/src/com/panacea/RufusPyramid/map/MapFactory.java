package com.panacea.RufusPyramid.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;

import java.util.Random;

/**
 * Created by lux on 08/07/15.
 */
public class MapFactory { /*http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm#The_algorithm*/

    private MapContainer map;
    Texture texture;
    int seed;
    Random random;

    private MapFactory(){}

    public Map generate(int seed){
        this.seed=seed;
        random=new Random(seed);
        map=new MapContainer(50,50);
        fillRect();


        return null;
    }

    public void fillRect(){
     /*step 1, riempimento della mappa con tile solide*/
        texture= new Texture(Gdx.files.internal("data/wall23.gif"));
        for(int row=0; row < map.rLenght();row++)
            for(int column=0; column < map.cLenght();column++)
                map.insertTile(new Tile(new GridPoint2(column,row), Tile.TileType.Solid));


        /*step 2, creazione di uno square iniziale, probabilmente da mettere sotto metodo*/
        int startx= Utilities.randInt(0, map.cLenght() - 5, seed); /*5 supposto come grandezza della stanza iniziale..da sistemare!*/
        int starty=Utilities.randInt(0,map.rLenght()-5,seed);
        for(int row=0; row < 5;row++)
            for(int column=0; column < 5;column++)
                map.insertTile(new Tile(new GridPoint2(startx+column,starty+row),Tile.TileType.Walkable)); /*da controllare se Tile(y,x) va bene o va invertito*/

    }

    public void addRoom(){
        do{
            int height=Utilities.randInt(1, 11,seed);
            int width=Utilities.randInt(1,11,seed);

            int posColumn=Utilities.randInt(0, map.cLenght(),seed);
            int posRow=Utilities.randInt(0, map.rLenght(),seed);

            for(int row=0; row < width;row++)
                for(int column=0; column < height;column++){

            //        if(map.getTile(column,row) != null && map.getTile()
                }
        }while(true);
    }

}
