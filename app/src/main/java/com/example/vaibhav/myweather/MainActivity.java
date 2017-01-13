package com.example.vaibhav.myweather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView;
    public static String cityName;
    public String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String errorMessage  = "Error occurred";

        final String urlPart1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        final String urlPart2 = "&APPID=6f428097c29323f63344f65a4408d50d";

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().
                setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES).build();

        autocompleteFragment.setFilter(autocompleteFilter);

        PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final String placeName = (String) place.getName();
                cityName = placeName;
                url = urlPart1 + cityName + urlPart2;
                new WeatherTask().execute();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT);
            }
        };

        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

        url = urlPart1 + cityName + urlPart2;

    }

    private class WeatherTask extends AsyncTask<Void, String, Void> {

        double temperature = 0.0;
        int integerTemperature;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            HTTPHandler httpHandler = new HTTPHandler();
            String jsonString = httpHandler.makeHttpServiceRequest(url);

            if (jsonString != null) {
                try {
                    JSONObject root = new JSONObject(jsonString);
                    JSONObject main = root.getJSONObject("main");
                    temperature = main.getDouble("temp");
                    //integerTemperature = (int) temperature;
                    temperature = convertKelvinToFahrenheit(temperature);
                } catch (final JSONException e){
                    Toast.makeText(MainActivity.this, "You fucked up", Toast.LENGTH_LONG);

                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            TextView tempDisplay = (TextView) findViewById(R.id.current_weather_number);
            String stringTemperature = Double.toString(temperature);
            //stringTemperature =
            stringTemperature = stringTemperature + "°F";
            tempDisplay.setText(stringTemperature);
        }

    }

    private int convertKelvinToFahrenheit(double temp) {
        double temp1 = 1.8 * (temp - 273.15) + 32;
        temp1 = Math.round(temp1);
        return (int) temp1;
    }

}
