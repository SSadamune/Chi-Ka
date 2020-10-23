package com.ssadamune.modular;

import java.io.IOException;

import javax.script.ScriptException;

public class Test {

    public static void main(String[] args) throws IOException, ScriptException {

        var ncCodes = SuumoParser.getUcList("toshima", 2);
        for(int nc : ncCodes) System.out.println(SuumoParser.getEstate(nc));
        System.out.println("Found " + ncCodes.size() + " properties in 2 pages of Toshima");

    }
}

