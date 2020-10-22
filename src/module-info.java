module MDproject {
    requires org.jsoup;
    requires java.scripting;
    requires com.google.gson;
    exports com.sadaki.modular;
    opens com.sadaki.modular;
}