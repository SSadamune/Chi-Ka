package com.sadaki.modular;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class SuumoParser {
    public static Estate getEstate(int ucCode) throws IOException {
        Document doc = Jsoup.connect("https://suumo.jp/ms/chuko/tokyo/sc_toshima/nc_"
                + ucCode + "/bukkengaiyo/").get();
        Element estateJsoup = doc.select("script").first();

        String estateStr = estateJsoup.data();
        estateStr = estateStr.substring(25, estateStr.length() - 11);

        Gson gson = new Gson();
        Estate curEstate = gson.fromJson(estateStr, Estate.class);

        curEstate.setId(ucCode);
        return curEstate;
    }
}
