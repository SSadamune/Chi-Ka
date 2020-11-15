package com.ssadamune.preparse;

import java.io.IOException;
import java.util.HashMap;
import org.jsoup.nodes.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class Collector {
    abstract void collect(Document doc, String url, String propertyKind);

    abstract void output() throws IOException;

    protected String[] add2Arr(String[] arr, String... strings) {
        String[] tempArr = new String[arr.length + strings.length];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for (int i = 0; i < strings.length; i++)
            tempArr[arr.length + i] = strings[i];
        return tempArr;

    }

    protected void add2Map(HashMap<String, String> map, String[] items, String property) {
        if (items == null || items.length == 0)
            return;
        for (String item : items) {
            if (!item.isBlank())
                map.putIfAbsent(item.trim(), property);
        }
    }

    protected String printMap(HashMap<String, String> map) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(map);
        /*
         * StringBuilder str = new StringBuilder("{\n"); map.forEach((m, p) ->
         * str.append("    \"" + m + "\" : \"" + p + "\",\n")); return str.substring(0,
         * str.length() - 2) + "\n}";
         */

    }

}
