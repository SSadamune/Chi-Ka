package com.ssadamune.preparse;

import static com.ssadamune.crawler.Features.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

/*
 * all files of this package are useless for the Finished project
 * this file was made to enumerate all the 「特徴PickupList」
 */

public class FeaturesCollector implements ICollector{

    static HashMap<String, String> UnexpectedFeatures = new HashMap<String, String>();

    private static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            if (item.isBlank()==false) map.putIfAbsent(item.trim(), property);
        }
    }

    private static String printMap (HashMap<String, String> map) {
        StringBuffer str = new StringBuffer("{\n");
        map.forEach((m, p) -> {
            str.append("    \"" + m + "\" : " + p + "\n");
        });
        str.append("}\n");
        return str.toString();
    }

    public void output() throws IOException{
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
        File logFile = new File("log\\Enumerate\\"
                + "Features_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("UnexpectedFeatures : " + printMap(UnexpectedFeatures));
        bw.close();
        System.out.println("文件创建成功！");
    }

    @Override
    public void collect(Document doc, String url, String propertyKind) {
        // TODO Auto-generated method stub
        // get json data of estate information
        String propertyJson = doc.select("script").first().data();
        propertyJson = propertyJson.substring(25, propertyJson.length() - 11);

        // parse the json-data
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Mansion.class, new PropertyDeserializer())
                .create();

        // if there is an unexpected feature, add it into Map
        try {
            gson.fromJson(propertyJson, Mansion.class);
        } catch (UnexpectedFeatureException ufe) {
            add2Map(UnexpectedFeatures, ufe.features(), url);
        }

    }

}

class PropertyDeserializer implements JsonDeserializer<Mansion> {

    @Override
    public Mansion deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Mansion curProperty = new Mansion();

        JsonArray features = jsonObject.get("tokuchoPickupList").getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            String curFeature = features.get(i).getAsString();
            boolean hasUnexpectedFeature = false;
            ArrayList<String> unexpectedFeatures = new ArrayList<String>();
            if(FEATURES.containsKey(curFeature)) {
                curProperty.addFeature(FEATURES.get(curFeature));
            } else {
                if (!curFeature.equals("")) {
                    hasUnexpectedFeature = true;
                    unexpectedFeatures.add(curFeature);
                }
            }
            if (hasUnexpectedFeature == true) throw new UnexpectedFeatureException(unexpectedFeatures);
        }

        return curProperty;
    }
}
