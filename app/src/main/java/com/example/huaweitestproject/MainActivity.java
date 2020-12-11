package com.example.huaweitestproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Circle;
import com.huawei.hms.maps.model.CircleOptions;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import android.Manifest;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";

    //HUAWEI map
    private HuaweiMap hmap;

    private MapView mMapView;
    private Marker mMarker;

    private Circle mCircle;


    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String[] RUNTIME_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private static final int REQUEST_CODE = 100;
    private static final LatLng LAT_LNG = new LatLng(31.2304, 121.4737);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.huawei.codelabpush.ON_NEW_TOKEN");
        MainActivity.this.registerReceiver(receiver,filter);
        if (!hasPermissions(this, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE);
        }
        initMap(savedInstanceState);
        String abv = "Master";


        Toast.makeText(this, "branch", Toast.LENGTH_SHORT).show();
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        // please replace "Your API key" with api_key field value in
        // agconnect-services.json
        MapsInitializer.setApiKey("CgB6e3x9BFNXqPyfHrhG6FFb5XiJtd66WawgZPoHW7aD6xFW1dV1ySc8uGiVLdYExqBz+VC9Csex6IZ24IHC618K");
        mMapView.onCreate(mapViewBundle);
        //get map instance
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(HuaweiMap map) {
        Log.d(TAG, "onMapReady: ");

        // after call getMapAsync method ,we can get HuaweiMap instance in this call back method
        hmap = map;
        hmap.setMyLocationEnabled(true);

        // move camera by CameraPosition param ,latlag and zoom params can set here
        CameraPosition build = new CameraPosition.Builder().target(new LatLng(60, 60)).zoom(5).build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(build);
        hmap.animateCamera(cameraUpdate);
        hmap.setMaxZoomPreference(5);
        hmap.setMinZoomPreference(2);

        // mark can be add by HuaweiMap
        mMarker = hmap.addMarker(new MarkerOptions().position(LAT_LNG)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24))
                .clusterable(true));

        mMarker.showInfoWindow();

        // circle can be add by HuaweiMap
        mCircle = hmap.addCircle(new CircleOptions().center(new LatLng(60, 60)).radius(5000).fillColor(Color.GREEN));
        mCircle.setFillColor(Color.TRANSPARENT);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.huawei.codelabpush.ON_NEW_TOKEN".equals(intent.getAction())) {
                String token = intent.getStringExtra("token");
                Log.v("Responzz","Token-"+token);
            }
            Toast.makeText(MainActivity.this, "OnMsgRcvd-"+intent.getStringExtra("keyz"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}