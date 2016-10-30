package bit.com.pathmapper.Utilities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.Activities.BaseMapActivity;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.PointOfInterest;


/**
 * Created by tsgar on 28/09/2016.
 */

public class LocationChecker
{

    public void checkNearby(Location location, GoogleMap map, Context context)
    {
        DB_Handler db = new DB_Handler(context);
        List<PointOfInterest> points = db.getAllPOI();

        for (PointOfInterest poi : points)
        {
            double lat = poi.getLat();
            double lng = poi.getLng();
            Location too = new Location("Location B");
            too.setLatitude(lat);
            too.setLongitude(lng);

            double distance=location.distanceTo(too);
            Toast.makeText(context, String.valueOf(distance), Toast.LENGTH_LONG).show();
            if (distance < 25)
            {
                MarkerOptions op = new MarkerOptions();
                op.title(poi.getName());
                op.position(new LatLng(lat, lng));
                map.addMarker(op);
            }

        }

    }

    //Location checker logic for getting the users location and showing the markers
    //Called by the PathMapperActivity





}
