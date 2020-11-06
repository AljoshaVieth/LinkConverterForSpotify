package de.aljoshavieth.spotifytoapple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static String streamingService = "appleMusic";


    TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(
                "streamingservicepreferences", Context.MODE_PRIVATE);
        streamingService = sharedPref.getString("streamingservice", "appleMusic");
        Log.d("LinkConverterForSpotify", "StreamingService is " + streamingService);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        loadingTextView = findViewById(R.id.loadingText);
        String[] loadingTexts = getResources().getStringArray(R.array.loading_texts);
        String loadingText = loadingTexts[new Random().nextInt(loadingTexts.length)];
        loadingTextView.setText(loadingText);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            String trackID = appLinkData.toString();
            getTrackInfo(trackID);
        }
    }

    public void getTrackInfo(String spotifyUrl) {
        new JsonTask().execute("https://api.song.link/v1-alpha.1/links?url=" + spotifyUrl + "&userCountry=DE");

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            String response = "error";
            try {
                JSONObject jsonObject = new JSONObject(result);
                response = jsonObject.getJSONObject("linksByPlatform").getJSONObject(streamingService).getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response));
            startActivity(browserIntent);
            MainActivity.this.finish();
            System.exit(0);
        }
    }

}
