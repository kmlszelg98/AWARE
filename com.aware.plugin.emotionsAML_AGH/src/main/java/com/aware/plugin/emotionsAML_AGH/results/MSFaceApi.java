package com.aware.plugin.emotionsAML_AGH.results;

import android.content.ContentValues;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MSFaceApi {

    private String simpleJson = "[\n" +
            "  {\n" +
            "    \"faceRectangle\": {\n" +
            "      \"top\": 66,\n" +
            "      \"left\": 36,\n" +
            "      \"width\": 110,\n" +
            "      \"height\": 110\n" +
            "    },\n" +
            "    \"scores\": {\n" +
            "      \"anger\": 2.03148872E-07,\n" +
            "      \"contempt\": 6.29244869E-06,\n" +
            "      \"disgust\": 8.73422046E-09,\n" +
            "      \"fear\": 1.08945715E-08,\n" +
            "      \"happiness\": 6.176505E-08,\n" +
            "      \"neutral\": 0.9999826,\n" +
            "      \"sadness\": 9.132367E-06,\n" +
            "      \"surprise\": 1.68514737E-06\n" +
            "    }\n" +
            "  }\n" +
            "]";

    private static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    private HttpClient httpClient;
    private HttpPost request;

    public MSFaceApi() {

        httpClient = new DefaultHttpClient();

        URIBuilder builder = null;
        try {
            builder = new URIBuilder(uriBase);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // Request parameters. All of them are optional.
        builder.setParameter("returnFaceId", "true");
        builder.setParameter("returnFaceLandmarks", "false");
        builder.setParameter("returnFaceAttributes", "emotion");

        // Prepare the URI for the REST API call.
        URI uri = null;
        try {
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        request = new HttpPost(uri);
        // Request headers.
        request.setHeader("Content-Type", "application/octet-stream");
        request.setHeader("Ocp-Apim-Subscription-Key", "3bc0c7c4231b43f3998fa8c89502ff36");
    }

    public ContentValues detect(String path) throws IOException, JSONException {
        /*TODO Add Ms Face Api */
        File file = new File(path);
        FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM.toString());

        request.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        Map<String,Double> results = new HashMap<>();

        if (entity != null)
        {
            // Format and display the JSON response.
            System.out.println("REST Response:\n");

            String jsonString = EntityUtils.toString(entity).trim();
            if (jsonString.charAt(0) == '[') {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion");

                ContentValues contentValues = new ContentValues();
                contentValues.put(EmotionsVariables.photo,path);
                contentValues.put(EmotionsVariables.anger,Double.toString(jsonObject.getDouble("anger")));
                contentValues.put(EmotionsVariables.contempt,Double.toString(jsonObject.getDouble("contempt")));
                contentValues.put(EmotionsVariables.disgust,Double.toString(jsonObject.getDouble("disgust")));
                contentValues.put(EmotionsVariables.fear,Double.toString(jsonObject.getDouble("fear")));
                contentValues.put(EmotionsVariables.happiness,Double.toString(jsonObject.getDouble("happiness")));
                contentValues.put(EmotionsVariables.neutral,Double.toString(jsonObject.getDouble("neutral")));
                contentValues.put(EmotionsVariables.sadness,Double.toString(jsonObject.getDouble("sadness")));
                contentValues.put(EmotionsVariables.surprise,Double.toString(jsonObject.getDouble("surprise")));
                contentValues.put("EMOTION", calculateMax(jsonObject));
                return contentValues;

            }
            else if (jsonString.charAt(0) == '{') {
                JSONObject jsonObject = new JSONObject(jsonString);
                System.out.println(jsonObject.toString(2));
            } else {
                System.out.println(jsonString);
            }
        }

        return new ContentValues();

    }

    private String calculateMax(JSONObject jsonObject) throws JSONException {

        Map<String, Double> res = new HashMap<>();
        res.put(EmotionsVariables.disgust,jsonObject.getDouble("disgust"));
        res.put(EmotionsVariables.sadness,jsonObject.getDouble("sadness"));
        res.put(EmotionsVariables.contempt,jsonObject.getDouble("contempt"));
        res.put(EmotionsVariables.anger,jsonObject.getDouble("anger"));
        res.put(EmotionsVariables.happiness,jsonObject.getDouble("happiness"));
        res.put(EmotionsVariables.neutral,jsonObject.getDouble("neutral"));
        res.put(EmotionsVariables.surprise,jsonObject.getDouble("surprise"));
        res.put(EmotionsVariables.fear,jsonObject.getDouble("fear"));
        double maxValueInMap=(Collections.max(res.values()));
        for (Map.Entry<String, Double> entry : res.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                return entry.getKey();
            }
        }
        return EmotionsVariables.neutral;
    }




}
