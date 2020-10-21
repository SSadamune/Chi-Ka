package test;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlReader {

	public static void main(String[] args) throws IOException {
//		Document doc = Jsoup.connect("https://suumo.jp/ms/chuko/tokyo/sc_toshima/nc_94589774/").get();
		Document doc = Jsoup.parse(new File("C:/Users/zwieb/Documents/test.html"),"utf-8");
        String title = doc.title();
        System.out.println("title is: " + title);
        Element target = doc.select("script").first();
        System.out.println(target);
	}
}
