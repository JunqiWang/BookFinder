package com.wilddynamos.bookfinder.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Provides methods that transform received data into desired format
 * 
 * @author JunqiWang
 * 
 */
public abstract class DataUtils {

	/**
	 * Receiving a flag, only one line, normally a char
	 * 
	 * @param is
	 * @return
	 */
	public static String receiveFlag(InputStream is) {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			return br.readLine();
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Receiving JSONArray
	 * 
	 * @param is
	 * @return
	 */
	public static JSONArray receiveJSON(InputStream is) {

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String result = "";
		String readline = null;

		try {
			while ((readline = br.readLine()) != null)
				result += readline;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}

		try {
			return new JSONArray(result);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
