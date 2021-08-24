package com.example.covid_19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;



public class AddActivity extends AppCompatActivity  {


    boolean isConfirmed, isDeaths, isNewDeaths, isNewConfirmed, isNewRecovered,
            isRecovered, isLastUpdated, addConfirmed;

    String selectedCountry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /////// AUTOCOMPLETETEXTVIEW ///////

        // COUNTRIES TAB FOR AUTOCOMPLETE TEXTVIEW
        Log.d("AddActivity", "Countries Tab: " + Arrays.toString(DataModel.getCountriestab()));

        //ARRAY TO COUNTRIES TAB

        final AutoCompleteTextView selectedCountryACTextView = findViewById(R.id.selectedCountry);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, DataModel.getCountriestab());
        selectedCountryACTextView.setAdapter(adapter);

        // HIDE KEYBOARD

        selectedCountryACTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        /////// END  AUTOCOMPLETETEXTVIEW ///////


                // DECLARED CHECKBOXES fROM ACTIVITY_ADD

        final TextView isConfirmedView = findViewById(R.id.checkedConfirmed);
        final TextView isDeathsView = findViewById(R.id.checkedDeaths);
        final TextView isNewDeathsView = findViewById(R.id.checkedNewDeaths);
        final TextView isNewConfirmedView = findViewById(R.id.checkedNewConfirmed);
        final TextView isNewRecoveredView = findViewById(R.id.checkedNewRecovered);
        final TextView isRecoveredView = findViewById(R.id.checkedRecovered);
        final TextView isLastUpdatedView = findViewById(R.id.checkedLastUpdated);

        //BUTTON "ADD"

        final Button mainAddButton = findViewById(R.id.mainAddButton);


        mainAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable name = selectedCountryACTextView.getText();
                selectedCountry = name.toString();
                for (int i = 0; i<190;i++)
                if (selectedCountry.equals(DataModel.countriestab[i])) {
                    onCheckboxClicked(isConfirmedView);
                    onCheckboxClicked(isNewConfirmedView);
                    onCheckboxClicked(isDeathsView);
                    onCheckboxClicked(isNewDeathsView);
                    onCheckboxClicked(isNewRecoveredView);
                    onCheckboxClicked(isRecoveredView);
                    onCheckboxClicked(isLastUpdatedView);
                    addConfirmed = true;

                    openMainActivity();

                } else {
                    mainAddButton.setTextColor(0xFF0000);
                }


            }
        });

        // IMAGEVIEW SETONCLICKLISTENER COMEBACK TO main_activity

        ImageView arrowBack = findViewById(R.id.imageViewArrowBack);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finishThisActivity();
            }
        });
    }



    private void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkedConfirmed:
                isConfirmed = checked;
                break;
            case R.id.checkedDeaths:
                isDeaths = checked;
                break;
            case R.id.checkedNewDeaths:
                isNewDeaths = checked;
                break;
            case R.id.checkedNewConfirmed:
                isNewConfirmed = checked;
                break;
            case R.id.checkedNewRecovered:
                isNewRecovered = checked;
                break;
            case R.id.checkedRecovered:
                isRecovered = checked;
                break;
            case R.id.checkedLastUpdated:
                isLastUpdated = checked;
                break;
        }
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);


        intent.putExtra("checkBoxConfirmed", isConfirmed);
        intent.putExtra("checkBoxNewConfirmed", isNewConfirmed);
        intent.putExtra("checkBoxDeaths", isDeaths);
        intent.putExtra("checkBoxNewDeaths", isNewDeaths);
        intent.putExtra("checkBoxRecovered", isRecovered);
        intent.putExtra("checkBoxNewRecovered", isNewRecovered);
        intent.putExtra("checkBoxLastUpdated", isLastUpdated);

        intent.putExtra("selectedCountry",  selectedCountry);

        intent.putExtra("addButtonPressed", addConfirmed);
        intent.putExtra("isChoice", true);

        startActivity(intent);

//        finish();

    }

    private void finishThisActivity() {
        super.onBackPressed();

    }

}