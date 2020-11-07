package com.ssadamune.preparse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestParser {

    static void stringJoin() {
        String[] array = {"1", "2", "3"};
        String join = String.join(",", array);
        System.out.println(join);
    }

    /*
     * 私道負担・道路, 諸費用, 建物面積, 土地面積, 建ぺい率・容積率
     * 土地の権利形態, 構造・工法, 用途地域, 地目
     * その他制限事項, その他概要・特記事項
     * view-source:https://suumo.jp/chukoikkodate/tokyo/sc_toshima/nc_94436214/bukkengaiyo/
     *
     * 管理費, 修繕積立金, 修繕積立基金, 諸費用, 専有面積, その他面積
     * 所在階, 構造・階建て, 敷地面積, 敷地の権利形態, 用途地域
     * その他概要・特記事項
     * view-source:https://suumo.jp/ms/chuko/tokyo/sc_ota/nc_95081028/bukkengaiyo/
     */
    static String parseProperty(String todofuken, int ucCode, String propertyKind) throws IOException {
        String url = propertyKind.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                        : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        Document doc = Jsoup.connect(url).get();
        Elements thtdElements = doc.select("table[summary=表]").eq(0).select("tr > *");
        if (propertyKind.equals("mansion")) thtdElements.addAll(doc.select("table[summary=表]").eq(1).select("tr > *"));

        StringBuffer json = new StringBuffer("{\n" + "    \"ID\" : " + ucCode + ",\n");
        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "諸費用" :
                case "管理費" :
                case "修繕積立金" :
                    json.append("    \"" + curItem + "\" : \"" + expenses(thtd.text()) + "\",\n");
                    break;
                case "建物面積" :
                case "土地面積" :
                case "敷地面積" :
                case "専有面積" :
                    json.append("    \"" + curItem + "\" : \"" + area(thtd.text()) + "\",\n");
                    break;
                case "土地の権利形態" :
                case "敷地の権利形態" :
                    String[] rf = rightForm(thtd.text());
                    json.append("    \"" + curItem + "\" : \"" + rf[0] + "\",\n");
                    json.append("    \"借地期間\" : \"" + rf[1] + "\",\n");
                    break;
                case "建ぺい率・容積率" :
                    float[] bf = buildingCoverageFloorAreaRatio(thtd.text());
                    json.append("    \"建ぺい率\" : \"" + bf[0] + "\",\n");
                    json.append("    \"容積率\" : \"" + bf[1] + "\",\n");
                    break;
                case "私道負担・道路" :
                case "構造・工法" :
                case "用途地域" :
                case "地目" :
                case "構造・階建て" :
                case "所在階" :
                case "その他制限事項" :
                    String text = thtd.text().equals("-")||thtd.text().equals("無") ? "" : thtd.text();
                    if (text.length() > 50) text = text.substring(0, 50);
                    json.append("    \"" + curItem + "\" : \"" + text + "\",\n");
                    break;
                case "その他概要・特記事項" :
                    String[][] notices = notices(thtd.text());
                    json.append("    \"設備\" : " + array2Json(notices[0]) + ",\n");
                    json.append("    \"駐車場\" : " + array2Json(notices[1]) + ",\n");
                    break;
                }
            }
        }
        json.append("}");
        return json.toString();
    }

    // building area & land area
    // 112.86m2 => 112.86
    static String area(String tdText) {
        Matcher m = Pattern.compile("\\D*(\\d{2,}(\\.\\d{1,})?)\\D*").matcher(tdText);
        return m.find() ? m.group(1) : "";
    }

    // building coverage ratio / floor area ratio
    // "60％・160％" => {0.6, 1.6}
    static float[] buildingCoverageFloorAreaRatio(String tdText) {
        Matcher m = Pattern.compile("\\D*(\\d+)％\\D*(\\d+)％\\D*").matcher(tdText);
        float[] bcfa = new float[2];
        if (m.find()) {
            bcfa[0] = (float)Integer.parseInt(m.group(1))/100;
            bcfa[1] = (float)Integer.parseInt(m.group(2))/100;
        }
        return bcfa;
    }

    // 諸費用
    // "バイク置場：1000円／月、駐輪場：200円／月" => 1200
    // "地代：4万1250円／月" => 41250
    static int expenses(String tdText) {
        int expenses = 0;
        Matcher m1 = Pattern.compile("(\\d+)円／月").matcher(tdText);
        while (m1.find()) {
            expenses += Integer.parseInt(m1.group(1));
        }
        Matcher m2 = Pattern.compile("(\\d+)万\\d*円／月").matcher(tdText);
        while (m2.find()) {
            expenses += Integer.parseInt(m2.group(1)) * 10000;
        }
        return expenses;
    }

    // land rights form
    // "賃借権（旧）、借地期間残存14年8ヶ月" => {"賃借権（旧）", "14-8"}
    static String[] rightForm(String tdText) {
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

    // その他制限事項
    static String[] limits(String tdText) {
        // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
        if (tdText.equals("-")||tdText.equals("無")) return null;
        return tdText.split("、|／|■|●|◆|※|・");
    }

    // その他概要・特記事項
    // "担当者：朱 楨鏑、設備：公営水道、本下水、都市ガス、駐車場：車庫"
    //          => [["公営水道", "本下水", "都市ガス"], ["車庫"]]
    static String[][] notices(String text) {
        if (text.equals("-")||text.equals("無")) return new String[][] {{},{}};
        String[][] notices = new String[2][];
        Matcher m1 = Pattern.compile(".*設備：([^：]+)($|(、建築.*)|(、駐車場.*))").matcher(text);
        if (m1.find()) notices[0] = m1.group(1).split("、|・|　");
        Matcher m2 = Pattern.compile(".*駐車場：([^：]+)($|(、建築.*))").matcher(text);
        if (m2.find()) notices[1] = m2.group(1).split("、");
        return notices;
    }

    static String array2Json(String[] array) {
        if (array == null || array.length == 0) return "\"\"";
        StringBuffer jsonArray = new StringBuffer("[");
        for (String str : array) {
            jsonArray.append("\"" + str + "\", ");
        }
        // delete ", " at the end of String
        return jsonArray.substring(0, jsonArray.length() - 2) + "]";
    }

    {
        // 交通
    }

    public static void main(String[] args) throws IOException {
        System.out.println(parseProperty("ota", 95081028, "mansion"));
        System.out.println(parseProperty("hachioji", 90966152, "house"));
    }
}
