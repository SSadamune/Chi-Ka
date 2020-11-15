package com.ssadamune.preparse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.ssadamune.utils.DirectoryPath;

/*
 * all files of this package are useless for the Finished project
 * this file was made to enumerate some table data of properties
 */

class TableDataCollector extends Collector {

    StructFloorMethod sfm = new StructFloorMethod();
    FundMadoriArea fma = new FundMadoriArea();
    LimitFacilityParking lfp = new LimitFacilityParking();

    // 「構造・階建て」を解析
    // "RC55階地下2階建一部鉄骨" => {{"RC", "一部鉄骨"}, {"[55, -2]"}}
    // "RC造一部鉄骨造・地上5階 地下1階" => {{"RC", "一部鉄骨"}, {"[5, -1]"}}
    // "ＳＲＣ・ＲＣ・鉄骨12階建" => {{"ＳＲＣ", "ＲＣ", "鉄骨"}, {"[12, 0]"}}
    private String[][] structureFloor(String text) {
        String strucs = "";
        String floorUp = "";
        String floorDown = "0";

        Matcher mStruc = Pattern.compile("(^[^\\d地一]*)").matcher(text);
        if (mStruc.find())
            strucs = mStruc.group(1);
        String[] sturcArr = strucs.replaceAll("[造　）（]", "").split("、|・|\\+|＋");
        Matcher mPart = Pattern.compile(".*(一部[^\\d地]*)").matcher(text);
        if (mPart.find())
            sturcArr = add2Arr(sturcArr, mPart.group(1).replaceAll("[造　・）（]", ""));

        Matcher mUp = Pattern.compile("[^\\d下]*(\\d+)階").matcher(text);
        Matcher mDown = Pattern.compile("地下(\\d+)階").matcher(text);
        if (mUp.find())
            floorUp = mUp.group(1);
        if (mDown.find())
            floorDown = "-" + mDown.group(1);
        String[] floorUpDown = { "[" + floorUp + ", " + floorDown + "]" };

        return new String[][] { sturcArr, floorUpDown };
    }

    // 「その他制限事項」を解析
    // "高度地区、準防火地域、風致地区、景観地区、日影制限有"
    static String[] limitMatters(String text) {
        if (text.equals("-") || text.equals("無"))
            return new String[] {};
        return text.split("、|／|■|●|◆|※|・");
    }

    // 「その他面積」を分類
    // "バルコニー面積：35.15m2、ルーフバルコニー：35.15m2（使用料無）"
    // => ["バルコニー面積", "ルーフバルコニー"]
    static String[] sortOtherArea(String text) {
        if (text.equals("-") || text.equals("無"))
            return new String[] {};
        Matcher m = Pattern.compile("(?:^|、)([^、：]*)：").matcher(text);
        ArrayList<String> areaArr = new ArrayList<>();
        while (m.find()) {
            areaArr.add(m.group(1));
        }
        String[] otherAreas = new String[areaArr.size()];
        return areaArr.toArray(otherAreas);
    }

    static String[][] notices(String text) {
        if (text.equals("-") || text.equals("無"))
            return new String[][] { {}, {} };
        String[][] notices = new String[2][];
        Matcher m1 = Pattern.compile(".*設備：([^：]+)($|(、建築.*)|(、駐車場.*))").matcher(text);
        if (m1.find())
            notices[0] = m1.group(1).split("、|・|　");
        Matcher m2 = Pattern.compile(".*駐車場：([^：]+)($|(、建築.*))").matcher(text);
        if (m2.find())
            notices[1] = m2.group(1).split("、");
        return notices;
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
                    case "構造・階建て":
                        String[][] structure = structureFloor(thtd.text());
                        add2Map(sfm.structure, structure[0], url);
                        sfm.floor.putIfAbsent(structure[1][0], url);
                        break;
                    case "構造・工法":
                        sfm.constMethod.putIfAbsent(thtd.text(), url);
                        break;
                    case "修繕積立基金":
                        fma.repairFund.putIfAbsent(thtd.text(), url);
                        break;
                    case "間取り":
                        fma.madori.putIfAbsent(thtd.text(), url);
                        break;
                    case "その他面積":
                        add2Map(fma.otherArea, sortOtherArea(thtd.text()), url);
                        break;
                    case "その他制限事項":
                        add2Map(lfp.limits, limitMatters(thtd.text()), url);
                        break;
                    case "その他概要・特記事項":
                        // add2Map(NoticeSorts, sortNotices(thtd.text()), url);
                        String[][] notices = notices(thtd.text());
                        add2Map(lfp.facility, notices[0], url);
                        add2Map(lfp.parking, notices[1], url);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void writefile(String path, UnexpectedTableData... tableData) throws IOException {

        Logger log = Logger.getLogger("EnumLog");
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        File logFile;
        for (UnexpectedTableData data : tableData) {
            logFile = new File(path, data.getName() + ".json");
            if (!logFile.createNewFile())
                log.info("{data.getName()}.json failed to create");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile.getAbsoluteFile()))) {
                bw.write(gson.toJson(data));
                log.info("{data.getName()}.json created SUCCESSFULLY!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void output() throws IOException {
        DirectoryPath dir = DirectoryPath.getInstance();
        String dirPath = dir.path();
        writefile(dirPath, this.sfm, this.fma, this.lfp);
    }
}

interface UnexpectedTableData {
    String getName();
}

class StructFloorMethod implements UnexpectedTableData {
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> structure = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> floor = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> constMethod = new HashMap<>();
    @Expose(serialize = false)
    private String name = "StructFloorMethod";

    public String getName() {
        return this.name;
    }
}

class FundMadoriArea implements UnexpectedTableData {
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> repairFund = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> madori = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> otherArea = new HashMap<>();
    @Expose(serialize = false)
    private String name = "FundMadoriArea";

    public String getName() {
        return this.name;
    }
}

class LimitFacilityParking implements UnexpectedTableData {
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> limits = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> facility = new HashMap<>();
    @Expose(serialize = true, deserialize = true)
    HashMap<String, String> parking = new HashMap<>();
    @Expose(serialize = false)
    private String name = "LimitFacilityParking";

    public String getName() {
        return this.name;
    }
}