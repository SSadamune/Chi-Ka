package com.ssadamune.crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

abstract class SuumoReader {
  protected abstract String ichiranUrl(String todofuken, int page);

  protected HashSet<Integer> getUcList(String todofuken, int endPage) throws IOException {
    // all the uc-code of this todofuken, use HashSet to avoid duplicate values
    HashSet<Integer> ucList = new HashSet<>();

    if (endPage <= 0)
      return ucList;

    // regex pattern of link, which included the uc-code
    String pattern = "(/tokyo/sc_" + todofuken + "/nc_)(\\d*)(/)";

    int curPage = 1;
    while (curPage <= endPage) {
      // parse the html of ichiran-page
      Document doc = Jsoup.connect(ichiranUrl(todofuken, curPage)).get();
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
    return ucList;
  }

  private static int maxPage(Document doc) throws IOException {
    Elements pages = doc.select("ol[class=pagination-parts]").first().select("li");
    return Integer.parseInt(pages.last().text());
  }

  public static void main(String[] args) throws IOException {
    HouseParser hp = new HouseParser("ome", 5);
    hp.parse();
  }
}
