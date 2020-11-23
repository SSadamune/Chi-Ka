package com.ssadamune.crawler;

public interface HandleProperty {
    void handle(Property p);
}

class Recorder implements HandleProperty {
    public void handle(Property p) {
        System.out.println("record " + p.getId());
    }
}

class DbWriter implements HandleProperty {
    public void handle(Property p) {
        // TODO
    }
}