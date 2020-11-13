package com.ssadamune.preparse;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ssadamune.crawler.SuumoParser;

public interface Collector{
    void collect(Document doc, String url, String propertyKind);
    // return the directionary path
    String output() throws IOException;
    void output(String dir) throws IOException;
}

class TodofukenParser{
    static void parseTodofuken (String tdfk, int maxHousePages, int maxMansionPages
            , Collector[] collectors) throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); //20
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); //50
        int houseNum = houseCodes.size();
        int mansionNum = mansionCodes.size();
        System.out.println(houseNum + " houses and " + mansionNum + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            parseProperty(tdfk, nc, "house", collectors);
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ houseNum + " houses parsed");
        }
        System.out.println(houseNum + " houses completed in " + tdfk);

        properties = 0;
        for (int nc : mansionCodes) {
            parseProperty(tdfk, nc, "mansion", collectors);
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ mansionNum + " mansions parsed");
        }
        System.out.println(mansionNum + " mansions completed in " + tdfk);
        System.out.println("=====================");
    }

    static void parseProperty(String todofuken, int ucCode, String propertyKind
            , Collector[] collectors) throws IOException {
        String url = propertyKind.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
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
        final String[] TOKYO_TDFK = {"chiyoda", "chuo", "minato", "shinjuku", "bunkyo", "shibuya"
                , "taito", "sumida", "koto", "arakawa", "adachi", "katsushika", "edogawa", "shinagawa"
                , "meguro", "ota", "setagaya", "nakano", "suginami", "nerima", "toshima", "kita"
                , "itabashi", "hachioji", "tachikawa", "musashino", "mitaka", "ome"};
        String dir = null;
        for (String tdfk : TOKYO_TDFK) {
            parseTodofuken(tdfk, 99, 99, collectors);
        }
        for (Collector c : collectors) {
            if (dir == null) {
                dir = c.output();
            } else {
                c.output(dir);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //collectWholeTokyo(new ICollector[]{new TableDataCollector(), new FeaturesCollector()});
        
        String dir;
        Collector tc = new TableDataCollector();
        Collector fc = new FeaturesCollector();
        parseTodofuken("ome", 3, 3, new Collector[] {tc, fc});
        parseTodofuken("setagaya", 3, 3, new Collector[] {tc, fc});
        dir = tc.output();
        fc.output(dir);
    }
}


