package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.game.creatures.ICreature;

/**
 * Created by gioele.masini on 09/10/2015.
 */
//General class that needs to be implemented on Android and Desktop Applications
public abstract class Database {

    protected static String database_name="rufus_data";
    protected static Database instance = null;
    protected static int version=1;

    //Runs a sql query like "create".
    public abstract void execute(String sql);

    //Identical to execute but returns the number of rows affected (useful for updates)
    public abstract int executeUpdate(String sql);

    //Runs a query and returns an Object with all the results of the query. [Result Interface is defined below]
    public abstract Result query(String sql);

    public void onCreate(){
        //Example of Highscore table code (You should change this for your own DB code creation)
//        execute("CREATE  TABLE \"main\".\"Enemies\" (\"Id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"Name\" TEXT NOT NULL , \"Description\" TEXT, \"HpMax\" INTEGER NOT NULL , \"HpMin\" FLOAT NOT NULL , \"AttackMax\" FLOAT NOT NULL , \"AttackMin\" FLOAT NOT NULL , \"DefenceMax\" FLOAT NOT NULL , \"DefenceMin\" FLOAT NOT NULL , \"SpeedMax\" FLOAT NOT NULL , \"SpeedMin\" FLOAT NOT NULL , \"SightMax\" FLOAT NOT NULL , \"SightMin\" FLOAT NOT NULL , \"SpritesheetPath\" TEXT NOT NULL )");
//        execute("INSERT INTO 'highscores'(name,score) values ('Cris',1234)");
        //Example of query to get DB data of Highscore table
        System.out.println("Enemies loaded from db: ");
        try {
            Result q = query("SELECT * FROM 'Enemies'");
            if (!q.isEmpty()) {
                while (q.moveToNext())
                    System.out.println("Creature " + q.getString(q.getColumnIndex("Type")) + ": " + q.getString(q.getColumnIndex("Name")));
            }
        } catch (Exception e) {
            System.err.println("[" + Database.class.toString() + "]" + " ERRORE NELLA LETTURA DAL DB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onUpgrade(){
        //Example code (You should change this for your own DB code)
        execute("DROP TABLE IF EXISTS 'highscores';");
        onCreate();
        System.out.println("DB Upgrade maded because I changed DataBase.version on code");
    }

    //Interface to be implemented on both Android and Desktop Applications
    public interface Result{
        public boolean isEmpty();
        public boolean moveToNext();
        public int getColumnIndex(String name);
        public float getFloat(int columnIndex);
        public String getString(int columnIndex);
        public int getInt(int columnIndex);
//        [...]
    }
}