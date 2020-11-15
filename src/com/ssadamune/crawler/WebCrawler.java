package com.ssadamune.crawler;

import static com.ssadamune.utils.MyConsts.TOKYO;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class WebCrawler {

    // 1. 是不是应该把 hp, mp 在此方法内部声明，并此方法的最后执行 .output()，而非传入 hp, mp
    // 2. 是不是应该再次抽象，将本函数内部的 houseXX, mansionXX 统一成 propertyXX，传入参数改成 PropertyParser pp...
    // 3. 28-30 行：不如直接给 House() 加上构造方法，传入 url 生成 doc, 这样 buildProperty() 方法只需要传入 curHouse 一个参数
    // 4. 或者，干脆就把从一览url到生成doc的这一段全都放进构造函数里，这样这整个方法都不需要了
    static void parseIchiran(String tdfk, int maxHousePages, int maxMansionPages, HouseParser hp, MansionParser mp)
            throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); 
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); 
        int houseNum = houseCodes.size();
        int mansionNum = mansionCodes.size();
        System.out.println(houseNum + " houses and " + mansionNum + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            String url = "https://suumo.jp/chukoikkodate/tokyo/sc_" + tdfk + "/nc_" + nc + "/bukkengaiyo/";

            try {
                Document doc = Jsoup.connect(url).get();
                House curHouse = new House(); 
                hp.buildProperty(curHouse, doc, url); 
                /**
                 * TODO:
                 * insert curHouse to DB
                 **/

            } catch (org.jsoup.HttpStatusException hse) {
                System.out.println("HttpStatusException : " + hse.getStatusCode());
                System.out.println("URL : " + hse.getUrl());
            }

            properties += 1;
            if (properties % 100 == 0)
                System.out.println(properties + "/" + houseNum + " houses parsed");
        }
        System.out.println(houseNum + " houses completed in " + tdfk);

        properties = 0;
        for (int nc : mansionCodes) {
            String url = "https://suumo.jp/ms/chuko/tokyo/sc_" + tdfk + "/nc_" + nc + "/bukkengaiyo/";

            try {
                Document doc = Jsoup.connect(url).get();
                Mansion curMansion = new Mansion(); 
                mp.buildProperty(curMansion, doc, url); 
                /**
                 * TODO:
                 * insert curMansion to DB
                 **/

            } catch (org.jsoup.HttpStatusException hse) {
                System.out.println("HttpStatusException : " + hse.getStatusCode());
                System.out.println("URL : " + hse.getUrl());
            }

            properties += 1;
            if (properties % 100 == 0)
                System.out.println(properties + "/" + mansionNum + " mansions parsed");
        }
        System.out.println(mansionNum + " mansions completed in " + tdfk);
        System.out.println("=====================");
    }

    public static void collectWholeTokyo() throws IOException {
        HouseParser hp = new HouseParser();
        MansionParser mp = new MansionParser();
        for (String tdfk : TOKYO) {
            parseIchiran(tdfk, 99, 99, hp, mp); // hp, mp
        }
        hp.outputSurpirses();
        mp.outputSurpirses();
    }

    public static void main(String[] args) throws IOException {
        // collectWholeTokyo(new Collector[]{new TableDataCollector(), new
        // FeaturesCollector()});

        HouseParser hp = new HouseParser();
        MansionParser mp = new MansionParser();
        parseIchiran("ome", 1, 1, hp, mp);
        parseIchiran("setagaya", 1, 1, hp, mp);
        hp.outputSurpirses();
        mp.outputSurpirses();

    }
}