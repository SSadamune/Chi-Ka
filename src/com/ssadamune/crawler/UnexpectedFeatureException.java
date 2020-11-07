package com.ssadamune.crawler;

import java.util.ArrayList;

import com.google.gson.JsonParseException;

public class UnexpectedFeatureException extends JsonParseException {
    private ArrayList<String> featuresName;

    public UnexpectedFeatureException(ArrayList<String> features) {
        super(features.toString());
        this.featuresName = features;
    }

    public String[] features() {
        String[] arr = new String[this.featuresName.size()];
        this.featuresName.toArray(arr);
        return arr;
    }
}
