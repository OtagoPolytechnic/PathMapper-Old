package bit.com.pathmapper.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by tsgar on 27/09/2016.
 */

public class ClusterMapMarker implements ClusterItem {

    private final LatLng position;

    public ClusterMapMarker(double lat, double lng){
        position = new LatLng(lat, lng);
    }


    @Override
    public LatLng getPosition() {
        return position;
    }
}
