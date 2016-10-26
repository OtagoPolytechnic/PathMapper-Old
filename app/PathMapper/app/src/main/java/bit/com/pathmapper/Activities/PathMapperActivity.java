package bit.com.pathmapper.Activities;

import android.location.Location;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

import bit.com.pathmapper.Interfaces.IMarkers;
import bit.com.pathmapper.Interfaces.IPaths;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.R;
import bit.com.pathmapper.Utilities.KmlParser;


/**
 * Created by tsgar on 27/09/2016.
 */

public class PathMapperActivity extends BaseMapActivity implements IMarkers, IPaths{

    private GoogleMap gMap;

    //Extends BaseMapActivity
    //Main logic for markers and paths / will create and use the utilities classes as necessary.
    //Subject to changes based on size and how paths and markers interact.

    //TODO create and add interface for paths
    //TODO create and add interface for markers


    @Override
    protected void start() {
        //Should start the map over the gardens information center.
        gMap = getMap();
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-45.856637, 170.518787), 15));

        KmlParser kmlParser = new KmlParser(gMap, this); //Initialize the KmlParser Class and pass it the map and the app context.
        kmlParser.RenderKmlPaths(); //Call the wrapper render function.

        Log.e("Yay the thing happened", "");

    }

    //Retrieves all markers from JSON
    //To be REPLACED by SQL query
    protected void retriveMarkers()
    {
        ClusterManager<ClusterMapMarker> mClusterManager = new ClusterManager<>(this, gMap);
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.poi_areas);
            List<ClusterMapMarker> items = new MyItemReader().read(inputStream);
            mClusterManager.addItems(items);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }



}
