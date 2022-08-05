package in.moreyeahs.livspace.delivery.views.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import in.moreyeahs.livspace.delivery.R;
import in.moreyeahs.livspace.delivery.model.SortedOrdersModel;
import in.moreyeahs.livspace.delivery.utilities.DirectionsJSONParser;
import in.moreyeahs.livspace.delivery.utilities.GpsUtils;
import in.moreyeahs.livspace.delivery.utilities.MarshmallowPermissions;
import in.moreyeahs.livspace.delivery.utilities.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrivingDirectionActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionsJSONParser.directionDuraction {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private double lat, lag, durectionTime;
    private LatLng destinationLatLog;
    private LatLng dblatlag;
    private Marker marker;
    private boolean isGPS = false;
    private ArrayList<SortedOrdersModel> SortedOrdersModel;
    private int orderID;
    private TextView timeDuractionTV, tatalDistanceTV;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        timeDuractionTV = (TextView) findViewById(R.id.tv_time_durection);
        tatalDistanceTV = (TextView) findViewById(R.id.tv_distance);
        permission();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       Intent intent = getIntent();
        SortedOrdersModel = (ArrayList<SortedOrdersModel>) intent.getSerializableExtra("SortedOrdersModel");

        try {
            lat = SortedOrdersModel.get(SortedOrdersModel.size() - 1).getLat();
            lag = SortedOrdersModel.get(SortedOrdersModel.size() - 1).getLng();
            destinationLatLog = new LatLng(lat, lag);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(true);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    map.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                                dblatlag = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(17)
                                        .bearing(90)
                                        .tilt(40)
                                        .build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            }

                            MarkerOptions options = new MarkerOptions();
                            options.position(dblatlag);
                            LatLng origin = dblatlag;
                            LatLng dest = destinationLatLog;
                            String url = getUrl(origin, dest);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);

                          marker = map.addMarker(new MarkerOptions().position(destinationLatLog).title(String.valueOf("Order ID " + orderID)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                           marker.showInfoWindow();
                        }
                    });


            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                dblatlag = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(17)
                        .bearing(90)
                        .tilt(40)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private String getUrl(LatLng origin, LatLng dest) {

        int i = 0;
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String output = "json";

        String waypoints = "";
        for (int j = 0; j < SortedOrdersModel.size(); j++) {
            SortedOrdersModel point = (SortedOrdersModel) SortedOrdersModel.get(j);
            orderID = point.getOrderId();
            if (j == 0){
                waypoints = "waypoints=";
            }

             waypoints += point.getLat() + "," + point.getLng() + "|";
             orderID = point.getOrderId();
             createMarker(destinationLatLog,"Order ID " + orderID,BitmapDescriptorFactory.HUE_GREEN);
            // marker .showInfoWindow();
             marker = map.addMarker(new MarkerOptions().position(destinationLatLog).title(String.valueOf("Order ID " + orderID)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
             marker.showInfoWindow();
        }

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyBR06JQMaQrd_Fnqn643-IroL_vL_bg9LI";
        System.out.println("url" + url);

        return url;
    }



    @Override
    public void onBackPressed() {
       super.onBackPressed();
       finish();
    }
    protected Marker createMarker( LatLng destinationLatLog,String title, float iconResID) {

        return map.addMarker(new MarkerOptions()
                .position(destinationLatLog)
                .anchor(0.5f, 0.5f)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(iconResID)));

    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void durection(double changeduraction, double distance) {
        System.out.println(changeduraction);
        durectionTime = changeduraction;

        if (durectionTime > 60) {
            String convertTime = Utils.convertToDaysHoursMinutes((long) durectionTime);

            NumberFormat formatter = new DecimalFormat("##.00");
            tatalDistanceTV.setText(String.valueOf(formatter.format(distance)) + " KM");
            timeDuractionTV.setText(convertTime);


        } else {
            String convertTime = Utils.convertToDaysHoursMinutes((long) durectionTime);
            timeDuractionTV.setText("" + convertTime);
            //tatalDistanceTV.setText(distance + " KM");
            tatalDistanceTV.setText(new DecimalFormat("##.##").format(distance) + " KM");
        }
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DrivingDirectionActivity.ParserTask parserTask = new DrivingDirectionActivity.ParserTask();
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser(DrivingDirectionActivity.this);
                routes = parser.parse(jObject);
            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try {
                ArrayList<LatLng> points;
                PolylineOptions lineOptions = null;
                if (result == null) {
                    Toast.makeText(DrivingDirectionActivity.this, "No point", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                    lineOptions.addAll(points);
                    lineOptions.width(20);
                    lineOptions.color(Color.RED);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }
                map.addPolyline(lineOptions);
                if (lineOptions != null) {
                    map.addPolyline(lineOptions);
                } else {
                    Log.d("onPostExecute", "without Polylines drawn");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
       /* if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }*/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (MarshmallowPermissions.checkPermissionLocation(DrivingDirectionActivity.this)) {

            } else {
                MarshmallowPermissions.requestPermissionLocation(DrivingDirectionActivity.this, MarshmallowPermissions.PERMISSION_REQUEST_CODE_LOCATION);
            }
        } else {

        }
    }
}
