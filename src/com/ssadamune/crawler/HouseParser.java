package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import org.jsoup.nodes.Document;

public class HouseParser extends PropertyParser {
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

    public void readIchiran() throws IOException {
        this.ncCodes = getUcList(tdfkName, maxPage);
    }

    public void buildProperty(String url) throws IOException {
        
        // TODO
    }
    
    public void outputSurpirses() {
        // TODO
    }
}
