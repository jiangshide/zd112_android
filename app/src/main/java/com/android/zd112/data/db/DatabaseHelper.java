package com.android.zd112.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.zd112.utils.LogUtils;

/**
 * Created by etongdai on 2018/3/7.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String name = "city";
    private static final int version = 1;

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.e("info", "create table");
        db.execSQL("CREATE TABLE IF NOT EXISTS recentcity (id integer primary key autoincrement, name varchar(40), date INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
