package com.chen.hanlin.plank;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Create a LatLngBounds that includes West Macedonia Perfecture.
    //private LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(39.821813, 20.784578), new LatLng(40.944606, 22.181563)); //center (40.383210, 21.483071)

    private LatLngBounds allowedBounds = new LatLngBounds(
            new LatLng(70.33956792419954, 178.01171875),
            new LatLng(83.86483689701898, -88.033203125)
    );


    //var lastValidCenter = map.getCenter();
    // private LatLng lastValidCenter = mMap.getCameraPosition().target; //get the center coords


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    //When the Search button is pressed
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
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


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


    //@Override
    //public void onMapLoaded() {
    //   mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
    // }


    // public void


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Kozani and move the camera and zoom there
        LatLng kozani = new LatLng(40.300882, 21.788082);
        mMap.addMarker(new MarkerOptions().position(kozani).title("Marker in Kozani"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(kozani));
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
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kozani, 9));

        //add a circle in kozani
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(40.300882, 21.788082))
                .radius(10000)
                .strokeColor(Color.RED));
        //.fillColor(Color.BLUE));

        // Add a triangle in kozani
        Polygon polygon = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(40.300882, 21.788082), new LatLng(40.300882, 22.788082), new LatLng(41.300882, 22.788082), new LatLng(40.300882, 21.788082))
                .strokeColor(Color.RED));
        //.fillColor(Color.BLUE));

        //Restriction of panning out of the wanted area (Dyt Mak)

        // bounds of the desired area
        /*var allowedBounds = new google.maps.LatLngBounds(
                new google.maps.LatLng(70.33956792419954, 178.01171875),
                new google.maps.LatLng(83.86483689701898, -88.033203125)
        ); */

        //var lastValidCenter = map.getCenter();
        //private LatLng lastValidCenter = mMap.getCameraPosition().target;

        // mMap.setOnCameraChangeListener();
        //  mMap.onC



      /*  google.maps.event.addListener(map, 'center_changed', function() {
            if (allowedBounds.contains(mMap.getCameraPosition().target)) {
                // still within valid bounds, so save the last valid position
                lastValidCenter = mMap.getCameraPosition().target;
                return;
            }
            // not valid anymore => return to last valid position
            mMap.panTo(lastValidCenter);
        }); /*
        //1st way to zoom the camera into my boundaries
        // Set the camera to the greatest possible zoom level that includes the bounds
        // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
        //LatLngBounds bounds = new LatLngBounds.Builder()
        //      .include(new LatLng(39.821813, 20.784578))
        //    .include(new LatLng(40.944606, 22.181563))
        //  .build();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        //2nd way
        // Create a LatLngBounds that includes Australia.
        //private LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(39.821813, 20.784578), new LatLng(40.944606, 22.181563)); //dyt makedonias
        // Set the camera to the greatest possible zoom level that includes the
        // bounds
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
        /* 3rd way
        try {
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
        } catch (IllegalStateException e) {
            // layout not yet initialized
            final View mapView = getFragmentManager()
                    .findFragmentById(R.id.map).getView();
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @SuppressWarnings("deprecation")
                            @SuppressLint("NewApi")
                            // We check which build version we are using.
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    mapView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                } else {
                                    mapView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                }
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(AUSTRALIA, 0));
                            }
                        });
            }
        }  */


    }

    //otan dokimaza me to xml
    /** public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
     private GoogleMap mMap;
     private List<PlaceOnMap> placeList;
     @Override
     protected void onCreate(final Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_maps);
     this.placeList = getPlaceList();
     }
     @Override
     public void onMapReady(final GoogleMap googleMap) {
     this.mMap = googleMap;
     addPlaceListMarkersToGoogleMap();
     }
     private void addPlaceListMarkersToGoogleMap() {
     for (final PlaceOnMap place : this.placeList) {
     final LatLong latLong = new LatLong(place.getPlaceLatitude(), place.getPlaceLongitude());
     this.mMap.addMarker(new MarkerOptions().position(latLong).title(place.getPlaceName()));
     }
     }
     private List<PlaceOnMap> getPlaceList() {
     final String xmlString = "<placesp>" +
     "<placep>" +
     "  <place_id>1</place_id>" +
     "  <name>Place1</name>" +
     "  <description>Place description 1</description>" +
     "  <coordinates>;40.430224;21.559570</coordinates>" +
     "</placep>" +
     "<placep>" +
     "  <place_id>2</place_id>" +
     "  <name>Place2</name>" +
     "  <description>Place description 2</description>" +
     "  <coordinates>;40.423324;21.062439</coordinates>" +
     "</placep>" +
     "<placep>" +
     "  <place_id>3</place_id>" +
     "  <name>Place3</name>" +
     "  <description>Place description 3</description>" +
     "  <coordinates>;40.266952;21.238220</coordinates>" +
     "</placep>" +
     "</placesp>";
     final InputStream xmlStream = getXmlStream(xmlString);
     final PlaceXmlParser parser = new PlaceXmlParser();
     return parser.parsePlacesXml(xmlStream);
     }
     private InputStream getXmlStream(final String xmlString) {
     InputStream xmlStream = null;
     try {
     xmlStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
     } catch (final UnsupportedEncodingException e) {
     e.printStackTrace();
     }
     return xmlStream;
     }
     } **/



}
