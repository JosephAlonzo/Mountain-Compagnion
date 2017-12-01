package iut_bm_lp.projet_mountaincompanion_v1.controllers;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by Zekri on 26/11/2017.
 */

public class MountainDatabaseHandler extends SQLiteOpenHelper {

    public static final String MOUNTAIN_KEY = "id";
    public static final String MOUNTAIN_LONGITUDE = "longitude";
    public static final String MOUNTAIN_LATITUDE = "latitude";
    public static final String MOUNTAIN_NOM = "nom";
    public static final String MOUNTAIN_ALTITUDE = "altitude";

    public static final int DATABASE_VERSION = 1;

    public static final String MOUNTAIN_TABLE_NAME = "montagnes";
    public static final String MOUNTAIN_TABLE_CREATE =
            "CREATE TABLE " + MOUNTAIN_TABLE_NAME + " (" +
                    MOUNTAIN_KEY + " INTEGER PRIMARY KEY, " +
                    MOUNTAIN_LATITUDE + " REAL, " +
                    MOUNTAIN_LONGITUDE + " REAL, " +
                    MOUNTAIN_NOM + " TEXT, " +
                    MOUNTAIN_ALTITUDE + " INTEGER);";

    public static final String MOUNTAIN_TABLE_DROP = "DROP TABLE IF EXISTS " + MOUNTAIN_TABLE_NAME + ";";

    public MountainDatabaseHandler(Context context) {

        super(context, MOUNTAIN_TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(MOUNTAIN_TABLE_CREATE);

        String insert_beg = " INSERT INTO " + MOUNTAIN_TABLE_NAME + "("
                + MOUNTAIN_KEY + ", "
                + MOUNTAIN_LATITUDE + ", "
                + MOUNTAIN_LONGITUDE + ", "
                + MOUNTAIN_NOM + ", "
                + MOUNTAIN_ALTITUDE + ") VALUES ";

        String insert_end = "(\"1116621810\" , \"47.8224908948974\" , \"6.84486789904718\" , \"Ballon d'Alsace\" , \"1247\");";
        sqLiteDatabase.execSQL(insert_beg + insert_end);

        insert_end = "(\"1661447505\" , \"47.8895027948947\" , \"6.89760629903985\" , \"Col de Bussang\" , \"731\");";
        sqLiteDatabase.execSQL(insert_beg + insert_end);

        insert_end = "(\"1662823209\" , \"47.9227979948934\" , \"6.91711559903713\" , \"Col d'Oderen\" , \"884\");";
        sqLiteDatabase.execSQL(insert_beg + insert_end);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(MOUNTAIN_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}