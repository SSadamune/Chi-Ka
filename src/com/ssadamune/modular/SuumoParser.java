package com.ssadamune.modular;

import java.io.IOException;
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

public class SuumoParser {

    private static String ichiranUrl (String todofuken, int page) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

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

            // all the link in ichiran-page
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
