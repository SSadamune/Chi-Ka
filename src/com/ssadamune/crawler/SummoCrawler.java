package com.ssadamune.crawler;

import java.io.IOException;

public class SummoCrawler {

    // ==============================================
    // field
    // ==============================================

    private HandleProperty handler;

    // ==============================================
    // constructor
    // ==============================================

    public SummoCrawler(HandleProperty hp) {
        this.handler = hp;
    }

    public SummoCrawler() {
        this.handler = new DbWriter();
    }

    // ==============================================
    // public method
    // ==============================================

    public void crawlHouse(String todofuken, int maxPage) throws IOException {
        IchiranPage ip = new IchiranPage(todofuken, maxPage, "house");
        HouseParser parser = new HouseParser();
        for (int uc : ip.getUcList()) {
            House house = new House();
            /**
             * TODO: house = parser.parse(doc);
             */
            handler.handle(house);
        }
        parser.outputSurpirses();
    }

    public void crawlMansion(String todofuken, int maxPage) {
        /** */
    }

    public void crawlAll(String... todofukens) throws IOException {
        for (String todofuken : todofukens) {
            crawlHouse(todofuken, 999);
            crawlMansion(todofuken, 999);
        }
    }

    // ==============================================
    // private method
    // ==============================================
}
