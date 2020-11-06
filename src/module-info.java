module MDproject {
    requires org.jsoup;
    requires java.scripting;
    requires com.google.gson;
    exports com.ssadamune.crawler;
    opens com.ssadamune.crawler;
}