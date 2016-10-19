package bit.com.pathmapper.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bit.com.pathmapper.Models.PointOfInterest;

/**
 * Created by jacksct1 on 20/10/2016.
 */

public class DB_Handler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pathmapperDB";
    //Table names
    private static final String TABLE_POI = "tblPOI";
    private static final String TABLE_COLLECTION = "tblCollection";
    //POI Table Columns names
    private static final String KEY_POI_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCI_NAME = "scientificName";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_COLLECTION = "collection";
    //Collection Columns names
    private static final String KEY_COLLECTION_ID = "id";
    private static final String KEY_COLLECTION_NAME = "collectionName";

    public DB_Handler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Creation of the Points of Interest Table
        String CREATE_POI_TABLE = "CREATE TABLE " + TABLE_POI + "("
        + KEY_POI_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
        + KEY_SCI_NAME + " TEXT," + KEY_LAT + " INTEGER,"
        + KEY_LNG + " INTEGER," + KEY_DESCRIPTION + " TEXT,"
        + KEY_COLLECTION + " INTEGER" + ")";
        db.execSQL(CREATE_POI_TABLE);

        //Creation of the Collection Table
        String CREATE_COLLECTION_TABLE = "CREATE TABLE " + TABLE_COLLECTION + "("
        + KEY_COLLECTION_ID + " INTEGER PRIMARY KEY," + KEY_COLLECTION_NAME + " TEXT" + ")";
        db.execSQL(CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION);
        // Creating tables again
        onCreate(db);
    }

    //Add new POI
    public void addPOI (PointOfInterest pointOfInterest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pointOfInterest.getName()); // POI Name
        values.put(KEY_SCI_NAME, pointOfInterest.getScientificName()); // POI Scientific Name
        values.put(KEY_LAT, pointOfInterest.getLat()); // POI Latitude
        values.put(KEY_LNG, pointOfInterest.getLng()); // POI Longitude
        values.put(KEY_DESCRIPTION, pointOfInterest.getDescription()); // POI Description
        values.put(KEY_COLLECTION, pointOfInterest.getCollection()); // POI Collection

        // Inserting Row
        db.insert(TABLE_POI, null, values);
        db.close(); // Closing database connection
    }
}
