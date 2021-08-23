
package com.example.covid_19;

import android.content.Intent;
import android.graphics.Paint;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;


/**
 * That's code the COVID-19 app.
 */

// TODO: Add JSON flags and config with code.



public class MainActivity extends AppCompatActivity {

    final String BASE_URL_SUMMARY = "https://api.covid19api.com/summary";

    TextView todayDate;

    FloatingActionButton addButton;


    String chosenCountry;

    TextView countryName;

    private static boolean isConfirmed, isNewConfirmed, isDeaths, isNewDeaths, isRecovered, isNewRecovered,
    isLastUpdated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Code to join to JSON ***/


//        todayDate
        Date currentTime = Calendar.getInstance().getTime();
        String formattedDate = DateFormat.getDateInstance().format(currentTime);
        todayDate = findViewById(R.id.todayDate);
        todayDate.setText(formattedDate);




//        /* END CODE FOR JSON ***/






        // setOnClickListener ImageView "add"
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddActivity();
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
                    choiceUser();
                    ImageView countryFlag;
                    countryFlag = findViewById(R.id.name_flag);
                    FlagsDataModel model = new FlagsDataModel();
                    loadImage(model.imageURL(), countryFlag);

                    Log.d("onResume", "button pressed");
                }

                String Poland = "Poland";

                if (chosenCountry != null && chosenCountry.equals(Poland)) {
                    //underline and blue
                    countryName.setPaintFlags(countryName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    countryName.setTextColor(getResources().getColor(R.color.cardview_light_background));
                    countryName.setOnClickListener(new View.OnClickListener() {
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

    // createNewLayout

    private void choiceUser() {



        TextView newConfirmed = findViewById(R.id.new_confirmed_name);
        TextView confirmed = findViewById(R.id.confirmed_name);
        TextView newDeaths = findViewById(R.id.new_deaths_name);
        TextView deaths = findViewById(R.id.deaths_name);
        TextView newRecovered = findViewById(R.id.new_recovered_name);
        TextView recovered = findViewById(R.id.recovered_name);
        TextView updated = findViewById(R.id.update_name);
//
        ImageView countryFlag = findViewById(R.id.name_flag);



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
    }

    private void loadImage(String url, ImageView countryFlag) {
        Picasso.with(getBaseContext())
                .load(url)
                .into(countryFlag);
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

    public String getChosenCountry() {
        return chosenCountry;
    }
}

