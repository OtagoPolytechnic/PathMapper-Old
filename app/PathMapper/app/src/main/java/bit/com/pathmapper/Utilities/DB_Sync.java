package bit.com.pathmapper.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import bit.com.pathmapper.Activities.BaseMapActivity;

/**
 * Created by jacksct1 on 18/10/2016.
 */

public class DB_Sync
{

    protected Context context;

    public DB_Sync(Context context)
    {
        this.context = context;

        WebService APIThread = new WebService(context);
        APIThread.execute();
    }

    class WebService extends AsyncTask<Void,Void,String>
    {
        private Context mContext;

        public WebService(Context context)
        {
            mContext = context;
        }

        @Override
        protected String doInBackground(Void...params)
        {
            String JSONString = null;

            try
            {
                //Add URL-------------------------------------
                String urlString = "http://jacksct1.pythonanywhere.com/points";

                URL URLObject = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) URLObject.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != 200)
                {
                    Toast.makeText(mContext, "Failed to connect to server", Toast.LENGTH_LONG).show();
                }
                else
                {
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String responseString;
                    StringBuilder stringBuilder = new StringBuilder();
                    while((responseString = bufferedReader.readLine()) != null)
                    {
                        stringBuilder = stringBuilder.append(responseString);
                    }
                    JSONString = stringBuilder.toString();
                }

            } catch (MalformedURLException e) {
                Log.e("URL exception:  ", e.getMessage());
            } catch (IOException e) {
                Log.e("IO exception:  ", e.getMessage());
            }

            return JSONString;
        }

        @Override
        protected void onPostExecute(String fetchedString)
        {

            try
            {
                JSONObject pointsOfInterest = new JSONObject(fetchedString);

                JSONArray collections = pointsOfInterest.getJSONArray("Collections");

                //JSONObject interestPoints = pointsOfInterest.getJSONObject("InterestPoints");
                Log.e("Points Test", collections.toString());



            }
            catch (JSONException e)
            {
                Log.e("JSON exception:  ", e.getMessage());
            }


        }


    }
}
