package com.uliamar.restaurant.app.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uliamar.restaurant.app.R;
import com.uliamar.restaurant.app.model.CoordParcelable;
import com.uliamar.restaurant.app.model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends ActionBarActivity {
    public static String ARG_RESTAURANT_LIST = "restaurant_list";
    HashMap<Marker,Integer> hashMap=new HashMap<Marker, Integer>();

    public static Intent createIntent(Context c, List<Restaurant> restaurants) {
        Intent myIntent = new Intent(c, MapActivity.class);

        ArrayList<CoordParcelable> coordList = new ArrayList<CoordParcelable>();
        Restaurant restaurant;
        for (int i = 0; i < restaurants.size(); ++i) {
            restaurant = restaurants.get(i);
            coordList.add(new CoordParcelable(restaurant.getRest_id(),
                    restaurant.getGeo_location().getLongitude(),
                    restaurant.getGeo_location().getLatitude(),
                    restaurant.getName()));
        }

        Bundle b = new Bundle();
        b.putParcelableArrayList(ARG_RESTAURANT_LIST, coordList);
        myIntent.putExtras(b);

        return myIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
        GoogleMap googleMap=mapFragment.getMap();
        //googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.951526, 116.340716),15));
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int id=hashMap.get(marker);
                //Toast.makeText(getBaseContext(),"this restaurant id is:"+id,Toast.LENGTH_SHORT).show();
                Intent myIntent=RestaurantActivity.createIntent(getBaseContext(),id);
                startActivity(myIntent);
            }
        });
        Intent i = getIntent();
        List<CoordParcelable> coordList = i.getExtras().getParcelableArrayList(ARG_RESTAURANT_LIST);
        for(CoordParcelable coordParcelable:coordList){
            LatLng latLng=new LatLng(coordParcelable.getmLat(),coordParcelable.getmLon());
            MarkerOptions m = new MarkerOptions().position(latLng).title(coordParcelable.getmName());
            Marker m2 = googleMap.addMarker(m);
            hashMap.put(m2,coordParcelable.getmID());
        }
        Log.v("penis", coordList.size() + " ");
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.map, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
