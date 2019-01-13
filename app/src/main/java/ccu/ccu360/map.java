package ccu.ccu360;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.location.Location;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import static java.lang.Double.parseDouble;

@SuppressLint("ValidFragment")
public class map extends Fragment
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private List<Marker> mHospital, mPhone, mAED;
    private static final int Request_User_Location_Code = 99;
    private double latitude, longitude;
    private int ProximityRadius = 10000;

    private FragmentActivity myContext;
    private Activity activity;
    private View view;
    private ImageButton btn_aed;
    private ImageButton btn_tel;
    private ImageButton btn_room;
    private boolean map_btn_flag_1 = false;
    private boolean map_btn_flag_2 = false;
    private boolean map_btn_flag_3 = false;
    private WhereAmI whereAmI;
    private TextView local_info;

    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private LocationManager mLocationManager;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1000;
    private static final int LOCATION_UPDATE_MIN_TIME = 50;

    private Marker mMarker;


    public map(Activity _activity, View _view) {

        whereAmI = new WhereAmI();
        view = _view;
        activity = _activity;
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) myContext.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mHospital = new ArrayList<>();
        mPhone = new ArrayList<>();
        mAED = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        mLocationManager = (LocationManager) myContext.getSystemService(myContext.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
        map_button_change(2);


        //顆顆寫ㄉ

        new Handler().postDelayed(get_loc,5000);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View v) {
        Log.d("AAA", "AAAA");
        switch (v.getId()) {
            case R.id.btn_3:

                break;

            case R.id.btn_2:

                break;

            case R.id.btn_1:

                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<Marker> DisplayPlaces(List<HashMap<String, String>> placeList, float markColor) {
        List<Marker> markerList = new ArrayList<>();
        for (int i = 0; i < placeList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> markingPlace = placeList.get(i);
            String nameOfPlace = markingPlace.get("name");
            double lat = parseDouble(Objects.requireNonNull(markingPlace.get("lat")));
            double lng = parseDouble(Objects.requireNonNull(markingPlace.get("lng")));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(markColor));
            Marker m = mMap.addMarker(markerOptions);
            markerList.add(i, m);
        }
        return markerList;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.563074, 120.474356), 1.0f));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }


    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_User_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(activity, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("user current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void map_button_change(int position) {
        btn_aed = (ImageButton) view.findViewById(R.id.btn_1);
        btn_aed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mAED.isEmpty()) {
                    String path = null;
                    try {
                        InputStream is = myContext.getAssets().open("aed.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        path = new String(buffer, "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DataParser dataParser = new DataParser();
                    List<HashMap<String, String>> placeList = dataParser.parse(path);

                    mAED = DisplayPlaces(placeList, BitmapDescriptorFactory.HUE_ROSE);
                }


                // TODO Auto-generated method stub
                /*若ImageButton狀態為onClick改變ImageButton的圖片
                 * 並改變textView的文字*/
                if (!map_btn_flag_1) {
                    btn_aed.setImageResource(R.drawable.ic_btn_1_1);
                    btn_aed.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_1 = true;
                    for (int i = 0; i < mAED.size(); i++) {
                        mAED.get(i).setVisible(true);
                    }
                } else {
                    btn_aed.setImageResource(R.drawable.ic_btn_1);
                    btn_aed.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_1 = false;
                    for (int i = 0; i < mAED.size(); i++) {
                        mAED.get(i).setVisible(false);
                    }
                }
            }
        });
        btn_tel = (ImageButton) view.findViewById(R.id.btn_2);
        btn_tel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mPhone.isEmpty()) {
                    String path = null;
                    try {
                        InputStream is = myContext.getAssets().open("phone.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        path = new String(buffer, "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DataParser dataParser = new DataParser();
                    List<HashMap<String, String>> placeList = dataParser.parse(path);

                    mPhone = DisplayPlaces(placeList, BitmapDescriptorFactory.HUE_CYAN);
                }
                // TODO Auto-generated method stub
                /*若ImageButton狀態為onClick改變ImageButton的圖片
                 * 並改變textView的文字*/
                if (!map_btn_flag_2) {
                    btn_tel.setImageResource(R.drawable.ic_btn_2_1);
                    btn_tel.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_2 = true;
                    for (int i = 0; i < mPhone.size(); i++) {
                        mPhone.get(i).setVisible(true);
                    }
                } else {
                    btn_tel.setImageResource(R.drawable.ic_btn_2);
                    btn_tel.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_2 = false;
                    for (int i = 0; i < mPhone.size(); i++) {
                        mPhone.get(i).setVisible(false);
                    }
                }
            }
        });

        btn_room = (ImageButton) view.findViewById(R.id.btn_3);
        btn_room.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mHospital.isEmpty()) {
                    String path = null;
                    try {
                        InputStream is = myContext.getAssets().open("health.json");
                        int size = is.available();
                        byte[] buffer = new byte[size];
                        is.read(buffer);
                        is.close();
                        path = new String(buffer, "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DataParser dataParser = new DataParser();
                    List<HashMap<String, String>> placeList = dataParser.parse(path);

                    mHospital = DisplayPlaces(placeList, BitmapDescriptorFactory.HUE_VIOLET);
                }
                // TODO Auto-generated method stub
                /*若ImageButton狀態為onClick改變ImageButton的圖片
                 * 並改變textView的文字*/
                if (!map_btn_flag_3) {
                    btn_room.setImageResource(R.drawable.ic_btn_3_1);
                    btn_room.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_3 = true;
                    for (int i = 0; i < mHospital.size(); i++) {
                        mHospital.get(i).setVisible(true);
                    }
                } else {
                    btn_room.setImageResource(R.drawable.ic_btn_3);
                    btn_room.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);
                    map_btn_flag_3 = false;
                    for (int i = 0; i < mHospital.size(); i++) {
                        mHospital.get(i).setVisible(false);
                    }
                }
            }
        });
    }

    private Runnable get_loc = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            new Handler().postDelayed(get_loc,5000);
        }
    };

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            
            latitude = location.getLatitude();
            longitude = location.getLongitude();


        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //deal with UI
            switch (msg.what) {
                case 0:
                    String place = whereAmI.whereami(latitude,longitude);
                    local_info = (TextView)view.findViewById(R.id.local_info);
                    local_info.setText(place);
                    break;
            }
        }
    };








}
