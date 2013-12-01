package com.wilddynamos.bookfinder.ws.remote;

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

/**
 * Abstract class used for remote connection. Http get and post methods are
 * provided.
 * 
 * @author JunqiWang
 * 
 */
public abstract class Connection {

	/**
	 * The main client for data exchange
	 */
	private static DefaultHttpClient client = new DefaultHttpClient();

	/**
	 * Client waiting for new requests and responds' coming
	 */
	private static DefaultHttpClient waitingClient = new DefaultHttpClient();

	/**
	 * Session id stored in the server side Consider not to use this field
	 */
	public static String sessionID = "";

	/**
	 * Server address
	 */
	public static final String STRURI = "http://10.0.23.238:8080/BookAppServer_1110";

	/**
	 * User id at server side
	 */
	public static int id;

	/**
	 * login method, initialise user id and session id.
	 * 
	 * @param email
	 * @param pwd
	 * @return
	 */
	public static boolean login(String email, String pwd) {
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

			if (response.getStatusLine().getStatusCode() != 200)
				return false;

			HttpEntity entity = response.getEntity();

			br = new BufferedReader(new InputStreamReader(entity.getContent()));
			
			String result = br.readLine();
			if (!result.equals("-1")) {
				sessionID = result;
				id = Integer.parseInt(br.readLine());
				return true;
			}
			return false;

		} catch (IOException e) {
			return false;
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Establish a long connection (no data timeout) with server. Waiting for
	 * new requests and responses.
	 * 
	 * @return
	 */
	public synchronized static InputStream waiting() {

		HttpParams hp = new BasicHttpParams();
		hp.setParameter(CoreConnectionPNames.SO_TIMEOUT, 0);
		waitingClient.setParams(hp);

		HttpGet doGet = new HttpGet(STRURI + "/LongConn?id=" + id);

		try {
			HttpResponse response = waitingClient.execute(doGet);
			HttpEntity entity = response.getEntity();

			return entity.getContent();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Http get method.
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public static InputStream requestByGet(String uri,
			Map<String, String> params) {

		uri += "?";
		if (params != null)
			for (Entry<String, String> entry : params.entrySet())
				uri += entry.getKey() + "=" + entry.getValue() + "&";
		uri = uri.substring(0, uri.length() - 1);

		HttpGet doGet = new HttpGet(STRURI + uri);

		synchronized (sessionID) {
			doGet.addHeader("cookie", "'JSESSIONID'='" + sessionID + "'");
		}

		try {
			HttpResponse response;

			synchronized (client) {
				response = client.execute(doGet);
			}

			HttpEntity entity = response.getEntity();

			return entity.getContent();

		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Http post method.
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public static InputStream requestByPost(String uri,
			Map<String, String> params) {

		HttpPost doPost = new HttpPost(STRURI + uri);

		synchronized (sessionID) {
			doPost.addHeader("cookie", "'JSESSIONID'='" + sessionID + "'");
		}

		if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();

			for (Entry<String, String> entry : params.entrySet())
				nvps.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));

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

		} catch (IOException e) {
			return null;
		}
	}

}
