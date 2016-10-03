package bit.com.pathmapper.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import bit.com.pathmapper.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Loads and runs the main PathMapper activity
            Intent intent = new Intent(this, PathMapperActivity.class);
            startActivity(intent);
            finish();
    }
}
