package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by gioele.masini on 09/10/2015.
 */
//General class that needs to be implemented on Android and Desktop Applications
public abstract class Database {

    protected static String database_name="rufus_data";
    protected static String dump_name="rufus_data";
    protected static Database instance = null;
    protected static int version=1;

    /**
     * Runs a sql query like "create".
     * @param sql
     */
    public abstract void execute(String sql);

    /**
     * Identical to execute but returns the number of rows affected (useful for updates)
     * @param sql
     * @return
     */
    public abstract int executeUpdate(String sql);

    /**
     * Runs a query and returns an Object with all the results of the query. [Result Interface is defined below]
     * @param sql
     * @return
     */
    public abstract Result query(String sql);

    public void onCreate(String textFile){
        //Example of Highscore table code (You should change this for your own DB code creation)
//        execute("CREATE  TABLE \"main\".\"Enemies\" (\"Id\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , \"Name\" TEXT NOT NULL , \"Description\" TEXT, \"HpMax\" INTEGER NOT NULL , \"HpMin\" FLOAT NOT NULL , \"AttackMax\" FLOAT NOT NULL , \"AttackMin\" FLOAT NOT NULL , \"DefenceMax\" FLOAT NOT NULL , \"DefenceMin\" FLOAT NOT NULL , \"SpeedMax\" FLOAT NOT NULL , \"SpeedMin\" FLOAT NOT NULL , \"SightMax\" FLOAT NOT NULL , \"SightMin\" FLOAT NOT NULL , \"SpritesheetPath\" TEXT NOT NULL )");
//        execute("INSERT INTO 'highscores'(name,score) values ('Cris',1234)");
        //Example of query to get DB data of Highscore table
        //TODO si può generare direttamente qui il db, se necessario, da un file di dump/export
        this.insertFromFile(textFile);
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

//    public void loadFromSql() {
//        this.insertFromFile("rufus_data.sql");
//    }

    public void onUpgrade(){
        //Example code (You should change this for your own DB code)
        execute("DROP TABLE IF EXISTS 'highscores';");
        onCreate(null);
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

    /**
     * This reads a file from the given Resource-Id and calls every line of it as a SQL-Statement
     * @return Number of SQL-Statements run
     */
    private int insertFromFile(String textFile) {
        // Resetting Counter
        int result = 0;

        if (textFile == null) {
            FileHandle file = Gdx.files.internal(Database.dump_name + ".sql");
            // Iterate through lines (assuming each insert has its own line and theres no other stuff)
            textFile = file.readString();
        }
        String[] lines = textFile.split("\n");
        for (String line : lines) {
            String insertStmt = line;
            this.execute(insertStmt);
            result++;
        }

        // returning number of inserted rows
        return result;
    }
}