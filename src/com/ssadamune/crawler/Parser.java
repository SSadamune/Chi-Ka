package com.ssadamune.crawler;

import java.io.IOException;

import org.jsoup.nodes.Document;

/**
 * use abstract class instead of interface, to make methods package accessly but
 * not public
 */
abstract class Parser {
    abstract Property parse(Document doc) ;

    abstract void outputSurpirses();
}

class HouseParser extends Parser {

    Property parse(Document doc) {
        Property house = new House();
        // TODO
        return house;
    }

    void outputSurpirses() {
        // TODO
    }

}

class MansionParser extends Parser {

    Property parse(Document doc) {
        Property mansion = new Mansion();
        // TODO
        return mansion;
    }

    void outputSurpirses() {
        // TODO
    }

}
