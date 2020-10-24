package com.ssadamune.modular;

import java.io.IOException;

import javax.script.ScriptException;

public class Test {

    public static void main(String[] args) throws IOException, ScriptException {

        var ncCodes = SuumoParser.getUcList("toshima", 19);
        for(int nc : ncCodes) SuumoParser.getEstate(nc);
        System.out.println("Found " + ncCodes.size() + " properties in 4 pages of Toshima");

    }
}

