package group14.wheresmystuff.controller;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import group14.wheresmystuff.model.Item;


import group14.wheresmystuff.R;
import group14.wheresmystuff.model.Model;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    MapView mapView;
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button logout = (Button) findViewById(R.id.logoutButton);
        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToPage(LoginActivity.class);

            }

        });

        Button addItem = (Button) findViewById(R.id.addItemButton);
        addItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToPage(SubmitItemActivity.class);

            }

        });

        Button showItems = (Button) findViewById(R.id.viewItemsButton);
        showItems.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToPage(DisplayItemsActivity.class);

            }

        });
    }

    public void addMarker(LatLng latlng, String title) {
        map.addMarker(new MarkerOptions()
                .position(latlng)
                .title(title));
    }

    public LatLng convertAddress(String address) {
        Geocoder geoCoder = new Geocoder(this);
        if (address != null && !address.isEmpty()) {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(address, 1);
                if (addressList != null && addressList.size() > 0) {
                    double lat = addressList.get(0).getLatitude();
                    double lng = addressList.get(0).getLongitude();
                    return new LatLng(lat, lng);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        for (Item item : Model.getItemList()) {
            LatLng location = convertAddress(item.getLocation());
            addMarker(location, item.getName());
        }
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void goToPage(Class next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
    }
}
