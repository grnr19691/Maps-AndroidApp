package com.example.googlemaps.maps;

/**
 * Created by RajaNageswaraRao on 5/25/2015.
 */
import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class LocationsContentProvider extends ContentProvider{

    public static final String PROVIDER_NAME = "com.example.provider.Maps.mylocation";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/locations" );
    private static final int LOCATIONS = 1;

    private static final UriMatcher machingUri ;

    static {
        machingUri = new UriMatcher(UriMatcher.NO_MATCH);
        machingUri.addURI(PROVIDER_NAME, "locations", LOCATIONS);
    }

    LocationsDB db;
    @Override
    public boolean onCreate() {
        db = new LocationsDB(getContext());
        return true;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentsvalues) {
        long rowNo = db.insert(contentsvalues);
        Uri argUri=null;
        if(rowNo>0){
            argUri = ContentUris.withAppendedId(CONTENT_URI, rowNo);
        }else {
            try {
                throw new SQLException("unable to insert row : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return argUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int countOfRows = 0;
        countOfRows = db.delelteAll();
        return countOfRows;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(machingUri.match(uri)==LOCATIONS){
            return db.getAllLocations();
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}