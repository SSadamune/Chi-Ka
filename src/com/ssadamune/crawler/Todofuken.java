package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Todofuken {

    // ====================================================
    // field
    // ====================================================

    private String name;
    private Integer maxHouses;
    private Integer maxMansions;
    private HashSet<Integer> allHouses;
    private HashSet<Integer> allMansions;

    // ====================================================
    // constructor
    // ====================================================

    Todofuken(String name) {
        this.name = name;
    }

    Todofuken(String name, int maxHouses, int maxMansions) {
        this.name = name;
        this.maxHouses = maxHouses;
        this.maxMansions = maxMansions;
    }

    // ====================================================
    // public
    // ====================================================

    public String getName() {
        return name;
    }

    public Set<Integer> getAllHouses() {
        if (allHouses == null)
            this.allHouses = bulidSet(maxHouses, "chukoikkodate");
        return allHouses;

    }

    public Set<Integer> getAllMansions() {
        if (allMansions == null)
            this.allMansions = bulidSet(maxMansions, "ms/chuko");
        return allMansions;
    }

    public String houseUrl(int nc) {
        return "https://suumo.jp/chukoikkodate/tokyo/sc_" + name + "/nc_" + nc + "/bukkengaiyo/";
    }

    public String mansionUrl(int nc) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + name + "/nc_" + nc + "/bukkengaiyo/";
    }

    // ====================================================
    // private method
    // ====================================================

    private HashSet<Integer> bulidSet(Integer maxProperties, String urlFragment) {
        HashSet<Integer> set = new HashSet<>();

        /** regex pattern of link, which included the uc-code */
        String pattern = urlFragment + "/tokyo/sc_" + this.name + "/nc_(\\d*)(/)";

        int curPage = 1;
        int endPage = 0;
        int num = 0;

        do {
            String url = "https://suumo.jp/" + urlFragment + "/tokyo/sc_" + this.name + "/pnz1" + curPage + ".html";
            Document doc = readIchiran(url);

            /** set endPage */
            if (curPage == 1) {
                Elements pages = doc.select("ol[class=pagination-parts]").first().select("li");
                endPage = Integer.parseInt(pages.last().text());
            }

            /** extract nc-code from all links in the document */
            for (Element link : doc.select("a[href]")) {
                String curLink = link.attr("href");
                Matcher m = Pattern.compile(pattern).matcher(curLink);
                if (!m.find() || !set.add(Integer.parseInt(m.group(1))))
                    continue;
                num++;
                if (maxProperties != null && maxProperties <= num) 
                    break;
            }
            curPage++;

        } while (curPage <= endPage && (maxProperties == null || maxProperties > num));

        return set;
    }

    private Document readIchiran(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

}
