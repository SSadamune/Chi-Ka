package com.ssadamune.modular;


import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class EstateDeserializer implements JsonDeserializer<Estate> {

    @SuppressWarnings("exports")
    @Override
    public Estate deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Estate curestate = new Estate();

        int areaCode = jsonObject.get("areaCd").getAsInt();
        curestate.setAreaCode(areaCode);

        int todofukenCode = jsonObject.get("todofukenCd").getAsInt();
        curestate.setTodofukenCode(todofukenCode);

        int shikugunCode = jsonObject.get("shikugunCd").getAsInt();
        curestate.setShikugunCode(shikugunCode);

        JsonArray priceArr = jsonObject.get("headerKakakuDisp").getAsJsonArray();
        curestate.setPrice(priceArr.get(0).getAsInt());

        JsonArray madoriArr = jsonObject.get("madoriDisp").getAsJsonArray();
        curestate.setMadori(madoriArr.get(0).getAsString());

        JsonArray totalAreaArr = jsonObject.get("senyuMensekiDisp").getAsJsonArray();
        curestate.setTotalArea(totalAreaArr.get(0).getAsFloat());

        String completeDate = jsonObject.get("kanseiDateDisp").getAsString();
        curestate.setCompleteDate(completeDate);

        String directionStr = jsonObject.get("muki").getAsString();
        curestate.setDirection(directCode(directionStr));

        JsonArray features = jsonObject.get("tokuchoPickupList").getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            String curFeature = features.get(i).getAsString();
            if(Features.map.containsKey(curFeature)) {
                curestate.addFeature(Features.map.get(curFeature));
            } else {
                if (!curFeature.equals("")) {
                    System.out.println("unexcpect feature: Map.entry(\"" + curFeature + "\", ),");
                }
            }
        }

        return curestate;
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
        default :
            System.out.println("unexcpection direction: " + direction);
            return -1;
        }
    }
}
