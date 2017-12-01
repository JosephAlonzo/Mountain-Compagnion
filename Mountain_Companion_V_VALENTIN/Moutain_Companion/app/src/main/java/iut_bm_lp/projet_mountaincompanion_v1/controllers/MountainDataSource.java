package iut_bm_lp.projet_mountaincompanion_v1.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import iut_bm_lp.projet_mountaincompanion_v1.models.Mountain;

/**
 * Created by Zekri on 27/11/2017.
 */

public class MountainDataSource {

    private SQLiteDatabase database;
    private MountainDatabaseHandler dbHelper;
    private String[] allColumns = {
            MountainDatabaseHandler.MOUNTAIN_KEY
            ,MountainDatabaseHandler.MOUNTAIN_LATITUDE
            ,MountainDatabaseHandler.MOUNTAIN_LONGITUDE
            ,MountainDatabaseHandler.MOUNTAIN_NOM
            ,MountainDatabaseHandler.MOUNTAIN_ALTITUDE
    };

    public MountainDataSource(Context context) {

        dbHelper = new MountainDatabaseHandler(context);
    }


    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
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

        // String selectQuery = "SELECT * FROM sommets";
        // SQLiteDatabase db = this.getReadableDatabase();
        // Cursor cursor = database.rawQuery(selectQuery, null);

        Cursor cursor = database.query(MountainDatabaseHandler.MOUNTAIN_TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Mountain mountain = cursorToMountain(cursor);
            mountainList.add(mountain);
            cursor.moveToNext();
        }

//
//        if (cursor.moveToFirst()) {
//            do {
//
//                Mountain mountain = new Mountain();
//                mountain.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MOUNTAIN_KEY))));
//                mountain.setLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MOUNTAIN_LATITUDE))));
//                mountain.setLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MOUNTAIN_LONGITUDE))));
//                mountain.setNom(cursor.getString(cursor.getColumnIndex(MOUNTAIN_NOM)));
//                mountain.setAltitude(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MOUNTAIN_ALTITUDE))));
//
//                mountainList.add(mountain);
//
//            }while (cursor.moveToNext());
//        }

        return mountainList;
    }

    private Mountain cursorToMountain(Cursor cursor) {

        Mountain mountain = new Mountain();
        mountain.setId(cursor.getInt(0));
        mountain.setLatitude(cursor.getFloat(1));
        mountain.setLongitude(cursor.getFloat(2));
        mountain.setNom(cursor.getString(3));
        mountain.setAltitude(cursor.getInt(4));

        return mountain;
    }
}