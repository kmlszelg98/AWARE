package com.aware.plugin.emotionsAML_AGH.results;

import android.content.ContentValues;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
        builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

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
        request.setHeader("Ocp-Apim-Subscription-Key", "de966db0a82641319a9c0079dfbdf983");
    }

    public ContentValues detect(String path) throws IOException, JSONException {
        /*TODO Add Ms Face Api */
        /*File file = new File("./test.jpg");
        FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM.toString());

        request.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (entity != null)
        {
            // Format and display the JSON response.
            System.out.println("REST Response:\n");

            String jsonString = EntityUtils.toString(entity).trim();
            if (jsonString.charAt(0) == '[') {
                JSONArray jsonArray = new JSONArray(jsonString);
                System.out.println(jsonArray.toString(2));
            }
            else if (jsonString.charAt(0) == '{') {
                JSONObject jsonObject = new JSONObject(jsonString);
                System.out.println(jsonObject.toString(2));
            } else {
                System.out.println(jsonString);
            }
        }*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(EmotionsVariables.photo,path);
        contentValues.put(EmotionsVariables.anger,"2.03148872E-07");
        contentValues.put(EmotionsVariables.contempt,"6.29244869E-06");
        contentValues.put(EmotionsVariables.disgust,"8.73422046E-09");
        contentValues.put(EmotionsVariables.fear,"1.08945715E-08");
        contentValues.put(EmotionsVariables.happiness,"6.176505E-08");
        contentValues.put(EmotionsVariables.neutral,"0.9999826");
        contentValues.put(EmotionsVariables.sadness,"9.132367E-06");
        contentValues.put(EmotionsVariables.surprise,"1.68514737E-06");
        return contentValues;

        //EmotionsVariables.face = new Face(path,"2.03148872E-07","6.29244869E-06","8.73422046E-09","1.08945715E-08",
                //"6.176505E-08","0.9999826","9.132367E-06","1.68514737E-06");


    }
}
