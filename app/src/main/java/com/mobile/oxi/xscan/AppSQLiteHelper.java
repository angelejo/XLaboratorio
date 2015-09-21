package com.mobile.oxi.xscan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mgoyeneche on 14/09/2015.
 */


    public class AppSQLiteHelper extends SQLiteOpenHelper {

        public static final String SCRIPTCREACIONTABLAS = "CREATE TABLE 'Captura' (  'id' INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,  'creado' TEXT NOT NULL,  'usuario' TEXT,  'latitud' TEXT,  'longitud' TEXT,  'idetiqueta' TEXT,  'imagenbmp' BLOB,  'texto' TEXT,  'tipo' INTEGER,  'xi' INTEGER,  'yi' INTEGER,  'xf' INTEGER,  'yf' INTEGER );";

        private static final String DATABASE_NAME = "xscandb.db";
        private static final int DATABASE_VERSION = 1;


    public AppSQLiteHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SCRIPTCREACIONTABLAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS 'Captura'");
        db.execSQL(SCRIPTCREACIONTABLAS);
        // create fresh books table
        this.onCreate(db);
    }

    }
