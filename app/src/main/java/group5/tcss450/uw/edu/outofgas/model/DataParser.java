package group5.tcss450.uw.edu.outofgas.model;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by navneet on 23/7/16.
 */
public class DataParser implements Serializable {

    private String mName;
    private String mVicinity;
    private String mLat, mLng;
    private double mRating;
    private int mPriceLevel;

    public DataParser(String theName, String theVicinity, String theLat, String theLng, double theRating, int thePriceLevel) {
        mName = theName;
        mVicinity = theVicinity;
        mLat = theLat;
        mLng = theLng;
        mRating = theRating;
        mPriceLevel = thePriceLevel;
    }

    public DataParser() {
    }

    public List<DataParser> parse(String jsonData) {
        ArrayList<DataParser> list = new ArrayList<>();
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");

            int length = jsonArray.length();
            Log.d("Length", Integer.toString(length));
            if (jsonArray != null) {
                for (int i = 0; i < length; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject geo = object.getJSONObject("geometry");
                    JSONObject location = geo.getJSONObject("location");

                    String name = object.getString("name");
                    String vicinity = object.getString("vicinity");
                    String lat = location.getString("lat");
                    String lng = location.getString("lng");

                    double rating = 0.0;
                    try {
                        rating = object.getDouble("rating");
                    } catch (JSONException e) {
                        rating = 0.0;
                    }

                    int priceLevel = 0;

                    try {
                        priceLevel = object.getInt("price_level");
                    } catch (JSONException e) {
                        priceLevel = 0;
                    }


                    DataParser gas = new DataParser(name, vicinity, lat, lng, rating, priceLevel);
                    list.add(gas);
                }
            }
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return list;
    }

    public String getName() {
        return mName;
    }
    public String getVicinity() {
        return mVicinity;
    }
    public String getLat() {
        return mLat;
    }
    public String getLng() {
        return mLng;
    }
    public double getRating() {
        return mRating;
    }
    public int getPriceLevel() {
        return mPriceLevel;
    }
}