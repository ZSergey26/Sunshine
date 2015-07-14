package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastFragment extends Fragment {

    //private final static String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private final static String LOG_TAG = "KEK";
    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    public String POSTAL_CODE = "94043";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask();
            fetchWeatherTask.execute(POSTAL_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forecastArray = {
                "Today - Sunny - 12/32",
                "Tomorrow - Foggy - 13/28",
                "Weds - Cloudy - 14/88",
                "Thurs - Sunny - 14/88",
                "Fri - Sunny - 14/88",
                "Sat - Sunny - 14/88",
                "Sun - Sunny - 14/88",
                "Today - Sunny - 12/32",
                "Tomorrow - Foggy - 13/28",
                "Weds - Cloudy - 14/88",
                "Thurs - Sunny - 14/88",
                "Fri - Sunny - 14/88",
                "Sat - Sunny - 14/88",
                "Sun - Sunny - 14/88"
        };
        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            String postalCode;
            if(params.length != 0)
            {
                postalCode = params[0];
            }
            else
            {
                return null;
            }
            Log.e(LOG_TAG, "postalCode - > " + postalCode);

            String format = "json";
            String units = "metric";
            int numDays = 7;


            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";


            Uri.Builder apiRequest = Uri.parse(FORECAST_BASE_URL).buildUpon();
            apiRequest.appendQueryParameter(QUERY_PARAM, postalCode);
            apiRequest.appendQueryParameter(FORMAT_PARAM, format);
            apiRequest.appendQueryParameter(UNITS_PARAM, units);
            apiRequest.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays));
            apiRequest.build();


            Log.e(LOG_TAG, "Uri.Builder = " + apiRequest);


            Log.e(LOG_TAG, "doInBackground");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonString = null;

            try {
                URL url = new URL(apiRequest.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));


                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                forecastJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, " IOException!", e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            if(forecastJsonString!= null)
                Log.e("KEK",forecastJsonString);
            return null;
        }
    }

}