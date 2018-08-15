package com.lakshya.xkcdcomicreader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECTION_TIMEOUT = 10000;
    private HttpURLConnection mHttpURLConnection;

    public ComicModel getDataFromUrl(String url_str){
        ComicModel comicModel = new ComicModel();
        try {
            URL url = new URL(url_str);
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setRequestMethod(REQUEST_METHOD);
            mHttpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            mHttpURLConnection.setReadTimeout(READ_TIMEOUT);
            mHttpURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mHttpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = bufferedReader.readLine()) != null){
                response.append(inputLine);
            }
            bufferedReader.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            comicModel.setComicTitle(jsonObject.getString("title"));
            comicModel.setComicImageUrl(jsonObject.getString("img"));
            comicModel.setImgAltText(jsonObject.getString("alt"));
            comicModel.setDate(jsonObject.getString("day"), jsonObject.getString("month"), jsonObject.getString("year"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comicModel;
    }
}
