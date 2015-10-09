package com.panacea.RufusPyramid.android;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.panacea.RufusPyramid.common.Database;

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

        public AndroidDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            stmt=db;
            DatabaseAndroid.this.onCreate();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            stmt=db;
            DatabaseAndroid.this.onUpgrade();
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
