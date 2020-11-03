package com.ssadamune.exercise;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ssadamune.modular.SuumoParser;

public class Test {
    static void stringJoin() {
        String[] array = {"1", "2", "3"};
        String join = String.join(",", array);
        System.out.println(join);
    }

    // 私道負担・道路, 諸費用, 建物面積, 土地面積, 建ぺい率・容積率
    // 土地の権利形態, 構造・工法, 用途地域, 地目
    // その他制限事項, その他概要・特記事項
    // view-source:https://suumo.jp/chukoikkodate/tokyo/sc_toshima/nc_94436214/bukkengaiyo/
    static String houseJsoupTest(String todofuken, int ucCode) throws IOException {
        String hsUrl = "https://suumo.jp/chukoikkodate/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(hsUrl).get();
        Elements thtdElements = doc.select("table[summary=表]").first().select("tr > *");
        StringBuffer json = new StringBuffer("{\n" + "    \"ID\" : " + ucCode + ",\n");
        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "私道負担・道路" :
                    String road = thtd.text().equals("-")||thtd.text().equals("無") ? "" : thtd.text();
                    json.append("    \"" + curItem + "\" : \"" + road + "\",\n");
                    break;
                case "諸費用" :
                    json.append("    \"" + curItem + "\" : \"" + thtd.text() + "\",\n");
                    json.append("    \"諸費用合計\" : \"" + expenses(thtd.text()) + "\",\n");
                    break;
                case "建物面積" :
                case "土地面積" :
                    json.append("    \"" + curItem + "\" : \"" + area(thtd.text()) + "\",\n");
                    break;
                case "建ぺい率・容積率" :
                    float[] bf = buildingCoverageFloorAreaRatio(thtd.text());
                    json.append("    \"建ぺい率\" : \"" + bf[0] + "\",\n");
                    json.append("    \"容積率\" : \"" + bf[1] + "\",\n");
                    break;
                case "土地の権利形態" :
                    String[] rf = rightForm(thtd.text());
                    json.append("    \"土地の権利形態\" : \"" + rf[0] + "\",\n");
                    json.append("    \"借地期間\" : \"" + rf[1] + "\",\n");
                    break;
                }
            }
        }
        json.append("}");
        System.out.println(json);
        return json.toString();
    }

    // building area & land area
    static String area(String tdText) {
        // 112.86m2
        Matcher m = Pattern.compile("(\\D*)(\\d{2,}(\\.\\d{1,})?)(\\D*)").matcher(tdText);
        return m.find() ? m.group(2) : "";
    }

    // building coverage ratio / floor area ratio
    static float[] buildingCoverageFloorAreaRatio(String tdText) {
        // "60％・160％"
        Matcher m = Pattern.compile("(\\D*)(\\d+)(％\\D*)(\\d+)(％\\D*)").matcher(tdText);
        float[] bcfa = new float[2];
        if (m.find()) {
            bcfa[0] = (float)Integer.parseInt(m.group(2))/100;
            bcfa[1] = (float)Integer.parseInt(m.group(4))/100;
        }
        return bcfa;
    }

    // land rights form
    static String[] rightForm(String tdText) {
        // "賃借権（旧）、借地期間残存14年8ヶ月"
        boolean isLeaseRight = false;
        String rightForm;
        String landLeasePeriod = new String();
        if (tdText.contains("所有権")) {
            rightForm = "所有権";
        } else if (tdText.contains("賃借権（旧）")) {
            rightForm = "賃借権（旧）";
            isLeaseRight = true;
        } else if (tdText.contains("賃借権（新）")) {
            rightForm = "賃借権（新）";
            isLeaseRight = true;
        } else {
            rightForm = "そのほか";
        }
        if (isLeaseRight) {
            Matcher m = Pattern.compile("\\D*((\\d+)年)?((\\d+)ヶ月)?").matcher(tdText);
            if (m.find()) {
                String year = (m.group(2) == null) ? "0" : m.group(2);
                String month = (m.group(4) == null) ? "0" : m.group(4);
                landLeasePeriod = year + "-" + month;
            }
        }
        return new String[]{rightForm, landLeasePeriod};
    }

    // 諸費用
    static int expenses(String tdText) {
        // "バイク置場：1000円／月、駐輪場：200円／月"
        // "地代：4万1250円／月"
        int expenses = 0;
        Matcher m1 = Pattern.compile("(\\d+)円／月").matcher(tdText);
        while (m1.find()) {
            expenses += Integer.parseInt(m1.group(1));
        }
        Matcher m2 = Pattern.compile("((\\d+)万)+(\\d*)円／月").matcher(tdText);
        while (m2.find()) {
            expenses += Integer.parseInt(m2.group(2)) * 10000;
        }
        return expenses;
    }

    // 管理費, 修繕積立金, 修繕積立基金, 諸費用, 専有面積, その他面積
    // 所在階, 構造・階建て, 敷地面積, 敷地の権利形態, 用途地域
    // その他概要・特記事項
    static String mansionJsoupTest(String todofuken, int ucCode) throws IOException {
        String msUrl = "https://suumo.jp/ms/chuko/tokyo/sc_" + todofuken + "/nc_" + ucCode + "/bukkengaiyo/";
        Document doc = Jsoup.connect(msUrl).get();

        Elements thtdElements = doc.select("table[summary=表]").eq(0).select("tr > *");
        thtdElements.addAll(doc.select("table[summary=表]").eq(1).select("tr > *"));
        StringBuffer json = new StringBuffer("{\n" + "    \"ID\" : " + ucCode + ",\n");
        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "諸費用" :
                    json.append("    \"" + curItem + "\" : \"" + thtd.text() + "\",\n");
                    json.append("    \"諸費用合計\" : \"" + expenses(thtd.text()) + "\",\n");
                    break;
                case "敷地面積" :
                    json.append("    \"" + curItem + "\" : \"" + area(thtd.text()) + "\",\n");
                    break;
                case "敷地の権利形態" :
                    String[] rf = rightForm(thtd.text());
                    json.append("    \"土地の権利形態\" : \"" + rf[0] + "\",\n");
                    json.append("    \"借地期間\" : \"" + rf[1] + "\",\n");
                    break;
                }
            }
        }
        json.append("}");
        System.out.println(json);
        return json.toString();
    }

    {
    // 交通
    }

    public static void main(String[] args) throws IOException {
        var houseCodes = SuumoParser.getHousesUcList("setagaya", 3);
        for (int nc : houseCodes) {
            houseJsoupTest("setagaya", nc);
        }
        var mansionCodes = SuumoParser.getMansionsUcList("setagaya", 1);
        for (int nc : mansionCodes) {
            mansionJsoupTest("setagaya", nc);
        }
    }
}
