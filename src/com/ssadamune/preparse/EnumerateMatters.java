package com.ssadamune.preparse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ssadamune.crawler.SuumoParser;

public class EnumerateMatters {

    static HashMap<String, String> Limits = new HashMap<String, String>();
    static HashMap<String, String> Notices = new HashMap<String, String>();

    // その他制限事項
    // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
    static String[] limits(String tdText) {
        if (tdText.equals("-")||tdText.equals("無")) return null;
        return tdText.split("、|／|■|●|◆|※|・");
    }

    // その他概要・特記事項
    // "担当者：XXX、設備：公営水道、本下水、都市ガス、駐車場：車庫"
    //          => {"設備"：公営水道、本下水、都市ガス、駐車場：車庫}
    static String[] notices(String tdText) {

        if (tdText.equals("-")||tdText.equals("無")) return null;
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(tdText);
        ArrayList<String> noticeArr = new ArrayList<String>();
        while (m.find()) {
            noticeArr.add(m.group(1));
        }
        String[] notices = new String[noticeArr.size()];
        return noticeArr.toArray(notices);
    }

    // parse houses or mansions
    static String parseProperty(String todofuken, int ucCode, String property) throws IOException {
        String url = property.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        Document doc = Jsoup.connect(url).get();
        Elements thtdElements = doc.select("table[summary=表]").first().select("tr > *");
        StringBuffer json = new StringBuffer("{\n" + "    \"ID\" : " + ucCode + ",\n");
        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "その他制限事項" :
                    add2Map(Limits, limits(thtd.text()), property + " " + todofuken + " " + ucCode);
                    break;
                case "その他概要・特記事項" :
                    add2Map(Notices, notices(thtd.text()), property + " " + todofuken + " " + ucCode);
                    break;
                }
            }
        }
        json.append("}");
        return json.toString();
    }

    static String array2Json(String[] array) {
        if (array == null || array.length == 0) return "\"\"";
        StringBuffer jsonArray = new StringBuffer("[");
        for (String str : array) {
            jsonArray.append("\"" + str + "\", ");
        }
        jsonArray.append("]");
        return jsonArray.toString();
    }

    static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            map.putIfAbsent(item.trim(), property);
        }
    }

    static void printMap (HashMap<String, String> map) {
        System.out.println("{");
        map.forEach((item, uc) -> {
            System.out.println("    " + item + " : " + uc);
        });
        System.out.println("}");
    }

    // parse the todofuken ichiran page, save the properties into maps
    static void parseTodofuken (String tdfk, int maxHousePages, int maxMansionPages) throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); //20
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); //50
        System.out.println(houseCodes.size() + " houses and " + mansionCodes.size() + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            parseProperty(tdfk, nc, "house");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ houseCodes.size() + " houses parsed");
        }

        properties = 0;
        for (int nc : mansionCodes) {
            parseProperty(tdfk, nc, "mansion");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ mansionCodes.size() + " mansions parsed");
        }
    }

    public static void main(String[] args) throws IOException {
        parseTodofuken("setagaya", 2, 2);
        printMap(Limits);
        printMap(Notices);
    }

}
