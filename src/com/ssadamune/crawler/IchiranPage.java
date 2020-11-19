package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IchiranPage {
    private HashSet<Integer> ucList = new HashSet<>();

    public HashSet<Integer> getUcList() {
        return this.ucList;
    }

    public IchiranPage(String todofuken, int endPage, String kind) throws IOException {
        if (endPage <= 0)
            return;

        // regex pattern of link, which included the uc-code
        String pattern = "(/tokyo/sc_" + todofuken + "/nc_)(\\d*)(/)";

        int curPage = 1;
        while (curPage <= endPage) {
            String url = "";
            switch (kind) {
                case "house":
                    url = "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/pnz1" + curPage + ".html";
                    break;
                case "mansion":
                    url = "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/pnz1" + curPage + ".html";
                    break;
                default:
            }

            // parse the html of ichiran-page
            Document doc = Jsoup.connect(url).get();
            if (curPage == 1)
                endPage = Math.min(endPage, maxPage(doc));

            // all the links in ichiran-page
            Elements links = doc.select("a[href]");

            for (Element link : links) {
                String curLink = link.attr("href");
                // match current-link and regex-pattern
                Matcher m = Pattern.compile(pattern).matcher(curLink);
                if (m.find()) {
                    // add uc-code into set
                    ucList.add(Integer.parseInt(m.group(2)));
                }
            }
            curPage++;
        }
    }

    private static int maxPage(Document doc) throws IOException {
        Elements pages = doc.select("ol[class=pagination-parts]").first().select("li");
        return Integer.parseInt(pages.last().text());
    }
}
