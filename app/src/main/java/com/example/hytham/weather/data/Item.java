package com.example.hytham.weather.data;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by Hytham on 2/3/2017.
 */

public class Item implements JSONPopulator {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));


    }
}
