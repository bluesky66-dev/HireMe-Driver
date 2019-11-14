package com.grepix.grepixutils;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJsonParsing {
	private static final boolean SUCCESS = true;

	public static String parseError(String json) {

		try {
			JSONObject stu = new JSONObject(json);
			JSONObject jsonObject = stu.getJSONObject("error");
			return jsonObject.getString("message");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public CloudResponse getCloudResponse(String response) {
		CloudResponse cloudResponse = new CloudResponse();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject.has("status")) {
				String status = jsonObject.getString("status");
				if (status.equalsIgnoreCase("OK")) {
					cloudResponse.setStatus(SUCCESS);
					cloudResponse.setJsonObject(jsonObject);
					/*if (jsonObject.has("next_offset")) {
						String next_offset = jsonObject
								.getString("next_offset");
					}*/
				} else {
					cloudResponse.setStatus(!SUCCESS);
					if (jsonObject.has("message")) {
						cloudResponse.setError(jsonObject.getString("message"));
					}
				}
			} else {
				cloudResponse.setStatus(!SUCCESS);
				if (jsonObject.has("error")) {
					JSONObject obj = jsonObject.getJSONObject("error");
					String error = obj.getString("message");
					cloudResponse.setError(error);
				}
			}
		} catch (JSONException e) {
			cloudResponse.setStatus(!SUCCESS);
		}
		return cloudResponse;
	}
}
