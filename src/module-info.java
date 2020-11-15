module CHIKA {
    requires org.jsoup;
    requires java.scripting;
    requires com.google.gson;
    exports com.ssadamune.crawler;
    exports com.ssadamune.preparse;
    opens com.ssadamune.crawler;
    opens com.ssadamune.preparse;
}