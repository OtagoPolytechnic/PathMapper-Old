package bit.com.pathmapper.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import bit.com.pathmapper.R;

/**
 * Created by tsgar on 27/09/2016.
 */

public abstract class BaseMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;

    protected int getLayoutID() { return  R.layout.map; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if google services is currently installed
        if(googleServicesAvailable())
        {
            Toast.makeText(this, "Google Service is Available", Toast.LENGTH_LONG).show();
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
        GoogleApiClient mGoogleApiClient;
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
