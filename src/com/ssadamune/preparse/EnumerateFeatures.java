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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ssadamune.crawler.Property;
import com.ssadamune.crawler.SuumoParser;
import com.ssadamune.crawler.UnexpectedFeatureException;

public class EnumerateFeatures {

    static HashMap<String, String> UnexpectedFeatures = new HashMap<String, String>();

    // parse houses or mansions
    static void parseProperty(String todofuken, int ucCode, String property) throws IOException {
        String url = property.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(url).get();

        // get json data of estate information
        String propertyJson = doc.select("script").first().data();
        propertyJson = propertyJson.substring(25, propertyJson.length() - 11);

        // parse the json-data
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Property.class, new PropertyDeserializer())
                .create();

        // if there is an unexpected feature, add it into Map
        String propertyForMap = "[\"" + property + "\", \"" + todofuken + "\", \"" + ucCode + "\"]";
        try {
            Property curProperty = gson.fromJson(propertyJson, Property.class);
        } catch (UnexpectedFeatureException ufe) {
            add2Map(UnexpectedFeatures, ufe.features(), propertyForMap);
        }

    }

    static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            if (item.isBlank()==false) map.putIfAbsent(item.trim(), property);
        }
    }

    // parse the todofuken ichiran page, save the properties into maps
    static void parseTodofuken (String tdfk, int maxHousePages, int maxMansionPages) throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); //20
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); //50
        int houseNum = houseCodes.size();
        int mansionNum = mansionCodes.size();
        System.out.println(houseNum + " houses and " + mansionNum + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            parseProperty(tdfk, nc, "house");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ houseNum + " houses parsed");
        }
        System.out.println(houseNum + " houses completed in " + tdfk);

        properties = 0;
        for (int nc : mansionCodes) {
            parseProperty(tdfk, nc, "mansion");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ mansionNum + " mansions parsed");
        }
        System.out.println(mansionNum + " mansions completed in " + tdfk);
        System.out.println("=====================");
    }

    static String printMap (HashMap<String, String> map) {
        StringBuffer str = new StringBuffer("{\n");
        map.forEach((m, p) -> {
            str.append("    \"" + m + "\" : " + p + "\n");
        });
        str.append("}\n");
        return str.toString();
    }

    static void writeLog() throws IOException{
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
        File logFile = new File("C:\\Users\\zwieb\\Documents\\MDproject\\MDproject\\log\\"
                + "EnumerateFeatures_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("UnexpectedFeatures : " + printMap(UnexpectedFeatures));
        bw.close();
        System.out.println("文件创建成功！");
    }

    public static void main(String[] args) throws IOException {
        parseTodofuken("hachioji", 17, 15);
        parseTodofuken("edogawa", 10, 20);
        parseTodofuken("ota", 14, 39);
        parseTodofuken("setagaya", 20, 50);
        writeLog();
    }

}

class PropertyDeserializer implements JsonDeserializer<Property> {

    @Override
    public Property deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Property curProperty = new Property();

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
