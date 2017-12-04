package com.example.valentin.moutain_companion.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.valentin.moutain_companion.models.Mountain;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by Zekri on 26/11/2017.
 */

public class MountainDatabaseHandler extends SQLiteAssetHelper {

    public static String DB_NAME = "montagnes.db";

    public static final String MOUNTAIN_KEY = "id";
    public static final String MOUNTAIN_LONGITUDE = "longitude";
    public static final String MOUNTAIN_LATITUDE = "latitude";
    public static final String MOUNTAIN_NOM = "nom";
    public static final String MOUNTAIN_ALTITUDE = "altitude";

    public static final int DATABASE_VERSION = 1;

    public static final String MOUNTAIN_TABLE_NAME = "sommets";

    private SQLiteDatabase myDataBase;


    public MountainDatabaseHandler(Context context){

        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


   /* public void setDirections(Location currentLocation, int maxDistance) throws IOException{

        if(currentLocation == null) {
            return;
        }
        if (myDataBase == null) {

            createDataBase();
            if(myDataBase == null) {

                return;
            }
        }

       mMountains.clear();

        double currentLatitude = currentLocation.getLatitude();
        double currentLongitude = currentLocation.getLongitude();


        double minLat = Math.min(currentLatitude - (maxDistance/111.0f) , currentLatitude + (maxDistance/111.0f));
        double maxLat = Math.max(currentLatitude - (maxDistance/111.0f) , currentLatitude + (maxDistance/111.0f));

        double minLong = Math.min(currentLongitude - (maxDistance/(111.0 * Math.sin(currentLatitude * Math.PI / 180))),
                (currentLongitude + (maxDistance/(111.0 * Math.sin(currentLatitude * Math.PI / 180)))));
        double maxLong = Math.max(currentLongitude - (maxDistance/(111.0 * Math.sin(currentLatitude * Math.PI / 180))),
                (currentLongitude + (maxDistance/(111.0 * Math.sin(currentLatitude * Math.PI / 180)))));

        String qu = "SELECT * FROM sommets WHERE latitude between " + minLat + " AND " + maxLat +
                " AND longitude between " + minLong + " AND " + maxLong;

        Cursor cursor;

        try {
            cursor = getReadableDatabase().rawQuery( qu, null);
        }catch (SQLiteException e) {
            return;
        }

        if(cursor == null) {
            return;
        }

        int tooFar = 0;

        if(cursor.moveToFirst()) {

            Mountain m;

            do {

                try{
                    m = new Mountain(
                            cursor.getInt(cursor.getColumnIndex("_id")),
                            cursor.getDouble(cursor.getColumnIndex("latitude")),
                            cursor.getDouble(cursor.getColumnIndex("longitude")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getInt(cursor.getColumnIndex("altitude")));

                    Location latLng = new Location("");
                    latLng.setLatitude(m.getLatitude());
                    latLng.setLongitude(m.getLongitude());


                    if(currentLocation.distanceTo(latLng) > maxDistance) {
                        tooFar++;
                    }else {

                        mMountains.add(m);
                    }
                }catch (Exception e) {
                    Log.e("MountainCompanion", "Bad database read: " + e.getMessage());
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        Log.d("MountainCompanion", "Added : " + mMountains.size() + " markers, SKIPPED " + tooFar + " tooFar.");
    }*/


}
