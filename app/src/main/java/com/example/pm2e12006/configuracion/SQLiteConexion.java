package com.example.pm2e12006.configuracion;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.example.pm2e12006.transacciones.Transacciones;
import com.example.pm2e12006.transacciones.Transacciones;

public class SQLiteConexion extends SQLiteOpenHelper {


    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, dbname,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

        sqLiteDatabase.execSQL(Transacciones.CreateTBContactos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Transacciones.CreateTBContactos);

    }



}
