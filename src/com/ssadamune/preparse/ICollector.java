package com.ssadamune.preparse;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ssadamune.crawler.SuumoParser;

public interface ICollector{
    void collect(Document doc, String url, String propertyKind);
    void output() throws IOException;
}

class TodofukenParser{
    static void parseTodofuken (String tdfk, int maxHousePages, int maxMansionPages
            , ICollector[] collectors) throws IOException {
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
            , ICollector[] collectors) throws IOException {
        String url = propertyKind.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        try {
            Document doc = Jsoup.connect(url).get();
            for (ICollector c : collectors) {
                c.collect(doc, url, propertyKind);
            }
        } catch (org.jsoup.HttpStatusException hse) {
            System.out.println("HttpStatusException : " + hse.getStatusCode());
            System.out.println("URL : " + hse.getUrl());
        }

    }

    public static void collectWholeTokyo(ICollector[] collectors) throws IOException {
        final String[] TOKYO_TDFK = {"chiyoda", "chuo", "minato", "shinjuku", "bunkyo", "shibuya"
                , "taito", "sumida", "koto", "arakawa", "adachi", "katsushika", "edogawa", "shinagawa"
                , "meguro", "ota", "setagaya", "nakano", "suginami", "nerima", "toshima", "kita"
                , "itabashi", "hachioji", "tachikawa", "musashino", "mitaka", "ome"};
        for (String tdfk : TOKYO_TDFK) {
            parseTodofuken(tdfk, 99, 99, collectors);
        }
        for (ICollector c : collectors) {
            c.output();
        }
    }

    public static void main(String[] args) throws IOException {
        //collectWholeTokyo(new ICollector[]{new TableDataCollector(), new FeaturesCollector()});

        
        ICollector collector = new TableDataCollector();
        // parseTodofuken("nerima", 13, 26, collector);
        // parseTodofuken("hachioji", 17, 15, collector);
        // parseTodofuken("edogawa", 10, 20, collector);
        // parseTodofuken("setagaya", 20, 50, collector);
        parseTodofuken("ome", 3, 3, new ICollector[] {collector});
        parseTodofuken("setagaya", 3, 3, new ICollector[] {collector});
        collector.output();
        

    }
}


