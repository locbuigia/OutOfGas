/**
 * Loc Bui, Andrew Dinh, Phuc Tran
 * Feb 9, 2017
 * @version: 0.1
 */
package group5.tcss450.uw.edu.outofgas;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
 * This is the Google maps activity that presents a map to the user.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    
    /*
     * The Google maps object that presents the view.
     */

    private GoogleMap mMap;
    
    /*
     * Creates the map fragment that is placed onto the user interface.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    /*
     * Places a marker on the map when the map is ready to be viewed.
     * This currently places a marker on UWT and centers the camera to that location.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in UWT and move the camera
        LatLng UWT = new LatLng(47.2451, -122.438);
        mMap.addMarker(new MarkerOptions().position(UWT).title("Marker in UWT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UWT));
        mMap.setMinZoomPreference(16);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
