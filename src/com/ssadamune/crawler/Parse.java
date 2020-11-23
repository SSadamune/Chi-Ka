package com.ssadamune.crawler;

import java.io.IOException;

import org.jsoup.nodes.Document;

abstract class Parse {
    abstract Property parse(Document doc) throws IOException;
    abstract void outputSurpirses();
}

class HouseParser extends Parse{

    Property parse(Document doc) throws IOException {
        Property house = new House();
        // TODO
        return house;
    }

    void outputSurpirses() {
        // TODO
    }

}

class MansionParser extends Parse{

    Property parse(Document doc) throws IOException {
        Property mansion = new Mansion();
        // TODO
        return mansion;
    }

    void outputSurpirses() {
        // TODO
    }

}
