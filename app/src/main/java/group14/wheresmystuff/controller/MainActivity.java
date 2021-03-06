package group14.wheresmystuff.controller;

import android.content.Context;
//import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import group14.wheresmystuff.model.Item;


import group14.wheresmystuff.R;
import group14.wheresmystuff.model.Model;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Where's My Stuff? - " + Model.getActiveUser().getName());
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

    private Marker addMarker(LatLng latlng, String title) {
        MarkerOptions newMarker = new MarkerOptions();
        newMarker.position(latlng).title(title);
        return map.addMarker(newMarker);
    }

    private final Context context = Model.getContext();
    private  LatLng convertAddress(String address) {
        Geocoder geoCoder = new Geocoder(context);
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
        int i = 0;
        for (Item item : Model.getItemList()) {
            LatLng location = convertAddress(item.getLocation());
            Marker itemMarker = addMarker(location, item.getName());
            itemMarker.setTag(item);
            if (getIntent().getExtras() != null && i == getIntent().getExtras().getInt("itemIndex")) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
            }
            i++;
        }
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Bundle bundle = new Bundle();
                int p = 0;
                for (Item item : Model.getItemList()) {
//                    if (item.equals(marker.getTag())) {
                    if (marker.getTag() != null && marker.getTag().toString().equals(item.toString())) {
                        bundle.putInt("itemIndex", p);
                        goToPage(ItemActivity.class, bundle);
                    }
                    p++;
                }
            }
        });
    }

    private void goToPage(Class next) {
        Intent intent = new Intent(this, next);
        startActivity(intent);
    }

    private void goToPage(Class next, Bundle bundle) {
        Intent intent = new Intent(this, next);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
