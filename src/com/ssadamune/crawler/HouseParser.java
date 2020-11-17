package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import org.jsoup.nodes.Document;

public class HouseParser extends SuumoReader implements PropertyParser {
    private String tdfkName;
    private int maxPage;
    private HashSet<Integer> ncCodes = new HashSet<>();

    protected final String propertyIchiranUrl(String todofuken, int page){
        return "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    public HouseParser(String todofuken, int maxPage) {
        this.tdfkName = todofuken;
        this.maxPage = maxPage;
    }

    public void parse() throws IOException {
        readIchiran();
        for (int nc : this.ncCodes) {
            String url = "https://suumo.jp/chukoikkodate/tokyo/sc_" + this.tdfkName + "/nc_" + nc + "/bukkengaiyo/";
            buildHouse(url); 
        }
    }

    public void outputSurpirses() {
        // TODO
    }

    private void readIchiran() throws IOException {
        this.ncCodes = getUcList(tdfkName, maxPage);
    }

    private void buildHouse(String url) throws IOException {
        
        // TODO
    }
    
}
