package com.chen.hanlin.plank;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.hanlin.plank.Firebase.FirebaseDatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.chen.hanlin.plank.PostActivity;
import com.chen.hanlin.plank.Firebase.FirebaseUserEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.chen.hanlin.plank.Firebase.FirebaseDatabaseHelper.databaseReference;
import static com.chen.hanlin.plank.PostActivity.list_locations;
import static com.chen.hanlin.plank.PostActivity.list_notes;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static LatLng current = new LatLng(10, 10);
    private ValueEventListener mPostListener;
    // Create a LatLngBounds that includes West Macedonia Perfecture.
    //private LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(39.821813, 20.784578), new LatLng(40.944606, 22.181563)); //center (40.383210, 21.483071)





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }



    //clicking on post, take you to posting
    public void onPost(View view) {

        LatLng currentLatLng = getCurrentLocation();
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng));
        mMarker.setTag(0);

        Intent moveToHome = new Intent(MapsActivity.this, PostActivity.class);
        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(moveToHome);
    }


//current location code
    public LatLng getCurrentLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //Permission check for location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //     ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            return current;
        }
        Location location = lm.getLastKnownLocation(lm.getBestProvider(criteria, false));
        return new LatLng(location.getLatitude(),location.getLongitude());
    }

    //pulls all information out of data base, geocode it and display on map

    public void onUpdate(View view){
        //EditText locationTextField = (EditText) findViewById(R.id.searchTextField);
        int j=0;
        while(true) {
            String location = list_locations.get(j);
            List<Address> addressList = null;

            //Get the marker on the address the user inputs
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(list_notes.get(j)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


            //Display the coordinates
            TextView coordinatesTextView = (TextView) findViewById(R.id.coordinatesTextView);
            coordinatesTextView.setText("N " + address.getLatitude() + " E " + address.getLongitude());

            //Here we get and display the address from the coordinates
            TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
            // addressTextView.setText("addr");

            Geocoder geocoder2 = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder2.getFromLocation(address.getLatitude(), address.getLongitude(), 1);

                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder();
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                    }
                    addressTextView.setText(strReturnedAddress.toString());
                } else {
                    addressTextView.setText("No Address returned!");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                addressTextView.setText("Cannot get Address!");
            }
            if(++j == list_locations.size()){
                break;
            }
        }
    }

    //When the Search button is pressed, geocode the search text
    public void onSearch(View view) {
        EditText locationTextField = (EditText) findViewById(R.id.searchTextField);
        String location = locationTextField.getText().toString();
        List<Address> addressList = null;

        //Get the marker on the address the user inputs
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Search"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        new CameraPosition.Builder().zoom(15);


        //Display the coordinates
        TextView coordinatesTextView = (TextView) findViewById(R.id.coordinatesTextView);
        coordinatesTextView.setText("N " + address.getLatitude() + " E " + address.getLongitude());

        //Here we get and display the address from the coordinates
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        // addressTextView.setText("addr");

        Geocoder geocoder2 = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder2.getFromLocation(address.getLatitude(), address.getLongitude(), 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
                }
                addressTextView.setText(strReturnedAddress.toString());
            } else {
                addressTextView.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            addressTextView.setText("Cannot get Address!");
        }


    }


    //log out
    public void onLogOut(View V){
        Intent loggingOut = new Intent(MapsActivity.this,  MainActivity.class);
        loggingOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loggingOut);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(""));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        /*
        parseString(list_locations.get(0));
        Log.d("location" , list_locations.get(0));
        int i = 0;
        while (list_locations.get(i) != null) {
            Log.d("location" , list_locations.get(i));
            parseString(list_locations.get(i));
            i++;
        }

        */
        mMap.setMyLocationEnabled(true);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 9));
        new CameraPosition.Builder().zoom(15);

    }


}
