package com.madventures.sawmalie.madventures;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.location.*;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onDestroy() {
        if((this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")) == 0)
        {
            mMap.setMyLocationEnabled(false);
        }
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        if((this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")) == 0)
        {
            mMap.setMyLocationEnabled(true);
            /*
            if (mMap != null)
            {
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = service.getBestProvider(criteria, false);
                location = service.getLastKnownLocation(provider);
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13));
                mMap.addMarker(new MarkerOptions().position(userLocation));
            }
            else
            {
                mMap.setMyLocationEnabled(true);
            }
            */
        }

    }
}
