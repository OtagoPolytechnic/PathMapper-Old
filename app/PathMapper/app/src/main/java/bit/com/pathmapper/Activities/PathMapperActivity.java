package bit.com.pathmapper.Activities;


import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import bit.com.pathmapper.AlertDialogs.Hard;
import bit.com.pathmapper.AlertDialogs.POI_Dialog;
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

    }

    @Override
    protected void showClusters()
    {

        getManager().clearItems();

        DB_Handler db = new DB_Handler(this);
        List<PointOfInterest> points = db.getAllPOI();

        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();
        for (PointOfInterest poi : points)
        {
            double lat = poi.getLat();
            double lng = poi.getLng();
            items.add(new ClusterMapMarker(poi.getId(), lat, lng));

        }

        getManager().addItems(items);
    }

    @Override
    protected void showClustersByCollection(int collectionID)
    {
        getManager().clearItems();

        DB_Handler db = new DB_Handler(this);
        List<PointOfInterest> points = db.getAllCollectionPOI(collectionID);

        List<ClusterMapMarker> items = new ArrayList<ClusterMapMarker>();
        for (PointOfInterest poi : points)
        {
            double lat = poi.getLat();
            double lng = poi.getLng();
            items.add(new ClusterMapMarker(poi.getId(), lat, lng));

        }

        getManager().addItems(items);
    }

    @Override
    protected void showNearClusters(List<ClusterMapMarker> items)
    {
        //getManager().clearItems();
        getManager().addItems(items);
    }




}
