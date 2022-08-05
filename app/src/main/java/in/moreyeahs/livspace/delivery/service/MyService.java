package in.moreyeahs.livspace.delivery.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.preference.Preference;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.moreyeahs.livspace.delivery.api.RestClient;
import in.moreyeahs.livspace.delivery.model.LatLongModel;
import in.moreyeahs.livspace.delivery.utilities.RxBus;
import in.moreyeahs.livspace.delivery.utilities.SharePrefs;
import in.moreyeahs.livspace.delivery.utilities.Utils;
import in.moreyeahs.livspace.delivery.views.main.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";
    Utils utils;
    MainActivity activity;
    String state="",city="",pincode="",address;

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        Log.i(LOGSERVICE, "onCreate");
        utils = new Utils(MyService.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGSERVICE, "onStartCommand");
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        return Service.START_STICKY;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            Log.i(LOGSERVICE, "lat " + l.getLatitude());
            Log.i(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOGSERVICE, "onConnectionSuspended " + i);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOGSERVICE, "lat " + location.getLatitude());
        Log.i(LOGSERVICE, "lng " + location.getLongitude());
        LatLng mLocation = (new LatLng(location.getLatitude(), location.getLongitude()));
        //EventBus.getDefault().post(mLocation);
        RxBus.getInstance().sendEvent(new LatLng(location.getLatitude(),location.getLongitude()));
        getAddressFromLatlog(location.getLatitude(),location.getLongitude());
        API(location.getLatitude(), location.getLongitude());
        SharePrefs.getInstance(activity).putString(SharePrefs.LAT, String.valueOf(location.getLongitude()));
        SharePrefs.getInstance(activity).putString(SharePrefs.LONG, String.valueOf(location.getLatitude()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOGSERVICE, "onDestroy - Estou sendo destruido ");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");

    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100000);
        mLocationRequest.setFastestInterval(100000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void API(double latitude, double longitude) {
        if (utils.isNetworkAvailable()) {

                int peopleId = SharePrefs.getInstance(activity).getInt(SharePrefs.PEOPLE_ID);


                Postlatlong(new LatLongModel(latitude, longitude, peopleId,state,city,pincode,address));

        } else {
            utils.setToast(MyService.this, "Please Connect to Internet");
        }
    }


    DisposableObserver postLatLong = new DisposableObserver() {
        @Override
        public void onNext(Object o) {
//            Utils.hideProgressDialog(activity);


        }

        @Override
        public void onError(Throwable e) {
            //Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    public void Postlatlong( LatLongModel model) {
        DisposableObserver<JsonObject> observerDes = RestClient.getInstance().getService().postlatlong(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<JsonObject>() {
                    @Override
                    public void onNext(JsonObject result) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getAddressFromLatlog(Double lat, Double log) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, log, 1);
            String addressess = addresses.get(0).getAddressLine(0);
            address=addressess;
             state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            addresses.get(0).getLocality();
            pincode=postalCode;
            city=addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "getAddressFromLatlog:>>> " + e);
        }


    }
}
