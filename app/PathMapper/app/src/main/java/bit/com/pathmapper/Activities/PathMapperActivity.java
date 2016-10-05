package bit.com.pathmapper.Activities;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import bit.com.pathmapper.Interfaces.IMarkers;
import bit.com.pathmapper.Interfaces.IPaths;

/**
 * Created by tsgar on 27/09/2016.
 */

public class PathMapperActivity extends BaseMapActivity implements IMarkers, IPaths{

    private GoogleMap gMap; //Might no be needed

    //Extends BaseMapActivity
    //Main logic for markers and paths / will create and use the utilities classes as necessary.
    //Subject to changes based on size and how paths and markers interact.

    //TODO create and add interface for paths
    //TODO create and add interface for markers


    @Override
    protected void start() {
        //Should start the map over the gardens information center.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-45.856637, 170.518787), 15));

        Log.e("Yay the thing happened", "");
    }

}
