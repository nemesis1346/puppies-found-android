package com.mywaytech.puppiessearchclient.controllers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mywaytech.puppiessearchclient.R;
import com.mywaytech.puppiessearchclient.models.LocationModel;
import com.mywaytech.puppiessearchclient.services.LocationsAsyncTask;


/**
 * Created by marco on 4/17/2016.
 */
public class MapActivity extends BaseActivity implements OnMapReadyCallback, LocationsAsyncTask.Callbacks {
    public static final int PERMISSIONS_MAP_ACTIVITY = 0;
    private GoogleMap mMap;
    private String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private int permissionCheck_FINE_LOCATIONS;

    private EditText searchText;
    private Button searchBtn;
    private TextView searchResult;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    public int getToolbarTitle() {
        return R.string.mapactivity_title;
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchText = (EditText) findViewById(R.id.edit_text_search_puppy);
        searchBtn = (Button) findViewById(R.id.btn_search);

        searchBtn.setOnClickListener(searchAddress);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_puppies);
        mapFragment.getMapAsync(this);

        permissionCheck_FINE_LOCATIONS = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        permissionsArray,
                        PERMISSIONS_MAP_ACTIVITY);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MAP_ACTIVITY:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //TODO FIX THIS
                } else {

                }
                return;
        }
    }

    @Override
    public void onFinishedSearch(LocationModel locationModel) {
        Log.d("onFinishedSearch", "Search finished:" + String.valueOf(locationModel.getLatitude()) + ", " + String.valueOf(locationModel.getLongitude()));
        drawMapMarkers(locationModel);

    }


    private View.OnClickListener searchAddress = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                String addressInput = searchText.getText().toString();
                LocationsAsyncTask locationsAsyncTask = new LocationsAsyncTask(MapActivity.this);
                locationsAsyncTask.setCallback(MapActivity.this);
                locationsAsyncTask.execute(addressInput);
            } else {
                Toast.makeText(MapActivity.this, "No hay conexion a Internet", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void drawMapMarkers(LocationModel coordinates) {
        LatLng latlng = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title("prueba"));
    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

//    public LocationModel parseToLocationModel(String locationData) {
//        LocationModel locationModelResult = new LocationModel();
//        String[] resultLocationData = locationData.split("\\s+");
//        locationModelResult.setLongitude(Long.valueOf(resultLocationData[0]));
//        locationModelResult.setLatitude(Long.valueOf(resultLocationData[1]));
//        return locationModelResult;
//    }
}
