package bit.com.pathmapper.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

public abstract class BaseMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    protected int getLayoutID() { return  R.layout.map; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        setUpMap();
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
}
