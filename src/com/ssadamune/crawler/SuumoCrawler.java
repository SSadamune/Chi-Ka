package com.ssadamune.crawler;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SuumoCrawler {

    // ====================================================
    // field
    // ====================================================

    private HandleProperty handler;
    static Logger log = Logger.getLogger("crawler");

    // ====================================================
    // constructor
    // ====================================================

    public SuumoCrawler(HandleProperty hp) {
        this.handler = hp;
    }

    public SuumoCrawler() {
        this.handler = new DbWriter();
    }

    // ====================================================
    // public method
    // ====================================================

    public void crawlHouse(int maxHouses, String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            crawlProperty(new Todofuken(tdfkName, maxHouses, 0), true);
        }
    }

    public void crawlMansion(int maxMansions, String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            crawlProperty(new Todofuken(tdfkName, 0, maxMansions), false);
        }
    }

    public void crawlAll(String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            Todofuken tdfk = new Todofuken(tdfkName);
            crawlProperty(tdfk, true);
            crawlProperty(tdfk, false);
        }
    }

    public void crawlAll(int maxHouses, int maxMansions, String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            Todofuken tdfk = new Todofuken(tdfkName, maxHouses, maxMansions);
            crawlProperty(tdfk, true);
            crawlProperty(tdfk, false);
        }
    }

    // ====================================================
    // private method
    // ====================================================

    private void crawlProperty(Todofuken tdfk, boolean isHouse) throws IOException {
        String propertyType = isHouse ? "house" : "mansion";
        Parser parser = isHouse ? new HouseParser() : new MansionParser();
        Set<Integer> ncSet = isHouse ? tdfk.getAllHouses() : tdfk.getAllMansions();

        int num = 0;
        int maxNum = ncSet.size();

        log.info("Crawler find " + maxNum + " " + propertyType + "s in " + tdfk.getName());
        for (int nc : ncSet) {
            Document doc = readBukkengaiyo(isHouse ? tdfk.houseUrl(nc) : tdfk.mansionUrl(nc));
            Property property = parser.parse(doc);
            if (property == null)
                continue;
            handler.handle(property);
            num += 1;
            if (num % 100 == 0)
                System.out.println(num + "/" + maxNum + " " + propertyType + "s handled");
        }
        parser.outputSurpirses();
        log.info("<Finish> " + num + " " + propertyType + "s in " + tdfk.getName() + " crawled");
    }

    /**
     * read bukkengaiyo-page
     * 
     * @param tdfk         todofuken name
     * @param typeFragment "ms/chuko" for mansion, "chukoikkodate" for house
     * @param ncCode       code of property
     * @return a json Document of current page
     * @throws IOException
     */
    private static Document readBukkengaiyo(String url) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (org.jsoup.HttpStatusException hse) {
            log.warning("HttpStatusException: " + hse.getStatusCode() + ", at: \n" + hse.getUrl());
        }
        return doc;
    }

    // ====================================================
    // test
    // ====================================================

    public static void main(String[] args) throws IOException {
        // collectWholeTokyo(new Collector[]{new TableDataCollector(), new
        // FeaturesCollector()});

        SuumoCrawler sc = new SuumoCrawler();
        // sc.crawlAll("ome");
        // sc.crawlAll(200, 200, "setagaya", "shinjuku");
        sc.crawlHouse(100, "nerima");
        sc.crawlMansion(100, "nerima");
    }
}
