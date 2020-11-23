package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SuumoCrawler {

    // ====================================================
    // field
    // ====================================================

    private HandleProperty handler;

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

    public void crawlHouse(int endPage, String... tdfkList) throws IOException {
        for (String todofuken : tdfkList) {
            crawlProperty(todofuken, endPage, true);
        }
    }

    public void crawlMansion(int endPage, String... tdfkList) throws IOException {
        for (String todofuken : tdfkList) {
            crawlProperty(todofuken, endPage, false);
        }
    }

    public void crawlAll(String... tdfkList) throws IOException {
        for (String todofuken : tdfkList) {
            crawlProperty(todofuken, 999, true);
            crawlProperty(todofuken, 999, false);
        }
    }

    // ====================================================
    // private method
    // ====================================================

    private void crawlProperty(String todofuken, int endPage, boolean isHouse) throws IOException {
        String propertyType = isHouse ? "house" : "mansion";
        String typeFragment = isHouse ? "chukoikkodate" : "ms/chuko";
        Parser parser = isHouse ? new HouseParser() : new MansionParser();

        HashSet<Integer> ncSet = readIchiran(todofuken, typeFragment, endPage);
        int num = 0;
        int max = ncSet.size();
        System.out.println("[Start] Crawler find " + max + " " + propertyType + "s in " + todofuken);
        for (int nc : ncSet) {
            Document doc = readBukkengaiyo(todofuken, typeFragment, nc);
            Property property = parser.parse(doc);
            if (property == null)
                continue;
            handler.handle(property);
            num += 1;
            if (num % 100 == 0)
                System.out.println(num + "/" + max + " " + propertyType + "s handled");
        }
        parser.outputSurpirses();
        System.out.println("[Finish] " + num + " " + propertyType + "s in " + todofuken + " crawled");
    }

    /**
     * read ichiran-page at suumo
     * 
     * @param tdfk    todofuken name
     * @param typeFragment    "ms/chuko" for mansion, "chukoikkodate" for house
     * @param endPage how many pages to read
     * @return a set of nc codes
     * @throws IOException
     */
    private static HashSet<Integer> readIchiran(String tdfk, String typeFragment, int endPage) throws IOException {

        /** all the uc-code of this todofuken, use HashSet to avoid duplicate values */
        HashSet<Integer> ncSet = new HashSet<>();

        if (endPage <= 0)
            return ncSet;

        /** regex pattern of link, which included the uc-code */
        String pattern = typeFragment + "/tokyo/sc_" + tdfk + "/nc_(\\d*)(/)";

        int curPage = 1;
        while (curPage <= endPage) {
            String url = "https://suumo.jp/" + typeFragment + "/tokyo/sc_" + tdfk + "/pnz1" + curPage + ".html";
            Document doc = Jsoup.connect(url).get();

            /** set endPage no more than the maxPage */
            if (curPage == 1)
                endPage = Math.min(endPage, maxPage(doc));

            /** extract nc-code from all links in the document */
            for (Element link : doc.select("a[href]")) {
                String curLink = link.attr("href");
                Matcher m = Pattern.compile(pattern).matcher(curLink);
                if (m.find()) {
                    ncSet.add(Integer.parseInt(m.group(1)));
                }
            }
            curPage++;
        }
        return ncSet;
    }

    private static int maxPage(Document doc) throws IOException {
        Elements pages = doc.select("ol[class=pagination-parts]").first().select("li");
        return Integer.parseInt(pages.last().text());
    }

    /**
     * read bukkengaiyo-page
     * 
     * @param tdfk   todofuken name
     * @param typeFragment   "ms/chuko" for mansion, "chukoikkodate" for house
     * @param ncCode code of property
     * @return a json Document of current page
     * @throws IOException
     */
    private static Document readBukkengaiyo(String tdfk, String typeFragment, int ncCode) throws IOException {
        String url = "https://suumo.jp/" + typeFragment + "/tokyo/sc_" + tdfk + "/nc_" + ncCode + "/bukkengaiyo/";
        Logger log = Logger.getLogger("crawler");
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
        sc.crawlAll("ome");
        sc.crawlHouse(3, "setagaya");

    }
}
