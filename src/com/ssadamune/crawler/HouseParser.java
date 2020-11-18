package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HouseParser extends SuumoReader implements ParseProperty {
    private String tdfkName;
    private int maxPage;
    private HashSet<Integer> ncCodes = new HashSet<>();
    private Logger log = Logger.getLogger("parseHouse");

    public HouseParser(String todofuken, int maxPage) {
        this.tdfkName = todofuken;
        this.maxPage = maxPage;
    }

    public void parse() throws IOException {
        readIchiran();
        int num = 0;
        int max = this.ncCodes.size();
        log.info(max + " houses found in " + tdfkName);
        for (int nc : this.ncCodes) {
            String url = "https://suumo.jp/chukoikkodate/tokyo/sc_" + this.tdfkName + "/nc_" + nc + "/bukkengaiyo/";
            buildHouse(url); 
            num += 1;
            if (num % 100 == 0) log.info(num + "/" + max + " houses parsed");
        }
        log.info(max + " houses in " + tdfkName + " completed");
    }

    public void outputSurpirses() {
        // TODO
    }

    protected final String ichiranUrl(String todofuken, int page){
        return "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    private void readIchiran() throws IOException {
        this.ncCodes = getUcList(tdfkName, maxPage);
    }

    private void buildHouse(String url) throws IOException {
        House curHouse = new House(); 
        try {
            Document doc = Jsoup.connect(url).get();
        } catch (org.jsoup.HttpStatusException hse) {
            log.warning(
                "HttpStatusException: " + 
                hse.getStatusCode() +
                ", at: \n" + 
                hse.getUrl());
        }

        // TODO
    }
    
}
