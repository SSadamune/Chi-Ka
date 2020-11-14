package com.ssadamune.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectoryPath {
    private static DirectoryPath instance = new DirectoryPath();
    private String path;

    private DirectoryPath() {
        String now = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.path = "log\\" + now;
        new File(this.path).mkdirs();
    }

    public static DirectoryPath getInstance() {
        return instance;
    }

    public String path() {
        return this.path;
    }

}