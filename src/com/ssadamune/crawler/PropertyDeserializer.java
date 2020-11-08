package com.ssadamune.crawler;

import static com.ssadamune.crawler.Features.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PropertyDeserializer implements JsonDeserializer<Mansion> {

    @SuppressWarnings("exports")
    @Override
    public Mansion deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Mansion curProperty = new Mansion();

        int areaCode = jsonObject.get("areaCd").getAsInt();
        curProperty.setAreaCode(areaCode);

        int todofukenCode = jsonObject.get("todofukenCd").getAsInt();
        curProperty.setTodofukenCode(todofukenCode);

        int shikugunCode = jsonObject.get("shikugunCd").getAsInt();
        curProperty.setShikugunCode(shikugunCode);

        JsonArray priceArr = jsonObject.get("headerKakakuDisp").getAsJsonArray();
        String price = priceArr.get(0).getAsString();
        curProperty.setPrice(Integer.parseInt(price.substring(0, price.length() - 4)));

        JsonArray madoriArr = jsonObject.get("madoriDisp").getAsJsonArray();
        curProperty.setMadori(madoriArr.get(0).getAsString());

        JsonArray totalAreaArr = jsonObject.get("senyuMensekiDisp").getAsJsonArray();
        curProperty.setTotalArea(totalAreaArr.get(0).getAsFloat());

        String completeDate = jsonObject.get("kanseiDateDisp").getAsString();
        curProperty.setCompleteDate(completeDate);

        String moveInDate = jsonObject.get("nyukyoDateDisp").getAsString();
        curProperty.setMoveInDate(moveInDate);

        String directionStr = jsonObject.get("muki").getAsString();
        curProperty.setDirection(directCode(directionStr));

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

    static byte directCode (String direction){
        switch (direction) {
        case "北": return 0;
        case "北東": return 1;
        case "東": return 2;
        case "南東": return 3;
        case "南": return 4;
        case "南西": return 5;
        case "西": return 6;
        case "北西": return 7;
        case "": return -1;
        default :
            System.out.println("unexcpection direction: " + direction);
            return -1;
        }
    }
}