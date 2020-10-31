package com.ssadamune.modular;

import static com.ssadamune.modular.Features.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SuumoParser {

    private static String ichiranUrl (String todofuken, int page) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    // get a list of uc code by the name of todofuken
    public static ArrayList<Integer> getUcList(String todofuken, int endPage) throws IOException {
        // all the uc-code of this todofuken
        ArrayList<Integer> ucList = new ArrayList<>();

        // regex pattern of link, which included the uc-code
        String pattern = "(/ms/chuko/tokyo/sc_" + todofuken + "/nc_)(\\d*)(/)";

        for (int page = 1; page <= endPage; page++) {
            // all the uc-code of current page, use HashSet to avoid duplicate values
            Set<Integer> ucSet = new HashSet<>();

            // parse the html of ichiran-page
            Document doc = Jsoup.connect(ichiranUrl(todofuken, page)).get();

            // all the links in ichiran-page
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String curLink = link.attr("href");
                // match current-link and regex-pattern
                Matcher m = Pattern.compile(pattern).matcher(curLink);
                if (m.find()) {
                    // add uc-code into set
                    ucSet.add(Integer.parseInt(m.group(2)));
                }
            }
            ucList.addAll(ucSet);
        }
        return ucList;
    }

    private static String bukkengaiyoUrl (String todofuken, int ucCode) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
    }

    public static Estate getEstate(String todofuken, int ucCode) throws IOException {
        Document doc = Jsoup.connect(bukkengaiyoUrl(todofuken, ucCode)).get();
        Element estateJsoup = doc.select("script").first();

        // get json data of estate information
        String estateJson = estateJsoup.data();
        estateJson = estateJson.substring(25, estateJson.length() - 11);

        // parse the json-data
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Estate.class, new EstateDeserializer())
                .create();
        Estate curEstate = gson.fromJson(estateJson, Estate.class);

        curEstate.setId(ucCode);
        return curEstate;
    }
}

class EstateDeserializer implements JsonDeserializer<Estate> {

    @Override
    public Estate deserialize(JsonElement json, Type tyepOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();
        Estate curestate = new Estate();

        int areaCode = jsonObject.get("areaCd").getAsInt();
        curestate.setAreaCode(areaCode);

        int todofukenCode = jsonObject.get("todofukenCd").getAsInt();
        curestate.setTodofukenCode(todofukenCode);

        int shikugunCode = jsonObject.get("shikugunCd").getAsInt();
        curestate.setShikugunCode(shikugunCode);

        JsonArray priceArr = jsonObject.get("headerKakakuDisp").getAsJsonArray();
        String price = priceArr.get(0).getAsString();
        curestate.setPrice(Integer.parseInt(price.substring(0, price.length() - 4)));

        JsonArray madoriArr = jsonObject.get("madoriDisp").getAsJsonArray();
        curestate.setMadori(madoriArr.get(0).getAsString());

        JsonArray totalAreaArr = jsonObject.get("senyuMensekiDisp").getAsJsonArray();
        curestate.setTotalArea(totalAreaArr.get(0).getAsFloat());

        String completeDate = jsonObject.get("kanseiDateDisp").getAsString();
        curestate.setCompleteDate(completeDate);

        String moveInDate = jsonObject.get("nyukyoDateDisp").getAsString();
        curestate.setMoveInDate(moveInDate);

        String directionStr = jsonObject.get("muki").getAsString();
        curestate.setDirection(directCode(directionStr));

        JsonArray features = jsonObject.get("tokuchoPickupList").getAsJsonArray();
        for (int i = 0; i < features.size(); i++) {
            String curFeature = features.get(i).getAsString();
            if(FEATURES.containsKey(curFeature)) {
                curestate.addFeature(FEATURES.get(curFeature));
            } else {
                if (!curFeature.equals("")) {
                    throw new UnexpectedFeatureException(curFeature);
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
        case "": return -1;
        default :
            System.out.println("unexcpection direction: " + direction);
            return -1;
        }
    }
}
