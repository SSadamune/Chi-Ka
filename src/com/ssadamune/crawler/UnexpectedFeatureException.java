package com.ssadamune.crawler;

import java.util.ArrayList;

import com.google.gson.JsonParseException;

public class UnexpectedFeatureException extends JsonParseException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<String> features;

    public UnexpectedFeatureException(ArrayList<String> features) {
        super(features.toString());
        this.features = features;
    }

    public String[] features() {
        String[] arr = new String[this.features.size()];
        this.features.toArray(arr);
        return arr;
    }
}
