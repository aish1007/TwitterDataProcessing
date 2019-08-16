package twitterProject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import twitterProject.ConstantData;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonWriter;
import org.apache.http.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static com.google.common.collect.Lists.newArrayList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.github.seratch.signedrequest4j.HttpMethod;
import com.github.seratch.signedrequest4j.OAuthAccessToken;
import com.github.seratch.signedrequest4j.OAuthConsumer;
import com.github.seratch.signedrequest4j.SignatureMethod;
import com.github.seratch.signedrequest4j.SignedRequest;
import com.github.seratch.signedrequest4j.SignedRequestFactory;
import com.google.common.collect.Lists;
import org.glassfish.json.JsonProviderImpl;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import okhttp3.*;
import javax.net.ssl.HttpsURLConnection;
 

public class DataExtraction {
	String url = ConstantData.url;
	String consumerKey = ConstantData.consumerKey;
	String token = ConstantData.token;
	String signature = ConstantData.signature;
	String version= ConstantData.version;
	String nonce = ConstantData.nonce;
	String signatureMethod = ConstantData.signatureMethod;
	String consumerSecret = ConstantData.consumerSecret;
	public static void main(String[] args) {
		DataExtraction sc = new DataExtraction();
		try {
			sc.streamAPI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.searchAPI();
	}
	
	public void streamAPI() throws IOException {
		ObjectOutputStream outputStream;
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

		/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		// Optional: set up some followings and track terms
		List<Long> followings = Lists.newArrayList(1392238819L,1095417676023902209L);
        List<String> terms = Lists.newArrayList("Halifax");
		
		hosebirdEndpoint.followings(followings);
		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, consumerSecret);
		ClientBuilder builder = new ClientBuilder()
				  .name("Hosebird-Client-01")                              
				  .hosts(hosebirdHosts)
				  .authentication(hosebirdAuth)
				  .endpoint(hosebirdEndpoint)
				  .processor(new StringDelimitedProcessor(msgQueue))
				  .eventMessageQueue(eventQueue);                        

				Client hosebirdClient = builder.build();
				// Attempts to establish a connection.
				hosebirdClient.connect();
				int count = 0;
				JSONArray jsonArray = new JSONArray();

				while (!hosebirdClient.isDone()) {
					
					  String msg;
					    JSONObject jsonObj = new JSONObject();
						JSONParser parser = new JSONParser();
						
					try {
						System.out.println("connection created");
						msg = msgQueue.take();
						
						try {
							
							jsonObj = (JSONObject) parser.parse(msg);
							jsonArray.add(jsonObj);
							count++;
							
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						if(count == 999) {
							try (FileWriter file = new FileWriter("D:/javaPrj/DataAssignment2/streamFinal.json")) {
								PrintWriter writer = new PrintWriter(file);
								writer.println(jsonArray);
								writer.close();
								hosebirdClient.stop();
							}
						}
						
						System.out.println("tweet count is"+ count + msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					}
				
				//hosebirdClient.stop();
	}
	
	@SuppressWarnings("unused")
	public  void searchAPI() {
		
		String res = null;
		JSONObject searchObj = new JSONObject();

			
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://api.twitter.com/1.1/tweets/search/30day/prod.json?query=halifax")
			  .get()
			  .addHeader("authorization", "OAuth oauth_consumer_key="+consumerKey +",oauth_token="+token+",oauth_signature_method="+signatureMethod+",oauth_timestamp=\"1551244939\",oauth_nonce="+nonce+",oauth_version="+version+",oauth_signature=\"%2FT05TtEq%2BvOYOh%2FAVXL%2FyvGPLvA%3D\"")
			  .addHeader("cache-control", "no-cache")
			  .addHeader("postman-token", "9d477603-bac6-1be4-cf3b-487732fc567b")
			  .build();

					try {
						int count =0 ;
						Response response = client.newCall(request).execute();
					    res = response.body().string();	
					    
					    JSONArray jsonArray = new JSONArray();
						JSONParser parser = new JSONParser(); 
						try {
							searchObj = (JSONObject) parser.parse(res);
							jsonArray.add(searchObj);
							
						  
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						try (FileWriter file = new FileWriter("D:/javaPrj/DataAssignment2/searchFinal.json",true)) {
							PrintWriter writer = new PrintWriter(file);
							 writer.println(jsonArray);
							 writer.close();
						}
						
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	
		
		
	}

}