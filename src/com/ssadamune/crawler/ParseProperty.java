package com.ssadamune.crawler;

import org.jsoup.nodes.Document;

interface ParseProperty {
    void buildProperty(Property property, Document doc, String url);
    void outputSurpirses();
}
