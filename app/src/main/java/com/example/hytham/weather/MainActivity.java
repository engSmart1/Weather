package com.example.hytham.weather;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hytham.weather.data.Channel;
import com.example.hytham.weather.data.Item;
import com.example.hytham.weather.service.WeatherServiceCallback;
import com.example.hytham.weather.service.YahooWeatherService;

public class MainActivity extends AppCompatActivity implements WeatherServiceCallback {
    private ImageView weatherIcomImageView;
    private TextView temperatureTextView , conditionTextView , locationTextView;

    private YahooWeatherService service;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherIcomImageView = (ImageView) findViewById(R.id.weatherIconImageView);

        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        service = new YahooWeatherService(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        service.refreshWeather("Cairo, Egypt");

    }

    @Override
    public void serviceSuccess(Channel channel) {
        progressDialog.hide();

        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode() ,null , getPackageName());
        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable  = getResources().getDrawable(resourceId);
        weatherIcomImageView.setImageDrawable(weatherIconDrawable);

        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());

    }

    @Override
    public void serviceFailure(Exception exception) {
        progressDialog.hide();
        Toast.makeText(this , exception.getMessage(), Toast.LENGTH_LONG).show();

    }
}
