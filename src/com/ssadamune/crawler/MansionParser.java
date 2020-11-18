package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import org.jsoup.nodes.Document;

public class MansionParser extends SuumoReader implements ParseProperty {
    private String tdfkName;
    private int maxPage;
    private HashSet<Integer> ncCodes = new HashSet<>();

    public MansionParser(String todofuken, int maxPage) {
        this.tdfkName = todofuken;
        this.maxPage = maxPage;
    }

    public void parse() throws IOException {
        readIchiran();
        for (int nc : this.ncCodes) {
            String url = "https://suumo.jp/chukoikkodate/tokyo/sc_" + this.tdfkName + "/nc_" + nc + "/bukkengaiyo/";
            buildMansion(url); 
        }
    }

    public void outputSurpirses() {
        // TODO
    }

    protected final String ichiranUrl(String todofuken, int page) {
        return "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/pnz1" + page + ".html";
    }

    private void readIchiran() throws IOException {
        this.ncCodes = getUcList(tdfkName, maxPage);
    }
    
    private void buildMansion(String url) throws IOException {
        
        // TODO
    }
    
}
