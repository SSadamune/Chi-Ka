package com.ssadamune.crawler;

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

    private static String mansionIchiranUrl (String todofuken, int page) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    private static String houseIchiranUrl (String todofuken, int page) {
        return "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    // get a list of uc code by the name of todofuken
    public static ArrayList<Integer> getMansionsUcList(String todofuken, int endPage) throws IOException {
        // all the uc-code of this todofuken
        ArrayList<Integer> ucList = new ArrayList<>();

        // regex pattern of link, which included the uc-code
        String pattern = "(/ms/chuko/tokyo/sc_" + todofuken + "/nc_)(\\d*)(/)";

        for (int page = 1; page <= endPage; page++) {
            // all the uc-code of current page, use HashSet to avoid duplicate values
            Set<Integer> ucSet = new HashSet<>();

            // parse the html of ichiran-page
            Document doc = Jsoup.connect(mansionIchiranUrl(todofuken, page)).get();

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

    public static ArrayList<Integer> getHousesUcList(String todofuken, int endPage) throws IOException {
        // all the uc-code of this todofuken
        ArrayList<Integer> ucList = new ArrayList<>();

        // regex pattern of link, which included the uc-code
        String pattern = "(/chukoikkodate/tokyo/sc_" + todofuken + "/nc_)(\\d*)(/)";

        for (int page = 1; page <= endPage; page++) {
            // all the uc-code of current page, use HashSet to avoid duplicate values
            Set<Integer> ucSet = new HashSet<>();

            // parse the html of ichiran-page
            Document doc = Jsoup.connect(houseIchiranUrl(todofuken, page)).get();

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

    public static Property getProperty(String todofuken, int ucCode) throws IOException {
        Document doc = Jsoup.connect(bukkengaiyoUrl(todofuken, ucCode)).get();

        // get json data of estate information
        String propertyJson = doc.select("script").first().data();
        propertyJson = propertyJson.substring(25, propertyJson.length() - 11);

        // parse the json-data
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Property.class, new PropertyDeserializer())
                .create();
        Property curProperty = gson.fromJson(propertyJson, Property.class);

        curProperty.setId(ucCode);
        return curProperty;
    }
}
