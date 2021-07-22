package com.developergpaurai.collegeapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databasesql extends SQLiteOpenHelper
{
    public databasesql(Context context) {
        super(context, "college.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table teacher (tid text , tname text , tqual text , tspeech text , temail text , tbranch text ) ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion){
            db.execSQL("ALTER TABLE teacher ADD COLUMN tbranch text DEFAULT 0");
        }else {
            db.execSQL("drop table if exists teacher");
        }
        onCreate(db);
    }
}
