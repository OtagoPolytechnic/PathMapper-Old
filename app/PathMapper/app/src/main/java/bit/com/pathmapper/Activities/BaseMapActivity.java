package bit.com.pathmapper.Activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import bit.com.pathmapper.AlertDialogs.Easy;
import bit.com.pathmapper.AlertDialogs.Hard;
import bit.com.pathmapper.AlertDialogs.Hours;
import bit.com.pathmapper.AlertDialogs.Medium;
import bit.com.pathmapper.AlertDialogs.POI_Dialog;
import bit.com.pathmapper.AlertDialogs.Prohibited;
import bit.com.pathmapper.AlertDialogs.Season;
import bit.com.pathmapper.AlertDialogs.Statistics;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.Collection;
import bit.com.pathmapper.R;
import bit.com.pathmapper.Utilities.DB_Handler;

/**
 * Created by tsgar on 27/09/2016.
 */

public abstract class BaseMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ClusterManager<ClusterMapMarker> mClusterManager;


    Hours hoursAlert;
    Season seasonAlert;
    Statistics statisticAlert;
    Prohibited prohibitedAlert;
    Easy easyAlert;
    Medium mediumAlert;
    Hard hardAlert;

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
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.setMinZoomPreference(16);
        map.setMaxZoomPreference(17);

        setClusterManager();

        LatLngBounds polyBounds = new LatLngBounds(
                new LatLng(-45.858595,170.518425),       // South west corner
                new LatLng(-45.857140,170.524462));      // North east corner
        map.setLatLngBoundsForCameraTarget(polyBounds);
        map.setOnMarkerClickListener(getManager());

        start();
        setOverlay();
        googleAPIConnection();
        //showClusters();

    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    protected abstract void start();
    protected abstract void showClusters();
    protected abstract void showClustersByCollection(int collectionID);
    protected abstract void showNearClusters(Location location);

    protected GoogleMap getMap() {
        return map;
    }
    protected ClusterManager getManager() { return mClusterManager; }


    //Start of Menu functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.colour_menu_list, menu);
        for (int i = 0; i < menu.size(); i++)
        {
            menu.getItem(i).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        MenuItem menuItem = menu.findItem(R.id.menu_item_collections);
        SubMenu subMenu = menuItem.getSubMenu();

        List <Collection> cl = new DB_Handler(this).getAllCollections();
        for (Collection col : cl)
        {
            String colName = col.getCollectionName();
            subMenu.add(1, col.getId(), 1, colName);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String itemTitle = (String) item.getTitle();

        switch(itemTitle)
        {
            case "Hours":
                hoursAlert = new Hours();
                FragmentManager fm = getFragmentManager();
                hoursAlert.show(fm, "confirm");
                break;

            case "Seasonal Attractions":
                seasonAlert = new Season();
                FragmentManager fm2 = getFragmentManager();
                seasonAlert.show(fm2, "confirm");
                break;

            case "Garden Statistics":
                statisticAlert = new Statistics();
                FragmentManager fm3 = getFragmentManager();
                statisticAlert.show(fm3, "confirm");
                break;

            case "Prohibited Items":
                prohibitedAlert = new Prohibited();
                FragmentManager fm4 = getFragmentManager();
                prohibitedAlert.show(fm4, "confirm");
                break;

            case "Easy":
                easyAlert = new Easy();
                FragmentManager fm5 = getFragmentManager();
                easyAlert.show(fm5, "confirm");
                break;

            case "Medium":
                mediumAlert = new Medium();
                FragmentManager fm6 = getFragmentManager();
                mediumAlert.show(fm6, "confirm");
                break;

            case "Hard":
                hardAlert = new Hard();
                FragmentManager fm7 = getFragmentManager();
                hardAlert.show(fm7, "confirm");
                break;

            case "Show All":
                showClusters();
                Toast.makeText(this, "Loading....", Toast.LENGTH_LONG).show();

                break;

            default:
                int colID = item.getItemId();
                showClustersByCollection(colID);
                Toast.makeText(this, "Loading....", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }
    //End of Menu functions

    public void setOverlay()
    {
        //Set the bounds of where the overlay will be
        LatLngBounds polyBounds = new LatLngBounds(
                new LatLng(-45.865092,170.511513),       // South west corner
                new LatLng(-45.851950,170.531448));      // North east corner

        //Create the ground the groundoverlay options
        GroundOverlayOptions groundMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.test))
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
        mLocationRequest.setInterval(5000);

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
    public void onLocationChanged(Location location)
    {
        showNearClusters(location);
    }

    public void setClusterManager()
    {
        mClusterManager = new ClusterManager<>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new clusterListener());

    }

    private class clusterListener implements ClusterManager.OnClusterItemClickListener<ClusterMapMarker> {
        @Override
        public boolean onClusterItemClick(ClusterMapMarker clusterMapMarker) {
            POI_Dialog pD= new POI_Dialog();
            Bundle bundle = new Bundle();
            bundle.putInt("key", clusterMapMarker.getID());
            pD.setArguments(bundle);
            FragmentManager fm8 = getFragmentManager();
            pD.show(fm8, "confirm");
            return false;
        }
    }




}
