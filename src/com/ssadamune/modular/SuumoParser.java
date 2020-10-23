package com.ssadamune.modular;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SuumoParser {
    public static Estate getEstate(int ucCode) throws IOException {
        Document doc = Jsoup.connect("https://suumo.jp/ms/chuko/tokyo/sc_toshima/nc_"
                + ucCode + "/bukkengaiyo/").get();
        Element estateJsoup = doc.select("script").first();

        String estateJson = estateJsoup.data();
        estateJson = estateJson.substring(25, estateJson.length() - 11);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Estate.class, new EstateDeserializer())
                .create();
        Estate curEstate = gson.fromJson(estateJson, Estate.class);

        curEstate.setId(ucCode);
        return curEstate;
    }
}
