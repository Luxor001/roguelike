package com.panacea.RufusPyramid.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.panacea.RufusPyramid.common.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by gioele.masini on 09/10/2015.
 */
public class DatabaseAndroid extends Database {
    protected SQLiteOpenHelper db_connection;
    protected SQLiteDatabase stmt;

    public DatabaseAndroid(Context context) {
        db_connection = new AndroidDB(context, database_name, null, version);
        stmt=db_connection.getWritableDatabase();
    }

    public void execute(String sql){
        stmt.execSQL(sql);
    }

    public int executeUpdate(String sql){
        stmt.execSQL(sql);
        SQLiteStatement tmp = stmt.compileStatement("SELECT CHANGES()");
        return (int) tmp.simpleQueryForLong();
    }

    public Result query(String sql) {
        ResultAndroid result=new ResultAndroid(stmt.rawQuery(sql,null));
        return result;
    }

    class AndroidDB extends SQLiteOpenHelper {

        private Context context;

        public AndroidDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            stmt=db;
            DatabaseAndroid.this.onCreate(readDumpFile(Database.dump_name + ".sql"));
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            stmt=db;
            DatabaseAndroid.this.onUpgrade();
        }

        private String readDumpFile(String path) {
            String textFile = "";
            AssetManager am = context.getAssets();
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                is = am.open(path);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String data;
                StringBuffer sbuffer = new StringBuffer();
                while ((data = br.readLine()) != null)
                {
                    sbuffer.append(data + "\n");
                }
                textFile = sbuffer.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return textFile;
        }
    }

    public class ResultAndroid implements Result{
        Cursor cursor;

        public ResultAndroid(Cursor cursor) {
            this.cursor=cursor;
        }

        public boolean isEmpty() {
            return cursor.getCount()==0;
        }

        @Override
        public boolean moveToNext() {
            return cursor.moveToNext();
        }

        public int getColumnIndex(String name) {
            return cursor.getColumnIndex(name);
        }

        public String[] getColumnNames() {
            return cursor.getColumnNames();
        }

        public float getFloat(int columnIndex) {
            return cursor.getFloat(columnIndex);
        }

        @Override
        public String getString(int columnIndex) {
            return cursor.getString(columnIndex);
        }

        @Override
        public int getInt(int columnIndex) {
            return cursor.getInt(columnIndex);
        }

//        [...]

    }

}
