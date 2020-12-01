package com.kazemieh.www.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

    public static String dbname = "dbnote";

    public static String tablename = "tblnote";

    public static String idnote = "idnote";
    public static String titlenote = "titlenote";
    public static String desnote = "desnote";
    public static String favnote = "favnote";

    public static String createTable = " create table " + tablename + " ( " + idnote + " integer primary key autoincrement  , " + titlenote + " varchar(200) , " + desnote + " varchar ," + favnote + " ineger default 0 ) ; ";

    public DataBaseOpenHelper(@Nullable Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  db.execSQL(" create table tblnote ( idnote integer , titlenote varchar(200) , desnote varchar ) ; ");
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try{
                db.execSQL("alter table " + tablename + " add column " + favnote + " ineter default 0");
            }catch (SQLiteException e){

            }

        }
    }
}
