package test;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReadHtml {

	public static void main(String[] args) throws IOException {
//		Document doc = Jsoup.connect("https://suumo.jp/ms/chuko/tokyo/sc_toshima/nc_94589774/").get();
		Document doc = Jsoup.parse(new File("C:/Users/zwieb/Documents/test.html"),"utf-8");
        String title = doc.title();
        System.out.println("title is: " + title);
        String keywords = doc.select("meta[name=keywords]").first().attr("content");
        System.out.println("Meta keyword : " + keywords);
	}
}
