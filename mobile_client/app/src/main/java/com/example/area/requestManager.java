package com.example.area;

import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * The type Request manager.
 */
public class requestManager {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * Create post request request.
     *
     * @param jsonData  the json data
     * @param url       the url
     * @param userToken the user token
     * @return the request
     */
    public Request createPostRequest(JSONObject jsonData, String url, String userToken) {
        String json = jsonData.toString();
        RequestBody body = RequestBody.create(JSON, json);
        if (userToken == null) {
            return new Request.Builder().url(url).post(body).build();
        } else {
            return new Request.Builder().url(url).addHeader("Authorization", "Bearer " + userToken).post(body).build();
        }
    }

    /**
     * Create get request request.
     *
     * @param url       the url
     * @param userToken the user token
     * @return the request
     */
    public Request createGetRequest(String url, String userToken) {
        return new Request.Builder().url(url).addHeader("Authorization", "Bearer " + userToken).build();
    }

    /**
     * Extract data response json object.
     *
     * @param result the result
     * @return the json object
     */
    JSONObject extractDataResponse(String result) {
        try {
            JSONObject jsonResponse = new JSONObject(result);
            return jsonResponse.getJSONObject("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
