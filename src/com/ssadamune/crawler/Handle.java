package com.ssadamune.crawler;

public interface Handle {
    void handle(Property p);
}

class Recorder implements Handle {
    public void handle(Property p) {
        System.out.println("record " + p.getId());
    }
}

class DbWriter implements Handle {
    public void handle(Property p) {
        // TODO
    }
}