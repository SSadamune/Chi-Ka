package com.ssadamune.exercise;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
    static void stringJoin() {
        String[] array = {"1", "2", "3"};
        String join = String.join(",", array);
        System.out.println(join);
    }

    // 私道負担・道路, 諸費用, 建物面積, 土地面積, 建ぺい率・容積率, 入居時期
    // 土地の権利形態, 構造・工法, 用途地域, 地目
    // その他制限事項, その他概要・特記事項
    // view-source:https://suumo.jp/chukoikkodate/tokyo/sc_toshima/nc_94436214/bukkengaiyo/
    static void houseJsoupTest(String todofuken, int ucCode) throws IOException {
        String hsUrl = "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(hsUrl).get();
        Elements thtdElements = doc.select("table[summary=表]").first().select("tr > *");
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                System.out.println("th: " + thtd.children().first().text());
            } else if (thtd.is("td")) {
                System.out.println("td: " + thtd.text());
            }
        }

    }

    /*
    static void houseJsoupTest(String todofuken, int ucCode) throws IOException {
        String hsUrl = "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(hsUrl).get();

        Elements trElements = doc.select("tr");

        for (Element tr : trElements) {
            Elements divText = tr.select("div");
            if (divText.text().contains("建物面積")) {
                System.out.println("建物面積：" + area(tr.select("td").text()));
            } else if (divText.text().contains("土地面積")) {
                System.out.println("土地面積：" + area(tr.select("td").text()));
            }
        }
    }
    */

    static String area(String tdText) {
        String areaPattern = "(\\D*)(\\d{2,}\\.\\d{2,})(\\D*)";
        Matcher m = Pattern.compile(areaPattern).matcher(tdText);
        return m.find() ? m.group(2) : "not found";
    }

    // 管理費, 修繕積立金, 修繕積立基金, 諸費用, 専有面積, その他面積, 入居時期
    // 所在階, 構造・階建て, 敷地面積, 敷地の権利形態, 用途地域
    // その他概要・特記事項
    static void mansionJsoupTest(String todofuken, int ucCode) throws IOException {
        String msUrl = "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(msUrl).get();

        Elements trElements = doc.select("tr");

        for (Element tr : trElements) {
            Elements divText = tr.select("div");

        }
    }

    {
    // 交通
    }

    public static void main(String[] args) throws IOException {
        houseJsoupTest("toshima", 94436214);
//        mansionJsoupTest("toshima", 94332373);
    }
}
