package com.ssadamune.crawler;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;

public class SuumoCrawler {

    // ====================================================
    // field
    // ====================================================

    private Handle handler;
    static Logger log = Logger.getLogger("crawler");

    // ====================================================
    // constructor
    // ====================================================

    public SuumoCrawler(Handle hp) {
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
            new Todofuken(tdfkName, maxHouses, 0).parseAllHouses(handler);
        }
    }

    public void crawlMansion(int maxMansions, String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            new Todofuken(tdfkName, 0, maxMansions).parseAllMansions(handler);
        }
    }

    public void crawlAll(String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            Todofuken tdfk = new Todofuken(tdfkName);
            tdfk.parseAllHouses(handler);
            tdfk.parseAllMansions(handler);
        }
    }

    public void crawlAll(int maxHouses, int maxMansions, String... tdfkList) throws IOException {
        log.info("<Start> Crawler work");
        for (String tdfkName : tdfkList) {
            Todofuken tdfk = new Todofuken(tdfkName, maxHouses, maxMansions);
            tdfk.parseAllHouses(handler);
            tdfk.parseAllMansions(handler);
        }
    }

    // ====================================================
    // private method
    // ====================================================
  

    // ====================================================
    // test
    // ====================================================

    public static void main(String[] args) throws IOException {
        // collectWholeTokyo(new Collector[]{new TableDataCollector(), new
        // FeaturesCollector()});

        SuumoCrawler sc = new SuumoCrawler();
        sc.crawlAll("ome");
        sc.crawlAll(120, 120, "setagaya", "shinjuku");
        sc.crawlHouse(120, "nerima");
    }
}
