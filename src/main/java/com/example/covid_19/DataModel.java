package com.example.covid_19;

import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DataModel extends MainActivity {


    TextView mNewConfirmedCountry;
    TextView mConfirmedCountry;
    TextView mNewDeathsCountry;
    TextView mDeathsCountry;
    TextView mNewRecoveredCountry;
    TextView mRecoveredCountry;
    TextView mDateOfSource;


    String CountryName;

    public static String[] countriestab = new String[190]; // StringTab of Countries


    // JSON FROM DOCUMENTER.GETPOSTMAN.COM :: link API: https://api.covid19api.com/live/country/summary




    public void letsDoSomeNetworkingPOSTMAN(String url)  {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) { // import cz.msebera.android.httpclient.Header
                // called when response HTTP status is "200 OK"
                Log.d("COVID-19", "JSONPOSTMAN: " + response.toString());

                try {
                    // connection
                    for (int i = 0; i < 190; i++)
                        countriestab[i] = response.getJSONArray("Countries").getJSONObject(i).getString("Country");
                    for (int i = 0; i < 190; i++)
                        Log.d("Tab", "Tab names: " + countriestab[i]);

                        Log.d("Technical", "choiceUser: worked");
                        for (int i = 0; i < 190; i++) {
                            if (countriestab[i].equals(CountryName)) { //countriestab[i] == CountryName

                                if(isNewConfirmed()) {
                                    String newConfirmedfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("NewConfirmed");
                                    String newConfirmed = mNewConfirmedCountry.getText() + newConfirmedfromJSON;
                                    mNewConfirmedCountry.setText(newConfirmed);
                                } else {
                                    mNewConfirmedCountry.setVisibility(View.GONE);
                                }

                                if (isConfirmed()) {
                                    String ConfirmedfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("TotalConfirmed");
                                    String Confirmed = mConfirmedCountry.getText() + ConfirmedfromJSON;
                                    mConfirmedCountry.setText(Confirmed);
                                } else {
                                    mConfirmedCountry.setVisibility(View.GONE);
                                }

                                if (isNewDeaths()) {
                                    String newDeathsfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("NewDeaths");
                                    String newDeaths = mNewDeathsCountry.getText() + newDeathsfromJSON;
                                    mNewDeathsCountry.setText(newDeaths);
                                } else {
                                    mNewDeathsCountry.setVisibility(View.GONE);
                                }

                                if (isDeaths()) {
                                    String DeathsfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("TotalDeaths");
                                    String Deaths = mDeathsCountry.getText() + DeathsfromJSON;
                                    mDeathsCountry.setText(Deaths);
                                } else {
                                    mDeathsCountry.setVisibility(View.GONE);
                                }

                                if (isNewRecovered()) {
                                    String NewRecoveredfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("NewRecovered");
                                    String newRecovered = mNewRecoveredCountry.getText() + NewRecoveredfromJSON;
                                    mNewRecoveredCountry.setText(newRecovered);
                                } else {
                                    mNewRecoveredCountry.setVisibility(View.GONE);
                                }

                                if (isRecovered()) {
                                    String RecoveredfromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("TotalRecovered");
                                    String Recovered = mRecoveredCountry.getText() + RecoveredfromJSON;
                                    mRecoveredCountry.setText(Recovered);
                                } else {
                                    mRecoveredCountry.setVisibility(View.GONE);
                                }

                                // CONFIG DATE

                                if (isLastedUpdated()) {
                                    String datefromJSON = response.getJSONArray("Countries").getJSONObject(i).getString("Date");
                                    String parts = datefromJSON.replace("T", " ");
                                    String parts2 = parts.replace("Z", "");
                                    StringBuilder build = new StringBuilder(parts2);

                                    for (int z = 0; z < 4; z++)
                                        build.deleteCharAt(build.length() - 1);

                                    String updated = "lasted updated: " + build.toString();
                                    mDateOfSource.setText(updated);
                                } else {
                                    mDateOfSource.setVisibility(View.GONE);
                                }

                            }
                        }

                } catch (JSONException e) {
                    // no connection
                    e.printStackTrace();


                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("COVID-19", "Request fail! Status code: " + statusCode);
                Log.d("COVID-19", "Fail response: " + response);
                Log.e("ERROR", e.toString());

            }
        });
    }
    public static String[] getCountriestab() {
        return countriestab;
    }


    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", getPackageName());
        return getString(resId);
    }
}
