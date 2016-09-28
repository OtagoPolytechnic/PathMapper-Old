package bit.com.pathmapper.Activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import bit.com.pathmapper.Interfaces.IMakers;
import bit.com.pathmapper.Interfaces.IPaths;

/**
 * Created by tsgar on 27/09/2016.
 */

public class PathMapperActivity extends BaseMapActivity implements IMakers, IPaths{


    //Extends baseMapActivity
    //Main logic for markers and paths
    //Subject to changes based on size and how paths and markers interact.

    //TODO create and add interface for paths
    //TODO create and add interface for markers


    @Override
    protected void start() {
        //Should start the map over D block.. not tested.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-45.8717394, 170.5052885), 16));
    }

}
