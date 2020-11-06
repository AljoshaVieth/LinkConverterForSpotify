package de.aljoshavieth.spotifytoapple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText("LinkConverterForSpotify v. " + versionName + " by Aljosha.eu");
    }

    public void switchToApple(View view){
        switchStreamingService("appleMusic");
    }

    public void switchToTidal(View view){
        switchStreamingService("tidal");
    }
    public void switchStreamingService(String service){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("streamingservice", service);
        editor.apply();
        MainActivity.streamingService = service;
        Context context = getApplicationContext();
        CharSequence text = "Set streaming service to " + service;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
