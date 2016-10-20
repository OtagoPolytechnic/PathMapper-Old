package bit.com.pathmapper.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.Models.PointOfInterest;
import bit.com.pathmapper.R;
import bit.com.pathmapper.Utilities.DB_Handler;

/**
 * Created by tsgar on 27/09/2016.
 */

public abstract class BaseMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    protected int getLayoutID() { return  R.layout.map; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if google services is currently installed
        if(googleServicesAvailable())
        {
            setContentView(getLayoutID());
            setUpMap();
        }
        else
        {
            Toast.makeText(this, "Please install Google Play Services", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        if (map != null) {
            return;
        }
        map = gMap;
        map.setMyLocationEnabled(true);
        start();
        setOverlay();
        googleAPIConnection();


    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    protected abstract void start();

    protected GoogleMap getMap() {
        return map;
    }

    public void setOverlay()
    {
        //Set the bounds of where the overlay will be
        LatLngBounds polyBounds = new LatLngBounds(
                new LatLng(-45.862595,170.515725),       // South west corner
                new LatLng(-45.854140,170.527462));      // North east corner

        //Create the ground the groundoverlay options
        GroundOverlayOptions groundMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.botanic_overlay))
                .positionFromBounds(polyBounds);

        //Set the overly to the map
        map.addGroundOverlay(groundMap);
    }

    public void googleAPIConnection()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    //Check the availability of Google Play Services on phone
    //May move to seperate class handling connections
    public boolean googleServicesAvailable()
    {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if (api.isUserResolvableError(isAvailable))
        {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "Can't get Map", Toast.LENGTH_LONG).show();
        }
        return false;


    }
    @Override
    public void onConnected( Bundle bundle)
    {
        //Request user location
        mLocationRequest = LocationRequest.create();
        //Set location priority to high. Use all available assets to get the most accurate location. GPS, Mobile data (If available)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Set location update interval
        mLocationRequest.setInterval(100);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //When location is changed
    @Override
    public void onLocationChanged(Location location) {


    }

    public void checkNearby(Location location)
    {
        if(location!=null)
        {
            InputStream inputStream = getResources().openRawResource(R.raw.poi_areas);
            List<ClusterMapMarker> items2 = null;
            try {
                items2 = new MyItemReader().read(inputStream);
                Location target = new Location("target");
                for(LatLng point : new LatLng[]{items2.get(0).getPosition(), items2.get(1).getPosition(), items2.get(2).getPosition(), items2.get(3).getPosition()}) {
                    target.setLatitude(point.latitude);
                    target.setLongitude(point.longitude);
                    if(location.distanceTo(target) < 20) {
                        Vibrator v = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(new long[]{500}, -1);
                        map.addMarker(new MarkerOptions().position(new LatLng(target.getLatitude(), target.getLongitude())).title("Near").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Can't find Location", Toast.LENGTH_LONG).show();
        }

    }


}
