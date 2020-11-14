package com.ssadamune.preparse;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ssadamune.crawler.SuumoParser;

public abstract class Collector {
    abstract void collect(Document doc, String url, String propertyKind);

    abstract void output() throws IOException;

    protected String[] add2Arr(String[] arr, String... strings) {
        String[] tempArr = new String[arr.length + strings.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for (int i = 0; i < strings.length; i++)
            tempArr[arr.length + i] = strings[i];
        return tempArr;

    }

    protected void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0)
            return;
        for (String item : items) {
            if (!item.isBlank())
                map.putIfAbsent(item.trim(), property);
        }
    }

    protected String printMap(HashMap<String, String> map) {
        StringBuilder str = new StringBuilder("{\n");
        map.forEach((m, p) -> str.append("    \"" + m + "\" : \"" + p + "\",\n"));
        return str.substring(0, str.length() - 2) + "\n}";
    }

}

class TodofukenParser {
    static void parseTodofuken(String tdfk, int maxHousePages, int maxMansionPages, Collector[] collectors)
            throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); // 20
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); // 50
        int houseNum = houseCodes.size();
        int mansionNum = mansionCodes.size();
        System.out.println(houseNum + " houses and " + mansionNum + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            parseProperty(tdfk, nc, "house", collectors);
            properties += 1;
            if (properties % 100 == 0)
                System.out.println(properties + "/" + houseNum + " houses parsed");
        }
        System.out.println(houseNum + " houses completed in " + tdfk);

        properties = 0;
        for (int nc : mansionCodes) {
            parseProperty(tdfk, nc, "mansion", collectors);
            properties += 1;
            if (properties % 100 == 0)
                System.out.println(properties + "/" + mansionNum + " mansions parsed");
        }
        System.out.println(mansionNum + " mansions completed in " + tdfk);
        System.out.println("=====================");
    }

    static void parseProperty(String todofuken, int ucCode, String propertyKind, Collector[] collectors)
            throws IOException {
        String url = propertyKind.equals("house") ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        try {
            Document doc = Jsoup.connect(url).get();
            for (Collector c : collectors) {
                c.collect(doc, url, propertyKind);
            }
        } catch (org.jsoup.HttpStatusException hse) {
            System.out.println("HttpStatusException : " + hse.getStatusCode());
            System.out.println("URL : " + hse.getUrl());
        }

    }

    public static void collectWholeTokyo(Collector[] collectors) throws IOException {
        final String[] TOKYO_TDFK = { "chiyoda", "chuo", "minato", "shinjuku", "bunkyo", "shibuya", "taito", "sumida",
                "koto", "arakawa", "adachi", "katsushika", "edogawa", "shinagawa", "meguro", "ota", "setagaya",
                "nakano", "suginami", "nerima", "toshima", "kita", "itabashi", "hachioji", "tachikawa", "musashino",
                "mitaka", "ome" };
        for (String tdfk : TOKYO_TDFK) {
            parseTodofuken(tdfk, 99, 99, collectors);
        }
        for (Collector c : collectors) {
            c.output();
        }
    }

    public static void main(String[] args) throws IOException {
        // collectWholeTokyo(new Collector[]{new TableDataCollector(), new
        // FeaturesCollector()});

        Collector tc = new TableDataCollector();
        Collector fc = new FeaturesCollector();
        parseTodofuken("ome", 3, 3, new Collector[] { tc, fc });
        parseTodofuken("setagaya", 3, 3, new Collector[] { tc, fc });
        tc.output();
        fc.output();

    }
}
