package com.example.hytham.weather.data;

import org.json.JSONObject;

/**
 * Created by Hytham on 2/3/2017.
 */

public class Units implements JSONPopulator {
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");


    }
}
