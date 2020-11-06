package com.ssadamune.crawler;

import java.io.IOException;

import javax.script.ScriptException;

public class Main {

    public static void main(String[] args) throws IOException, ScriptException {
//        test("toshima", 19);
//        test("nerima", 5);
//        test("chiyoda", 8);
//        test("shinjuku", 48);
//        test("setagaya", 56);
//        test("shibuya", 37);
        test("shinagawa", 1);
    }

    private static void test(String todofuken, int endPage) throws IOException{
        var ncCodes = SuumoParser.getMansionsUcList(todofuken, endPage);
        System.out.println(todofuken + " has " + ncCodes.size() + " properties");
        int num = 0;
        for(int nc : ncCodes) {
            try {
                System.out.println(SuumoParser.getEstate(todofuken, nc));
            } catch (UnexpectedFeatureException ufe) {
                System.out.print("unexpected feature at " + nc + ": ");
                System.out.println("Map.entry(\"" + ufe.feature() + "\", )," );
            }
            num++;
            if (num % 50 == 0) System.out.println(num + " properties found in " + todofuken);
        }
        System.out.println("all the " + num + " properties found in " + endPage + " pages of " + todofuken);
    }
}

