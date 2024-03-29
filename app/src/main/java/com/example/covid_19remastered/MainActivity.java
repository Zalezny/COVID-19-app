
package com.example.covid_19remastered;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * That's code the COVID-19 app.
 *
 * Used API:
 * https://www.countryflags.io/code_country_name/shiny/64.png
 * https://api.covid19api.com/summary
 * https://api.apify.com/v2/key-value-stores/3Po6TV7wTht4vIEid/records/LATEST?disableRedirect=true
 *
 */





public class MainActivity extends AppCompatActivity {

    final String BASE_URL_SUMMARY = "https://api.covid19api.com/summary";


    private String chosenCountry;

    private TextView countryName;

    private static boolean isConfirmed, isNewConfirmed, isDeaths, isNewDeaths, isRecovered, isNewRecovered,
    isLastUpdated;

    private Button tryAgainButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Code to join to JSON ***/


//        todayDate
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);
        TextView todayDate = findViewById(R.id.todayDate);
        todayDate.setText(formattedDate);



        // setOnClickListener ImageView "add"
        FloatingActionButton addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddActivity();
            }
        });

        // Button try_Again

        tryAgainButton = findViewById(R.id.try_again_button);

        drawLayout();
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLayout();
            }
        });



    }

    public void openPolandRegionActivity() {

        Intent intent = new Intent(this, PolandRegionActivity.class);
        startActivity(intent);

    }

    public void openAddActivity() {
        DataModel ForCountriesTab= new DataModel();
        ForCountriesTab.letsDoSomeNetworkingPOSTMAN(BASE_URL_SUMMARY);

        Intent intent = new Intent(this, AddActivity.class);

        startActivity(intent);



    }




    // onResume()

    protected void onResume() {
        super.onResume();
        Log.d("COVID-19", "onResume() called");

        Intent myIntent = getIntent();

        // isChoice - it is from AddActivity.java. If backArrow have clicked that isChoice is true,
        // unless is false.

            boolean isArrowBackFromAddActivity = myIntent.getBooleanExtra("isChoice", false);

            if(!isConnected())
            {
                drawLayout();
            } else {

                if (isArrowBackFromAddActivity) {
                    isConfirmed = myIntent.getBooleanExtra("checkBoxConfirmed", true);
                    isNewConfirmed = myIntent.getBooleanExtra("checkBoxNewConfirmed", true);
                    isDeaths = myIntent.getBooleanExtra("checkBoxDeaths", true);
                    isNewDeaths = myIntent.getBooleanExtra("checkBoxNewDeaths", true);
                    isRecovered = myIntent.getBooleanExtra("checkBoxRecovered", true);
                    isNewRecovered = myIntent.getBooleanExtra("checkBoxNewRecovered", true);
                    isLastUpdated = myIntent.getBooleanExtra("checkBoxLastUpdated", true);

                    Log.d("Checkoboxe", "onResume: " + isLastUpdated);

                    chosenCountry = myIntent.getStringExtra("selectedCountry");

                    boolean isAddButtonPressed = myIntent.getBooleanExtra("addButtonPressed", false);

                    if (isAddButtonPressed) {
                        choiceUserAndSetVisibility();


                        // ImageView Flag
                        ImageView countryFlag;
                        countryFlag = findViewById(R.id.name_flag);
                        String url = "https://www.countryflags.io/" + getCountryCode() + "/shiny/64.png";
                        loadImage(url, countryFlag);

                        Log.d("onResume", "button pressed");
                    }

                    String Poland = "Poland";

                    if (chosenCountry != null && chosenCountry.equals(Poland)) {
                        //underline and blue for poland name
                        countryName.setPaintFlags(countryName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        countryName.setTextColor(getResources().getColor(R.color.colorAccent));
                        countryName.setOnClickListener(new View.OnClickListener() {


                            // Interact onClick poland country and turn on polish regions
                            @Override
                            public void onClick(View view) {
                                openPolandRegionActivity();

                            }
                        });
                    } else if (chosenCountry != null) {
                        countryName.setPaintFlags(View.INVISIBLE);
                    }

                    // this change key from true to false (main)
                    myIntent.putExtra("isChoice", false);
                }
            }

    }


    private void choiceUserAndSetVisibility() {



        TextView newConfirmed = findViewById(R.id.new_confirmed_name);
        TextView confirmed = findViewById(R.id.confirmed_name);
        TextView newDeaths = findViewById(R.id.new_deaths_name);
        TextView deaths = findViewById(R.id.deaths_name);
        TextView newRecovered = findViewById(R.id.new_recovered_name);
        TextView recovered = findViewById(R.id.recovered_name);
        TextView updated = findViewById(R.id.update_name);



        //DATA MODEL

        final DataModel Name = new DataModel();

        Name.CountryName = chosenCountry;
        Name.mNewConfirmedCountry = newConfirmed;
        Name.mConfirmedCountry = confirmed;
        Name.mNewDeathsCountry = newDeaths;
        Name.mDeathsCountry = deaths;
        Name.mNewRecoveredCountry = newRecovered;
        Name.mRecoveredCountry = recovered;
        Name.mDateOfSource = updated;

        Name.letsDoSomeNetworkingPOSTMAN(BASE_URL_SUMMARY);

        // Set country name
        countryName = findViewById(R.id.country_name);
        countryName.setText(chosenCountry);

        // set Visibility
        newConfirmed.setVisibility(View.VISIBLE);
        confirmed.setVisibility(View.VISIBLE);
        newDeaths.setVisibility(View.VISIBLE);
        deaths.setVisibility(View.VISIBLE);
        newRecovered.setVisibility(View.VISIBLE);
        recovered.setVisibility(View.VISIBLE);
        updated.setVisibility(View.VISIBLE);
    }

    private void loadImage(String url, ImageView countryFlag) {
        Picasso.with(getBaseContext())
                .load(url)
                .into(countryFlag);


    }


    private String getCountryCode() {

        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        return countries.get(chosenCountry);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            if(networkInfo.isConnected())
                return true;
            else
                return false;
        }else
            return false;
    }

    private void drawLayout() {
        TextView infoText = findViewById(R.id.country_name);
        TextView noInternetHeading = findViewById(R.id.no_internet_heading);
        TextView noInternetText = findViewById(R.id.no_internet_text);
        ImageView gravePicture = findViewById(R.id.grave_picture);


        if (isConnected()) {
            infoText.setVisibility(View.VISIBLE);
            noInternetHeading.setVisibility(View.GONE);
            noInternetText.setVisibility(View.GONE);
            gravePicture.setVisibility(View.GONE);
            tryAgainButton.setVisibility(View.GONE);
        }
            else
            {
                infoText.setVisibility(View.GONE);
                noInternetHeading.setVisibility(View.VISIBLE);
                noInternetText.setVisibility(View.VISIBLE);
                gravePicture.setVisibility(View.VISIBLE);
                tryAgainButton.setVisibility(View.VISIBLE);
        }
    }


    //GETTERS

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public boolean isNewConfirmed() {
        return isNewConfirmed;
    }

    public boolean isDeaths() {
        return isDeaths;
    }

    public boolean isNewDeaths() {
        return isNewDeaths;
    }

    public boolean isRecovered() {
        return isRecovered;
    }

    public boolean isNewRecovered() {
        return isNewRecovered;
    }

    public boolean isLastedUpdated() {
        return isLastUpdated;
    }

}

