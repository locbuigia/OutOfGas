package group5.tcss450.uw.edu.outofgas.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group5.tcss450.uw.edu.outofgas.MapsActivity;

/**
 * Created by navneet on 23/7/16.
 */
public class GetNearbyPlacesData extends AsyncTask<Object, String, String>{

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    public double lat;
    public double lng;
    public String name;
    public String vicinity;
    public double rating;
    public int priceLevel;
    public List<DataParser> mList;
    public HashMap<Marker, Integer> mPubMarkerMap = new HashMap<>();

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<DataParser> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        showNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    public void showNearbyPlaces(List<DataParser> nearbyPlacesList) {
        mList = new ArrayList<>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            DataParser googlePlace = nearbyPlacesList.get(i);
            lat = Double.parseDouble(googlePlace.getLat());
            lng = Double.parseDouble(googlePlace.getLng());
            name = googlePlace.getName();
            vicinity = googlePlace.getVicinity();
            rating = googlePlace.getRating();
            priceLevel = googlePlace.getPriceLevel();
            DataParser dataParser = new DataParser(name, vicinity,
                    googlePlace.getLat(), googlePlace.getLng(), rating, priceLevel);
            mList.add(dataParser);
            markerOptions.position(new LatLng(lat,lng));
            markerOptions.title(dataParser.getName());
            Marker mark = mMap.addMarker(markerOptions);
            mPubMarkerMap.put(mark, i);
        }
    }
}