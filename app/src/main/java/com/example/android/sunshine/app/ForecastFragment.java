package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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

    public ForecastFragment() {
    }

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
        List<String > weekForecast = new ArrayList<>(Arrays.asList(forecastArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast );
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonString = null;

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q=94043,usa&mode=json&units=metric&cnt=7");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null)
            {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));


            String line;
            while((line=reader.readLine()) != null)
            {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0)
            {
                return null;
            }

            forecastJsonString = buffer.toString();
        }
        catch (IOException e) {
            Log.e(MainActivity.TAG, " IOException!", e);
        }
        finally {
            if(urlConnection != null)
            {
                urlConnection.disconnect();
            }

            if(reader!=null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(MainActivity.TAG, "Error closing stream", e);
                }
            }
        }

        return rootView;
    }
}