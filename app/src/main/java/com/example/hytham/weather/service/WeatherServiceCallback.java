package com.example.hytham.weather.service;

import com.example.hytham.weather.data.Channel;

/**
 * Created by Hytham on 2/3/2017.
 */

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
