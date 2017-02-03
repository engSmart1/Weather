package com.example.hytham.weather.service;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.hytham.weather.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Hytham on 2/3/2017.
 */

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;

    public YahooWeatherService(WeatherServiceCallback weatherServiceCallback) {
        this.callback = weatherServiceCallback;
    }

    public String getLocation() {
        return location;
    }
    public void refreshWeather(final String location){
        this.location = location;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'"
                        , params[0]);

                String endPoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json" , Uri.encode(YQL));

                try {
                    URL url = new URL(endPoint);

                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null){
                        result.append(line);
                    }
                    return result.toString();

                } catch (Exception e) {
                    error = e;

                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null){

                    callback.serviceFailure(error);
                    return;

                }

                try {
                    JSONObject data = new JSONObject(s);
                    JSONObject queryResult = data.optJSONObject("query");
                    int count = queryResult.optInt("count");

                    if (count == 0){
                        callback.serviceFailure(new locationWeatherExcption("No Weather formation found for " + location));

                        return;
                    }
                    Channel channel = new Channel();
                    channel.populate(queryResult.optJSONObject("results").optJSONObject("channel"));

                    callback.serviceSuccess(channel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }.execute(location);


    }
    public class locationWeatherExcption extends Exception{
        public locationWeatherExcption(String message) {
            super(message);
        }
    }
}
