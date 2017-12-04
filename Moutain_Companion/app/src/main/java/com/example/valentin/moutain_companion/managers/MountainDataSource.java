package com.example.valentin.moutain_companion.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.valentin.moutain_companion.models.Mountain;

import java.util.ArrayList;

/**
 * Created by Zekri on 27/11/2017.
 */

public class MountainDataSource {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static MountainDataSource instance;


    private String[] allColumns = {
                MountainDatabaseHandler.MOUNTAIN_KEY
                ,MountainDatabaseHandler.MOUNTAIN_LATITUDE
                ,MountainDatabaseHandler.MOUNTAIN_LONGITUDE
                ,MountainDatabaseHandler.MOUNTAIN_NOM
                ,MountainDatabaseHandler.MOUNTAIN_ALTITUDE
    };

    public MountainDataSource(Context context){

        this.openHelper = new MountainDatabaseHandler(context);
    }


    public static MountainDataSource getInstance(Context context){
        if(instance == null) {

            instance = new MountainDataSource(context);
        }
        return instance;
    }

    public void open(){

        this.database = openHelper.getWritableDatabase();
    }

    public void close() {

        if(database != null){
            this.database.close();
        }
    }

    public Mountain addMountain(int id, float latitude, float longitude, String nom, int altitude ) {

        ContentValues values = new ContentValues();
        values.put(MountainDatabaseHandler.MOUNTAIN_KEY, id);
        values.put(MountainDatabaseHandler.MOUNTAIN_LATITUDE, latitude);
        values.put(MountainDatabaseHandler.MOUNTAIN_LONGITUDE, longitude);
        values.put(MountainDatabaseHandler.MOUNTAIN_NOM, nom);
        values.put(MountainDatabaseHandler.MOUNTAIN_ALTITUDE, altitude);

        long insertId = database.insert(MountainDatabaseHandler.MOUNTAIN_TABLE_NAME, null, values);
        Cursor cursor = database.query(MountainDatabaseHandler.MOUNTAIN_TABLE_NAME, allColumns, ""+insertId, null, null, null, null);

        cursor.moveToFirst();
        Mountain newMountain = cursorToMountain(cursor);
        cursor.close();

        return newMountain;

    }

    public void deleteMountain(Mountain mountain) {

        long id = mountain.getId();
        System.out.println(" Mountain deleted with id: " + id);
        database.delete(MountainDatabaseHandler.MOUNTAIN_TABLE_NAME, MountainDatabaseHandler.MOUNTAIN_KEY + " = " + id, null);
    }

    public ArrayList<Mountain> getAllMountains() {

        ArrayList<Mountain> mountainList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM sommets", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Mountain mountain = cursorToMountain(cursor);
            mountainList.add(mountain);
            cursor.moveToNext();
        }

        return mountainList;
    }

    private Mountain cursorToMountain(Cursor cursor) {

        Mountain mountain = new Mountain();
        mountain.setId(cursor.getInt(0));
        mountain.setLongitude(cursor.getFloat(1));
        mountain.setLatitude(cursor.getFloat(2));
        mountain.setNom(cursor.getString(3));
        mountain.setAltitude(cursor.getInt(4));

        return mountain;
    }
}
