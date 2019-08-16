package twitterProject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataCleaning {

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {
		//	connectToDb();

		String searchTweets = "";
		JSONParser parser = new JSONParser();
		JSONArray rawArray = new JSONArray();
		JSONArray resultsArr = new JSONArray();
		JSONArray finalarr = new JSONArray();
		try {
			//reading json files for both searching and streaming and performing cleaning operation
			Object obj = parser.parse(new FileReader("D:/javaPrj/twitterProject/searchFinal.json"));
			JSONObject rawData = new JSONObject();
			rawArray = (JSONArray)obj;
			for(int i=0;i<rawArray.size();i++) {
				rawData = (JSONObject) rawArray.get(i);
				resultsArr = (JSONArray) rawData.get("results");
				for (int k =0 ; k < resultsArr.size();k++) {

					JSONObject  resultsObj = new JSONObject();
					JSONObject tweetObj = (JSONObject) resultsArr.get(k);
					String text = (String) tweetObj.get("text");
					String cleanTweet = textCleaning(text);
					searchTweets = searchTweets + cleanTweet;
					//writing into json object
					resultsObj.put("text",cleanTweet);
					resultsObj.put("tweetId", tweetObj.get("id"));
					resultsObj.put("reTweeted", tweetObj.get("retweeted"));
					finalarr.add(resultsObj);

				}
			}

			System.out.println(searchTweets);
			try (FileWriter file = new FileWriter("D:/javaPrj/twitterProject/processedsample.json",true)) {
				PrintWriter writer = new PrintWriter(file);

				writer.println(finalarr);
				writer.close();
			}


			try (FileWriter file = new FileWriter("D:/javaPrj/twitterProject/searchsample.txt",true)) {
				PrintWriter writer = new PrintWriter(file);

				writer.println(searchTweets);
				writer.close();
			}



		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String textCleaning(String str) {

		String tweetText = "";
		//regex to remove url
		tweetText = str.replaceAll("^(http:\\/\\/)?[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+$", " ");;

		//regex to remove emoticons
		String regex = "/[^\\u1F600-\\u1F6FF\\s]/i"; 
		tweetText = tweetText.replaceAll(regex, " ");

		//removing special characters
		tweetText = tweetText.replaceAll("\\p{P}", ""); 

		//removing stop words
		Pattern stopWordPattern = Pattern.compile("\\b(I|this|its|The|is|was|here|there|not)\\b\\s?", Pattern.CASE_INSENSITIVE);
		Matcher stopWordMatcher = stopWordPattern.matcher(tweetText);
		while (stopWordMatcher.find()) {
			tweetText = tweetText.replace(stopWordMatcher.group(),"");
		}
		return tweetText;

	}



}
