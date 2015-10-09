package com.panacea.RufusPyramid.common;

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
        execute("CREATE TABLE 'highscores' ('_id' INTEGER PRIMARY KEY  NOT NULL , 'name' VARCHAR NOT NULL , 'score' INTEGER NOT NULL );");
        execute("INSERT INTO 'highscores'(name,score) values ('Cris',1234)");
        //Example of query to get DB data of Highscore table
        Result q=query("SELECT * FROM 'highscores'");
        if (!q.isEmpty()){
            q.moveToNext();
            System.out.println("Highscore of "+q.getString(q.getColumnIndex("name"))+": "+q.getString(q.getColumnIndex("score")));
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