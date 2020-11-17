package com.ssadamune.preparse;

import static com.ssadamune.utils.MyConsts.TOKYO;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class WebCrawler {

	static void parseIchiran(String tdfk, int maxHousePages, int maxMansionPages, Collector[] collectors) 
            throws IOException {
        var houseCodes = SuumoReader.getHousesUcList(tdfk, maxHousePages); // 20
        var mansionCodes = SuumoReader.getMansionsUcList(tdfk, maxMansionPages); // 50
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
        for (String tdfk : TOKYO) {
            parseIchiran(tdfk, 99, 99, collectors);
        }
        for (Collector c : collectors) {
            c.output();
        }
    }

    public static void main(String[] args) throws IOException {

        Collector tc = new TableDataCollector();
        Collector fc = new FeaturesCollector();
        parseIchiran("ome", 1, 1, new Collector[] { tc, fc });
        parseIchiran("setagaya", 1, 1, new Collector[] { tc, fc });
        tc.output();
        fc.output();

    }
}