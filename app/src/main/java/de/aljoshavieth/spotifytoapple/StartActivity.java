package de.aljoshavieth.spotifytoapple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.streaming_services, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(getSelectedStreamingServicePosition());
        TextView authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText(getString(R.string.versionText) + " " + versionName + " " + getString(R.string.author));
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selectedItem = (String) parent.getItemAtPosition(pos);
        switchStreamingService(selectedItem);
    }

    public void switchStreamingService(String service) {
        SharedPreferences sharedPref = getSharedPreferences(
                "streamingservicepreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("streamingservice", service);
        editor.apply();
        MainActivity.streamingService = service;
        Context context = getApplicationContext();
        CharSequence text = "Streaming service : " + service.toUpperCase();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private int getSelectedStreamingServicePosition(){
        SharedPreferences sharedPref = getSharedPreferences(
                "streamingservicepreferences", Context.MODE_PRIVATE);
        String selectedStreamingService = sharedPref.getString("streamingservice",
                "appleMusic");
        switch (selectedStreamingService.toLowerCase()){
            case "applemusic": return 0;
            case "tidal": return 1;
            case "deezer": return 2;
            case "amazonMusic": return 3;
            case "youtubeMusic": return 4;
            default: return 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
