package com.panacea.RufusPyramid.desktop;

import com.panacea.RufusPyramid.common.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by gioele.masini on 09/10/2015.
 */
public class DatabaseDesktop extends Database {
    protected Connection db_connection;
    protected Statement stmt;
    protected boolean nodatabase=false;

    public DatabaseDesktop() {
        loadDatabase();
        if (isNewDatabase()){
            onCreate(this.readDumpFile(Database.dump_name + ".sql"));
            upgradeVersion();
        } else if (isVersionDifferent()){
            onUpgrade();
            upgradeVersion();
        }

    }

    public void execute(String sql){
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int executeUpdate(String sql){
        try {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Result query(String sql) {
        try {
            return new ResultDesktop(stmt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadDatabase(){
        File file = new File(database_name+".db");
        if(!file.exists())
            nodatabase=true;
        try {
            Class.forName("org.sqlite.JDBC");
            db_connection = DriverManager.getConnection("jdbc:sqlite:" + database_name + ".db");
            stmt = db_connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void upgradeVersion() {
        execute("PRAGMA user_version="+version);
    }

    private boolean isNewDatabase() {
        return nodatabase;
    }

    private boolean isVersionDifferent(){
        Result q=query("PRAGMA user_version");
        if (!q.isEmpty())
            return (q.getInt(1)!=version);
        else
            return true;
    }

    private String readDumpFile(String path) {
        String textFile = "";
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            textFile = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                //Do nothing
            }
        }
        return textFile;
    }

    public class ResultDesktop implements Result{

        ResultSet res;
        boolean called_is_empty=false;

        public ResultDesktop(ResultSet res) {
            this.res = res;
        }

        public boolean isEmpty() {
            try {
                if (res.getRow()==0){
                    called_is_empty=true;
                    return !res.next();
                }
                return res.getRow()==0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public boolean moveToNext() {
            try {
                if (called_is_empty){
                    called_is_empty=false;
                    return true;
                } else
                    return res.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public int getColumnIndex(String name) {
            try {
                return res.findColumn(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public float getFloat(int columnIndex) {
            try {
                return res.getFloat(columnIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        public String getString(int columnIndex) {
            try {
                return res.getString(columnIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getInt(int columnIndex) {
            try {
                return res.getInt(columnIndex);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

//        [...]

    }

}
