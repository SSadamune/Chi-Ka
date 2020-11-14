package com.ssadamune.preparse;

import static com.ssadamune.crawler.Features.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ssadamune.crawler.Mansion;
import com.ssadamune.crawler.UnexpectedFeatureException;
import com.ssadamune.utils.DirectoryPath;

/*
 * all files of this package are useless for the Finished project
 * this file was made to enumerate all the 「特徴PickupList」
 */

public class FeaturesCollector extends Collector {

    static HashMap<String, String> unexpectedFeatures = new HashMap<>();

    @Override
    public void collect(Document doc, String url, String propertyKind) {
        // TODO Auto-generated method stub
        // get json data of estate information
        String propertyJson = doc.select("script").first().data();
        propertyJson = propertyJson.substring(25, propertyJson.length() - 11);

        // parse the json-data
        Gson gson = new GsonBuilder().registerTypeAdapter(Mansion.class, new FeatureDeserializer()).create();

        // if there is an unexpected feature, add it into Map
        try {
            gson.fromJson(propertyJson, Mansion.class);
        } catch (UnexpectedFeatureException ufe) {
            add2Map(unexpectedFeatures, ufe.features(), url);
        }
    }

    public void output() throws IOException {
        DirectoryPath dir = DirectoryPath.getInstance();
        Logger log = Logger.getLogger("EnumLog");
        File logFile = new File(dir.path() + "\\Features.json");
        if (!logFile.createNewFile())
            log.info("create file failed");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()))) {
            bw.write("{\n\"UnexpectedFeatures\" : " + printMap(unexpectedFeatures) + "}");
            log.info("Features.log created SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class FeatureDeserializer implements JsonDeserializer<Mansion> {

    @Override
    public Mansion deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Mansion curProperty = new Mansion();

        JsonArray features = jsonObject.get("tokuchoPickupList").getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            String curFeature = features.get(i).getAsString();
            boolean hasUnexpectedFeature = false;
            ArrayList<String> unexpectedFeatures = new ArrayList<>();
            if (FEATURES.containsKey(curFeature)) {
                curProperty.addFeature(FEATURES.get(curFeature));
            } else {
                if (!curFeature.equals("")) {
                    hasUnexpectedFeature = true;
                    unexpectedFeatures.add(curFeature);
                }
            }
            if (hasUnexpectedFeature)
                throw new UnexpectedFeatureException(unexpectedFeatures);
        }

        return curProperty;
    }
}
