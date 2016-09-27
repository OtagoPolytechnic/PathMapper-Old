package bit.com.pathmapper;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by tsgar on 27/09/2016.
 */

public class BaseMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;

    protected int getLayoutID() { return  R.layout.map; }

    //Base activity class that creates the map.
    //Mimic off google maps example
    //TODO add overrides and setup methods
    //onResume
    //onCreate
    //SetUpMap
    //GetMap

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
