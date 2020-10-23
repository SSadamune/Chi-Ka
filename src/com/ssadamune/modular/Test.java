package com.ssadamune.modular;

import java.io.IOException;

import javax.script.ScriptException;

public class Test {

    public static void main(String[] args) throws IOException, ScriptException {
        int[] ncCodes = {94389101, 94589774};
        for(int nc : ncCodes) {
            System.out.println(SuumoParser.getEstate(nc));
        }
    }
}

