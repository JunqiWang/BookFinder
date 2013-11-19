package com.wilddynamos.bookapp.ws.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public abstract class Connection {
	
	private static DefaultHttpClient client = new DefaultHttpClient();
	private static DefaultHttpClient waitingClient = new DefaultHttpClient();
	
	public static String sessionID;
	
	public static final String STRURI = "http://10.0.23.39:8080/BookAppServer";
	public static int id;
	
	public synchronized static boolean login(String email, String pwd) {
		
		HttpPost doPost = new HttpPost(STRURI + "/Login");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("password", pwd));
		
		HttpEntity requestEntity = null;
		try {
			requestEntity = new UrlEncodedFormEntity(nvps);
		} catch (UnsupportedEncodingException e) {
		}
		
		doPost.setEntity(requestEntity);
		
		BufferedReader br = null;
		try {
			HttpResponse response;
			
			synchronized (client) {
				response = client.execute(doPost);
			}
			
			if(response.getStatusLine().getStatusCode() != 200)
				return false;
			
			HttpEntity entity = response.getEntity();
			
			br = new BufferedReader(
					new InputStreamReader(entity.getContent()));
			
			String result = br.readLine();
			if(!result.equals("-1")) {
				sessionID = result;
				id = Integer.parseInt(br.readLine());
				return true;
			} else
				return false;
			
		} catch(IOException e) {
			return false;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}
	
	public synchronized static InputStream waiting() {
		
		HttpParams hp = new BasicHttpParams();
		hp.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5);
		hp.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
		waitingClient.setParams(hp);
		
		HttpGet doGet = new HttpGet(STRURI + "/LongConn?id=" + id);
		
		try {
			HttpResponse response = waitingClient.execute(doGet);
			HttpEntity entity = response.getEntity();
			
			return entity.getContent();
		} catch(Exception e) {
			return null;
		}
	}
	
	public static InputStream requestByGet(String uri, Map<String, String> params) {
		
		uri += "?";
		if(params != null)
			for(Entry<String, String> entry: params.entrySet())
				uri += entry.getKey() + "=" + entry.getValue() + "&";
		uri = uri.substring(0, uri.length() - 1);
		
		HttpGet doGet = new HttpGet(STRURI + uri);

		synchronized (sessionID) {
			doGet.addHeader("cookie", "JSESSIONID=" + sessionID);
		}
		
		try {
			HttpResponse response;
			
			synchronized (client) {
				response = client.execute(doGet);
			}
			
			HttpEntity entity = response.getEntity();
			
			return entity.getContent();
			
		} catch(IOException e) {
			return null;
		}
	}
	
	public static InputStream requestByPost(String uri, Map<String, String> params) {
		
		HttpPost doPost = new HttpPost(STRURI + uri);
		
		synchronized (sessionID) {
			doPost.addHeader("cookie", "JSESSIONID=" + sessionID);
		}
		
		if(params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			for(Entry<String, String> entry: params.entrySet())
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));

			HttpEntity requestEntity = null;
			try {
				requestEntity = new UrlEncodedFormEntity(nvps);
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			doPost.setEntity(requestEntity);
		}
		
		try {
			HttpResponse response;
			
			synchronized (client) {
				response = client.execute(doPost);
			}
			
			HttpEntity entity = response.getEntity();
			
			return entity.getContent();
			
		} catch(IOException e) {
			return null;
		}
	}

}
