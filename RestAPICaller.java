package console_restapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;


public class RestAPICaller {
	
	private String USER_AGENT = "Mozilla/5.0";
	
	
	// HTTP GET request
	public RestAPIResponse sendGet(String url) throws Exception {
		
		System.out.println("\nSending HTTP 'GET' request to URL : " + url);
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		RestAPIResponse myresponse = new RestAPIResponse();
		myresponse.responseCode = con.getResponseCode();

		System.out.println("Response Code : " + myresponse.responseCode);

		if (myresponse.responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			myresponse.response = response.toString();
		}
		
		return myresponse;
	}
		
	// HTTP POST request
	public RestAPIResponse sendPost_XML(String url, String xmlbodystr) throws Exception {

		System.out.println("\nSending HTTP 'POST' request with XML body to URL : " + url);
		
		URL obj = new URL(url);
			
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//without Accept in header you will get 406 error
		//Accept: */* will give you 415 error 
		//Accept:application/xml
		con.setRequestProperty("Accept", "application/xml");
		con.setRequestProperty("Content-Type", "application/xml");

		
		//send body
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(xmlbodystr);
		wr.flush();
		wr.close();

		RestAPIResponse myresponse = new RestAPIResponse();
		myresponse.responseCode = con.getResponseCode();

		System.out.println("Response Code : " + myresponse.responseCode);

		if (myresponse.responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			myresponse.response = response.toString();
		}
		
		return myresponse;
	}
	
	// HTTPS GET request
	public RestAPIResponse sendGet_https(String url) throws Exception {
		
		System.out.println("\nSending HTTPS 'GET' request to URL : " + url);
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		RestAPIResponse myresponse = new RestAPIResponse();
		myresponse.responseCode = con.getResponseCode();
		
		System.out.println("Response Code : " + myresponse.responseCode);

		if (myresponse.responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			myresponse.response = response.toString();
		}
		
		return myresponse;
	}
	
	//HTTPS POST request
	public RestAPIResponse sendPost_https_XML(String url, String xmlbodystr) throws Exception {

		System.out.println("\nSending HTTPS 'POST' request with XML body to URL : " + url);
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//without Accept in header you will get 406 error
		//Accept: */* will give you 415 error 
		//Accept:application/xml
		con.setRequestProperty("Accept", "application/xml");
		con.setRequestProperty("Content-Type", "application/xml");
		
		//send body
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(xmlbodystr);
		wr.flush();
		wr.close();

		RestAPIResponse myresponse = new RestAPIResponse();
		myresponse.responseCode = con.getResponseCode();

		System.out.println("Response Code : " + myresponse.responseCode);

		if (myresponse.responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			myresponse.response = response.toString();
		}
		
		return myresponse;
	}

}//class
