package com.example.covid_19;

import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import cz.msebera.android.httpclient.Header;

public class FlagsDataModel extends MainActivity {

    public static String[] equalsArray = new String[240];


    // String flagsURL = "https://www.countryflags.io/be/shiny/64.png";

    String codeFromJSON;

    String chosenCountry;
    String url;
    String forCountry = MainActivity.getChosenCountry();




    public void letsDoSomeNetworking(String url) {
        Log.d("FlagsDataModel", "Country: " + forCountry);

        AsyncHttpClient kom = new AsyncHttpClient();
        kom.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                // called when response HTTP status is "200 OK"
                Log.d("FlagsDataModel", "JSONPOSTMAN: " + response.toString());


                try {
                    for (int i = 0; i < 240; i++) {

                        if (equalsArray[i].equals(forCountry)) { // looking for chosen country between numbers
                            codeFromJSON = response.getJSONObject(String.valueOf(i)).getString("code");
                            Log.d("FlagsDataModel", "Country Code:  " + codeFromJSON);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("test", "onSuccess: error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                super.onFailure(statusCode, headers, e, response);
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("COVID-19", "Request fail! Status code: " + statusCode);
                Log.d("COVID-19", "Fail response: " + response);
                Log.e("ERROR", e.toString());

            }

        });
//        return codeFromJSON;

        url = "https://www.countryflags.io/" + codeFromJSON + "/shiny/64.png";
        Log.d("FlagsDataModel", "imageURL: " + url);
    }



    public String getUrl() {
        return url;
    }
}

