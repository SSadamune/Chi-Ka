package com.ssadamune.crawler;

import java.io.IOException;

public class SummoCrawler {

    // ==============================================
    // field
    // ==============================================

    private HandleProperty hp;

    // ==============================================
    // constructor
    // ==============================================

    public SummoCrawler(HandleProperty hp) {
        this.hp = hp;
    }

    public SummoCrawler() {
        this.hp = new DbWriter();
    }

    // ==============================================
    // public method
    // ==============================================

    public void crawlHouse(String todofuken, int maxPage) throws IOException {
        IchiranPage ip = new IchiranPage(todofuken, maxPage, "house");
        for (int uc : ip.getUcList()) {
            House house = new House();
            /**
             * TODO: house = parseHouse();
             */
            hp.handle(house);
        }
        hp.outputSurpirses();
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
