package bit.com.pathmapper.Activities;


import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.Interfaces.IMarkers;
import bit.com.pathmapper.Interfaces.IPaths;
import bit.com.pathmapper.Models.ClusterMapMarker;
import bit.com.pathmapper.Models.PointOfInterest;
import bit.com.pathmapper.Utilities.DB_Handler;
import bit.com.pathmapper.Utilities.KmlParser;


/**
 * Created by tsgar on 27/09/2016.
 */

public class PathMapperActivity extends BaseMapActivity implements IMarkers, IPaths{


    private ClusterManager<ClusterMapMarker> mClusterManager;
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
        mClusterManager = new ClusterManager<>(this, gMap);

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-45.856637, 170.518787), 15));

        KmlParser kmlParser = new KmlParser(gMap, this); //Initialize the KmlParser Class and pass it the map and the app context.
        kmlParser.RenderKmlPaths(); //Call the wrapper render function.

    }

    @Override
    protected void showClusters()
    {
        mClusterManager.clearItems();
        getMap().setOnCameraIdleListener(mClusterManager);

        DB_Handler db = new DB_Handler(getApplicationContext());
        List<PointOfInterest> points = db.getAllPOI();

        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();
        for (PointOfInterest poi : points)
        {
            double lat = poi.getLat();
            double lng = poi.getLng();
            items.add(new ClusterMapMarker(lat, lng));
            Log.e("JSON exception:  ", String.valueOf(poi.getLat()));
        }

        mClusterManager.addItems(items);
    }

    @Override
    protected void showClustersByCollection(int collectionID)
    {
        mClusterManager.clearItems();
        getMap().setOnCameraIdleListener(mClusterManager);

        DB_Handler db = new DB_Handler(this);
        List<PointOfInterest> points = db.getAllCollectionPOI(collectionID);

        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();
        for (PointOfInterest poi : points)
        {
            double lat = poi.getLat();
            double lng = poi.getLng();
            items.add(new ClusterMapMarker(lat, lng));

        }

        mClusterManager.addItems(items);
    }
}
