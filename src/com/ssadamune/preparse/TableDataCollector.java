package com.ssadamune.preparse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * all files of this package are useless for the Finished project
 * this file was made to enumerate some table data of properties
 */

class TableDataCollector implements Collector{

    static HashMap<String, String> structure = new HashMap<>();
    static HashMap<String, String> floor = new HashMap<>();
    static HashMap<String, String> constMethod = new HashMap<>();

    static HashMap<String, String> repairFund = new HashMap<>();
    static HashMap<String, String> madori = new HashMap<>();
    static HashMap<String, String> otherArea = new HashMap<>();

    // static HashMap<String, String> NoticeSorts = new HashMap<String, String>();
    static HashMap<String, String> limits = new HashMap<>();
    static HashMap<String, String> facility = new HashMap<>();
    static HashMap<String, String> parking = new HashMap<>();

    // 「構造・階建て」を解析
    // "RC55階地下2階建一部鉄骨" => {{"RC", "一部鉄骨"}, {"[55, -2]"}}
    // "RC造一部鉄骨造・地上5階　地下1階" => {{"RC", "一部鉄骨"}, {"[5, -1]"}}
    // "ＳＲＣ・ＲＣ・鉄骨12階建" => {{"ＳＲＣ", "ＲＣ", "鉄骨"}, {"[12, 0]"}}
    private static String[][] structureFloor (String text) {
        String strucs = "";
        String floorUp = "";
        String floorDown = "0";

        Matcher mStruc = Pattern.compile("(^[^\\d地一]*)").matcher(text);
        if (mStruc.find()) strucs = mStruc.group(1);
        String[] sturcArr = strucs.replace("造", "").replace("　", "").split("、|・|\\+|＋");
        Matcher mPart = Pattern.compile(".*(一部[^地]*)").matcher(text);
        if (mPart.find()) sturcArr = add2Arr(sturcArr, mPart.group(1)
                .replace("造", "").replace("　", "").replace("・", "").replace("）", "").replace("（", ""));

        Matcher mUp = Pattern.compile("[^\\d下]*(\\d+)階").matcher(text);
        Matcher mDown = Pattern.compile("地下(\\d+)階").matcher(text);
        if (mUp.find()) floorUp = mUp.group(1);
        if (mDown.find()) floorDown = "-" + mDown.group(1);
        String[] floorUpDown = {"[" + floorUp + ", " + floorDown + "]"};

        return new String[][] {sturcArr, floorUpDown};
    }

    // 「その他制限事項」を解析
    // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
    static String[] limitMatters(String text) {
        if (text.equals("-")||text.equals("無")) return new String[]{};
        return text.split("、|／|■|●|◆|※|・");
    }

    // 「その他面積」を分類
    // "バルコニー面積：35.15m2、ルーフバルコニー：35.15m2（使用料無）"
    //          => ["バルコニー面積", "ルーフバルコニー"]
    static String[] sortOtherArea(String text) {
        if (text.equals("-")||text.equals("無")) return new String[]{};
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(text);
        ArrayList<String> areaArr = new ArrayList<>();
        while (m.find()) {
            areaArr.add(m.group(1));
        }
        String[] otherAreas = new String[areaArr.size()];
        return areaArr.toArray(otherAreas);
    }


    // 「その他概要・特記事項」を分類
    // "担当者：XXX、設備：公営水道、本下水、都市ガス、駐車場：車庫"
    //          => ["担当者", "設備", "駐車場"]
    /*
    static String[] sortNotices(String text) {
        if (text.equals("-")||text.equals("無")) return null;
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(text);
        ArrayList<String> noticeArr = new ArrayList<String>();
        while (m.find()) {
            noticeArr.add(m.group(1));
        }
        String[] notices = new String[noticeArr.size()];
        return noticeArr.toArray(notices);
     */

    // 「その他概要・特記事項」を解析
    // "担当者：XXX、設備：公営水道、本下水、都市ガス、駐車場：車庫"
    //          => {{"公営水道", "本下水", "都市ガス"}, {"車庫"}}
    static String[][] notices(String text) {
        if (text.equals("-")||text.equals("無")) return new String[][] {{},{}};
        String[][] notices = new String[2][];
        Matcher m1 = Pattern.compile(".*設備：([^：]+)($|(、建築.*)|(、駐車場.*))").matcher(text);
        if (m1.find()) notices[0] = m1.group(1).split("、|・|　");
        Matcher m2 = Pattern.compile(".*駐車場：([^：]+)($|(、建築.*))").matcher(text);
        if (m2.find()) notices[1] = m2.group(1).split("、");
        return notices;
    }

    // ===================================================================
    private static String printMap (HashMap<String, String> map) {
        StringBuilder str = new StringBuilder("{\n");
        map.forEach((m, p) -> str.append("    \"" + m + "\" : \"" + p + "\",\n"));
        str.append("}");
        return str.toString();
    }

    private static String[] add2Arr(String[] arr, String... strings){
        String[] tempArr = new String[arr.length + strings.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for(int i=0; i < strings.length; i++)
            tempArr[arr.length+i] = strings[i];
        return tempArr;

    }

    // add all the items from String array to HashMap
    private static void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0) return;
        for (String item : items) {
            if (!item.isBlank()) map.putIfAbsent(item.trim(), property);
        }
    }

    // ===================================================================
    @Override
    public void collect(Document doc, String url, String propertyKind) {
        // TODO Auto-generated method stub
        Elements thtdElements = doc.select("table[summary=表]").eq(0).select("tr > *");
        if (propertyKind.equals("mansion")) {
            thtdElements.addAll(doc.select("table[summary=表]").eq(1).select("tr > *"));
        }
        String curItem = "";
        for (Element thtd : thtdElements) {
            if (thtd.is("th")) {
                curItem = thtd.children().first().text();
            } else if (thtd.is("td")) {
                switch (curItem) {
                case "構造・階建て" :
                    String[][] sf = structureFloor(thtd.text());
                    add2Map(structure, sf[0], url);
                    floor.putIfAbsent(sf[1][0], url);
                    break;
                case "構造・工法" :
                    constMethod.putIfAbsent(thtd.text(), url);
                    break;
                case "修繕積立基金" :
                    repairFund.putIfAbsent(thtd.text(), url);
                    break;
                case "間取り" :
                    madori.putIfAbsent(thtd.text(), url);
                    break;
                case "その他面積" :
                    add2Map(otherArea, sortOtherArea(thtd.text()), url);
                    break;
                case "その他制限事項" :
                    add2Map(limits, limitMatters(thtd.text()), url);
                    break;
                case "その他概要・特記事項" :
                    // add2Map(NoticeSorts, sortNotices(thtd.text()), url);
                    String[][] notices = notices(thtd.text());
                    add2Map(facility, notices[0], url);
                    add2Map(parking, notices[1], url);
                    break;
                default:
                    break;
                }
            }
        }
    }

    public void output(String dirLoc) throws IOException{
        String createFailedInfo = "create file failed";
        Logger logger = Logger.getLogger("LoggingDemo");
        File logFile = new File( dirLoc + "\\Structure.json");
        if(!logFile.createNewFile()) logger.info(createFailedInfo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()))) {
            bw.write("{\"マンション　構造\" : " + printMap(structure) + ",\n");
            bw.write("\"マンション　階建て\" : " + printMap(floor) + ",\n");
            bw.write("\"一戸建て構造・工法\" : " + printMap(constMethod) + "}");
            logger.info("Structure.log created SUCCESSFULLY!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        logFile = new File( dirLoc + "\\FundMadoriArea.json");
        if(!logFile.createNewFile()) logger.info(createFailedInfo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()))) {
            bw.write("{\"修繕積立基金\" : " + printMap(repairFund) + ",\n");
            bw.write("\"間取り\" : " + printMap(madori) + ",\n");
            bw.write("\"その他面積\" : " + printMap(otherArea) + "}");
            logger.info("Information.log created SUCCESSFULLY!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        logFile = new File( dirLoc + "\\FacilityParkingLimits.json");
        if(!logFile.createNewFile()) logger.info(createFailedInfo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()))) {
            bw.write("{\"設備\" : " + printMap(facility) + ",\n");
            bw.write("\"駐車場\" : " + printMap(parking) + ",\n");
            bw.write("\"制限事項\" : " + printMap(limits) + "}");
            logger.info("Matters.log created SUCCESSFULLY!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String output() throws IOException{
        String now = new SimpleDateFormat ("yyyyMMdd_HHmmss").format(new Date( ));
        String dirLoc = "log\\Enumerate\\" + now;
        new File(dirLoc).mkdirs();
        output(dirLoc);
        return dirLoc;
    }
}