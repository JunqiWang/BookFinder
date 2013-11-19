package com.wilddynamos.bookapp.ws.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class DataUtils {
	
	public static String receiveFlag(InputStream is) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			return br.readLine();
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {}
		}
	}

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
			} catch (IOException e) {}
		}

		try {
			return new JSONArray(result);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Map<String, Object>> JSON2Array(JSONArray jsonArray) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		for(int i = 0; i < jsonArray.length(); i ++) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			try {
				for(@SuppressWarnings("unchecked")
				Iterator<String> itr = jsonArray.getJSONObject(i).keys(); itr.hasNext();) {
					String name = itr.next();
					map.put(name, jsonArray.getJSONObject(i).get(name));
				}
			} catch (JSONException e) {
				return null;
			}
			
			list.add(map);
		}
		
		return list;
	}
}
