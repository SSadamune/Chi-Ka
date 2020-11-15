package com.ssadamune.crawler;

import org.jsoup.nodes.Document;

public interface PropertyParser {
    void buildProperty(House house, Document doc, String url);
    void outputSurpirses();
}
