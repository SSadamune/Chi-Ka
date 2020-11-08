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

public class EnumerateTableData {

    static HashMap<String, String> Structure = new HashMap<String, String>();
    static HashMap<String, String> Floor = new HashMap<String, String>();
    static HashMap<String, String> ConstMethod = new HashMap<String, String>();

    static HashMap<String, String> RepairFund = new HashMap<String, String>();
    static HashMap<String, String> Madori = new HashMap<String, String>();
    static HashMap<String, String> OtherArea = new HashMap<String, String>();

    // static HashMap<String, String> Notices = new HashMap<String, String>();
    static HashMap<String, String> Limits = new HashMap<String, String>();
    static HashMap<String, String> Facility = new HashMap<String, String>();
    static HashMap<String, String> Parking = new HashMap<String, String>();

    // ================================================================================================
    // parse houses or mansions
    static void parseProperty(String todofuken, int ucCode, String propertyKind) throws IOException {
        String url = propertyKind.equals("house")
                ? "https://suumo.jp/chukoikkodate/tokyo/sc_"
                : "https://suumo.jp/ms/chuko/tokyo/sc_";
        url += todofuken + "/nc_" + ucCode + "/bukkengaiyo/";

        Document doc = Jsoup.connect(url).get();
        Elements thtdElements = doc.select("table[summary=表]").eq(0).select("tr > *");
        if (propertyKind.equals("mansion")) thtdElements.addAll(doc.select("table[summary=表]").eq(1).select("tr > *"));

        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "構造・階建て" :
                    String[] sf = structureFloor(thtd.text());
                    Structure.putIfAbsent(sf[0], url);
                    Floor.putIfAbsent(sf[1], url);
                    break;
                case "構造・工法" :
                    ConstMethod.putIfAbsent(thtd.text(), url);
                    break;
                case "修繕積立基金" :
                    RepairFund.putIfAbsent(thtd.text(), url);
                    break;
                case "間取り" :
                    Madori.putIfAbsent(thtd.text(), url);
                    break;
                case "その他面積" :
                    add2Map(OtherArea, sortOtherArea(thtd.text()), url);
                    break;
                case "その他制限事項" :
                    add2Map(Limits, limits(thtd.text()), url);
                    break;
                case "その他概要・特記事項" :
                    // add2Map(Notices, sortNotices(thtd.text()), url);
                    String[][] notices = notices(thtd.text());
                    add2Map(Facility, notices[0], url);
                    add2Map(Parking, notices[1], url);
                    break;
                }
            }
        }
    }

    // ================================================================================================
    private static String[] structureFloor (String text) {
        String struc = "";
        String floor = "";
        Matcher m = Pattern.compile("(^\\D*)(\\d+.*$)").matcher(text);
        if (m.find()) {
            struc = m.group(1);
            floor = m.group(2);
        }
        return new String[] {struc, floor};
    }

    // 「その他制限事項」を解析
    // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
    static String[] limits(String text) {
        if (text.equals("-")||text.equals("無")) return null;
        return text.split("、|／|■|●|◆|※|・");
    }

    // 「その他面積」を分類
    // "バルコニー面積：35.15m2、ルーフバルコニー：35.15m2（使用料無）"
    //          => ["バルコニー面積", "ルーフバルコニー"]
    static String[] sortOtherArea(String text) {
        if (text.equals("-")||text.equals("無")) return null;
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(text);
        ArrayList<String> areaArr = new ArrayList<String>();
        while (m.find()) {
            areaArr.add(m.group(1));
        }
        String[] otherAreas = new String[areaArr.size()];
        return areaArr.toArray(otherAreas);
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

    private static String printMap (HashMap<String, String> map) {
        StringBuffer str = new StringBuffer("{\n");
        map.forEach((m, p) -> {
            str.append("    \"" + m + "\" : " + p + "\n");
        });
        str.append("}\n");
        return str.toString();
    }

    // ================================================================================================
    // add all the items from String array to HashMap
    private static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            if (item.isBlank()==false) map.putIfAbsent(item.trim(), property);
        }
    }

    // ================================================================================================

    private static void writeLog() throws IOException{
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_hhmmss");
        File logFile = new File("C:\\Users\\zwieb\\Documents\\MDproject\\MDproject\\log\\Enumerate\\"
                + "Structure_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("マンション　構造 : " + printMap(Structure));
        bw.write("マンション　階建て : " + printMap(Floor));
        bw.write("一戸建て構造・工法 : " + printMap(ConstMethod));

        bw.close();
        System.out.println("Structure 文件创建成功！");

        logFile = new File("C:\\Users\\zwieb\\Documents\\MDproject\\MDproject\\log\\Enumerate\\"
                + "Information_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("修繕積立基金 : " + printMap(RepairFund));
        bw.write("間取り : " + printMap(Madori));
        bw.write("その他面積 : " + printMap(OtherArea));
        bw.close();
        System.out.println("Information 文件创建成功！");

        logFile = new File("C:\\Users\\zwieb\\Documents\\MDproject\\MDproject\\log\\Enumerate\\"
                + "Matters_" + ft.format(dNow) + ".txt");
        logFile.createNewFile();
        bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()));
        bw.write("設備 : " + printMap(Facility));
        bw.write("駐車場 : " + printMap(Parking));
        bw.write("制限事項 : " + printMap(Limits));
        bw.close();
        System.out.println("Matters 文件创建成功！");

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

    public static void main(String[] args) throws IOException {
        parseTodofuken("nerima", 13, 26);
        parseTodofuken("shinjuku", 6, 46);
        parseTodofuken("musashino", 3, 7);
        parseTodofuken("hachioji", 17, 15);
        parseTodofuken("edogawa", 10, 20);
        parseTodofuken("setagaya", 20, 50);
        writeLog();
    }

}
