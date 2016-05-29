package com.example.googlemaps.maps;

/**
 * Created by RajaNageswaraRao on 5/25/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationsDB extends SQLiteOpenHelper{

    private static String DBN = "locationstorage";
    private static int Version = 1;
    public static final String row_id = "row_id";
    public static final String LatitudeF = "latitude";
    public static final String LongitudeF = "longitude";
    public static final String ZoomF = "zoom";
    private static final String DATABASE_TABLE = "locations";
    private SQLiteDatabase SDB;
    public LocationsDB(Context context) {
        super(context, DBN, null, Version);
        this.SDB = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE + " ( " +
                row_id + " integer primary key autoincrement , " +
                LongitudeF + " double , " +
                LatitudeF + " double , " +
                ZoomF + " text " +
                " ) ";
        db.execSQL(sql);
    }
    public long insert(ContentValues contentValues){
        long rowNo = SDB.insert(DATABASE_TABLE, null, contentValues);
        return rowNo;
    }
    public int delelteAll(){
        int count = SDB.delete(DATABASE_TABLE, null , null);
        return count;
    }
    public Cursor getAllLocations(){
        return SDB.query(DATABASE_TABLE, new String[] { row_id,  LatitudeF , LongitudeF, ZoomF } , null, null, null, null, null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}