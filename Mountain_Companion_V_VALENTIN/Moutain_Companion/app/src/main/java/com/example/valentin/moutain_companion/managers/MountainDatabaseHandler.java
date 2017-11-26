package com.example.valentin.moutain_companion.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.valentin.moutain_companion.models.Mountain;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Zekri on 26/11/2017.
 */

public class MountainDatabaseHandler extends SQLiteOpenHelper {

    public static final String MOUNTAIN_KEY = "id";
    public static final String MOUNTAIN_LONGITUDE = "longitude";
    public static final String MOUNTAIN_LATITUDE = "latitude";
    public static final String MOUNTAIN_NOM = "nom";
    public static final String MOUNTAIN_IDK = "idk";

    public static final String MOUNTAIN_TABLE_NAME = "sommets";
    public static final String MOUNTAIN_TABLE_CREATE =
            "CREATE TABLE " + MOUNTAIN_TABLE_NAME + " (" +
                    MOUNTAIN_KEY + " INTEGER PRIMARY KEY, " +
                    MOUNTAIN_LONGITUDE + " REAL, " +
                    MOUNTAIN_LATITUDE + " REAL, " +
                    MOUNTAIN_NOM + " TEXT, " +
                    MOUNTAIN_IDK + " INTEGER);";

    public static final String MOUNTAIN_TABLE_DROP = "DROP TABLE IF EXISTS " + MOUNTAIN_TABLE_NAME + ";";

    public MountainDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(MOUNTAIN_TABLE_CREATE);

        String insert_beg = " INSERT INTO " + MOUNTAIN_TABLE_NAME + "("
                + MOUNTAIN_LONGITUDE + ", "
                + MOUNTAIN_LATITUDE + ", "
                + MOUNTAIN_NOM + ", "
                + MOUNTAIN_IDK + ") VALUES ";

        String insert_end = "(\"6.84486789904718\" , \"47.8224908948974\" , \"Ballon d'Alsace\" , \"1247\");";
        sqLiteDatabase.execSQL(insert_beg + insert_end);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(MOUNTAIN_TABLE_DROP);
        onCreate(sqLiteDatabase);

    }



    public ArrayList<Mountain> getMountains() {

        ArrayList<Mountain> mountainList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + MOUNTAIN_TABLE_NAME + " ORDER BY " + MOUNTAIN_KEY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                    Mountain mountain = new Mountain();
                    mountain.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MOUNTAIN_KEY))));
                    mountain.setLongitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MOUNTAIN_LONGITUDE))));
                    mountain.setLatitude(Float.parseFloat(cursor.getString(cursor.getColumnIndex(MOUNTAIN_LATITUDE))));
                    mountain.setNom(cursor.getString(cursor.getColumnIndex(MOUNTAIN_NOM)));
                    mountain.setIdk(Integer.parseInt(cursor.getString(cursor.getColumnIndex(MOUNTAIN_IDK))));

                    mountainList.add(mountain);

            }while (cursor.moveToNext());
        }

        db.close();

        return mountainList;
    }
}
