package com.example.googlemaps.maps;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private final LatLng LOCATION_UNIV = new LatLng(37.349642, -121.938987);
    private final LatLng LOCATION_BUILDING = new LatLng(37.348190, -121.937975);

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        getSupportLoaderManager().initLoader(0, null, this);
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLngP) {
                drawMarker(latLngP);
                ContentValues contentValues = new ContentValues();
                contentValues.put(LocationsDB.LatitudeF, latLngP.latitude);
                contentValues.put(LocationsDB.LongitudeF, latLngP.longitude);
                contentValues.put(LocationsDB.ZoomF, map.getCameraPosition().zoom);
                LocationInsertTask inserts = new LocationInsertTask();
                inserts.execute(contentValues);
                Toast.makeText(getBaseContext(), "You added a marker", Toast.LENGTH_SHORT).show();
            }
        });
        map.setOnMapLongClickListener(new OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                map.clear();
                LocationDeleteTask deleteTask = new LocationDeleteTask();
                deleteTask.execute();
                Toast.makeText(getBaseContext(), "Markers are deleted", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void drawMarker(LatLng point){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        map.addMarker(markerOptions);
    }
    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    public void onClick_City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);

    }

    public void onClick_University(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 13);
        map.animateCamera(update);

    }

    public void onClick_Building(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_BUILDING, 17);
        map.animateCamera(update);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = LocationsContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;
        locationCount = data.getCount();
        data.moveToFirst();

        for(int i=0;i<locationCount;i++){
            lat = data.getDouble(data.getColumnIndex(LocationsDB.LatitudeF));
            lng = data.getDouble(data.getColumnIndex(LocationsDB.LongitudeF));
            zoom = data.getFloat(data.getColumnIndex(LocationsDB.ZoomF));
            LatLng location = new LatLng(lat, lng);
            drawMarker(location);
            data.moveToNext();
        }

        if(locationCount>0){
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}