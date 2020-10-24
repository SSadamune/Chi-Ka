package com.ssadamune.modular;

import com.google.gson.JsonParseException;

public class UnexpectedFeatureException extends JsonParseException {
    private String featureName;

    UnexpectedFeatureException(String feature) {
        super(feature);
        this.featureName = feature;
    }

    public String feature() {
        return featureName;
    }
}
