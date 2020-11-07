package com.ssadamune.preparse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ssadamune.crawler.SuumoParser;

/*
 * all files of this package are useless for the Finished project
 * this file was made to enumerate all the 「その他事項」
 */

public class EnumerateOtherMatters {

    static HashMap<String, String> Limits = new HashMap<String, String>();
    static HashMap<String, String> Notices = new HashMap<String, String>();
    static HashMap<String, String> Facility = new HashMap<String, String>();
    static HashMap<String, String> Parking = new HashMap<String, String>();

    // 「その他制限事項」を解析
    // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
    static String[] limits(String text) {
        if (text.equals("-")||text.equals("無")) return null;
        return text.split("、|／|■|●|◆|※|・");
    }

    // 「その他概要・特記事項」を分類
    // "担当者：XXX、設備：公営水道、本下水、都市ガス、駐車場：車庫"
    //          => ["担当者", "設備", "駐車場"]
    static String[] sortNotices(String text) {
        if (text.equals("-")||text.equals("無")) return null;
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(text);
        ArrayList<String> noticeArr = new ArrayList<String>();
        while (m.find()) {
            noticeArr.add(m.group(1));
        }
        String[] notices = new String[noticeArr.size()];
        return noticeArr.toArray(notices);
    }

    // 「その他概要・特記事項」を解析
    // "担当者：XXX、設備：公営水道、本下水、都市ガス、駐車場：車庫"
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

    // parse houses or mansions
    static void parseProperty(String todofuken, int ucCode, String propertyKind) throws IOException {
        String url = propertyKind.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        Document doc = Jsoup.connect(url).get();
        Elements thtdElements = doc.select("table[summary=表]").first().select("tr > *");
        String curItem = "";
        String propertyForMap = "[\"" + propertyKind + "\", \"" + todofuken + "\", \"" + ucCode + "\"]";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "その他制限事項" :
                    add2Map(Limits, limits(thtd.text()), propertyForMap);
                    break;
                case "その他概要・特記事項" :
                    add2Map(Notices, sortNotices(thtd.text()), propertyForMap);
                    String[][] notices = notices(thtd.text());
                    add2Map(Facility, notices[0], propertyForMap);
                    add2Map(Parking, notices[1], propertyForMap);
                    break;
                }
            }
        }
    }

    // add all the items from String array to HashMap
    private static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            if (item.isBlank()==false) map.putIfAbsent(item.trim(), property);
        }
    }

    // parse the todofuken ichiran page, save the properties into maps
    static void parseTodofuken (String tdfk, int maxHousePages, int maxMansionPages) throws IOException {
        var houseCodes = SuumoParser.getHousesUcList(tdfk, maxHousePages); //20
        var mansionCodes = SuumoParser.getMansionsUcList(tdfk, maxMansionPages); //50
        int houseNum = houseCodes.size();
        int mansionNum = mansionCodes.size();
        System.out.println(houseNum + " houses and " + mansionNum + " mansions found in " + tdfk);

        int properties = 0;
        for (int nc : houseCodes) {
            parseProperty(tdfk, nc, "house");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ houseNum + " houses parsed");
        }
        System.out.println(houseNum + " houses completed in " + tdfk);

        properties = 0;
        for (int nc : mansionCodes) {
            parseProperty(tdfk, nc, "mansion");
            properties += 1;
            if (properties % 100 == 0) System.out.println(properties +"/"+ mansionNum + " mansions parsed");
        }
        System.out.println(mansionNum + " mansions completed in " + tdfk);
        System.out.println("=====================");
    }

    private static String printMap (HashMap<String, String> map) {
        StringBuffer str = new StringBuffer("{\n");
        map.forEach((m, p) -> {
            str.append("    \"" + m + "\" : " + p + "\n");
        });
        str.append("}\n");
        return str.toString();
    }

    private static void writeLog() throws IOException{
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
        File logFile = new File("C:\\Users\\zwieb\\Documents\\MDproject\\MDproject\\log\\"
                + "EnumerateMatters_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("Notices : " + printMap(Notices));
        bw.write("Facility : " + printMap(Facility));
        bw.write("Parking : " + printMap(Parking));
        bw.write("Limits : " + printMap(Limits));
        bw.close();
        System.out.println("文件创建成功！");
    }

    public static void main(String[] args) throws IOException {
        parseTodofuken("hachioji", 17, 15);
        parseTodofuken("edogawa", 10, 20);
        parseTodofuken("setagaya", 20, 50);
        System.out.println(printMap(Facility));
        writeLog();
    }

}
