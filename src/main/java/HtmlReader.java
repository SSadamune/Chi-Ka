package main.java;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;

public class HtmlReader {

	public static void main(String[] args) throws IOException, ScriptException {
//		Document doc = Jsoup.connect("https://suumo.jp/ms/chuko/tokyo/sc_toshima/nc_93348582/").get();
		Document doc = Jsoup.parse(new File("C:/Users/zwieb/Documents/test.html"),"utf-8");
		Element estateJsoup = doc.select("script").first();

		String estateStr = estateJsoup.data();
		estateStr = estateStr.substring(25, estateStr.length() - 11);

		String estateStrTest = "{ \"todofukenCd\" : \"13\" , \"todofukenNm\" : \"東京都\" }";

		Gson gson = new Gson();
		Estate curEstate = gson.fromJson(estateStrTest, Estate.class);
		System.out.println(curEstate);
	}
}

