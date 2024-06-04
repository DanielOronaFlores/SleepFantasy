package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import Dates.DateManager;
import Dialogs.SelectDateFragment;
import Styles.Themes;

public class ChartSelector extends AppCompatActivity {
    private Button btDate, btnConsult;

    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_selector);

        btDate = findViewById(R.id.chartDate);
        btDate.setOnClickListener(view -> chooseDate());

        Spinner spinnerFilter = findViewById(R.id.spinnerTipoFiltro);
        Spinner spinnerData = findViewById(R.id.spinnerDato);

        ArrayAdapter<CharSequence> adapterFilter = ArrayAdapter.createFromResource(
                this,
                R.array.filter,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDataPie = ArrayAdapter.createFromResource(
                this,
                R.array.pie,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterDataBar = ArrayAdapter.createFromResource(
                this,
                R.array.bar,
                android.R.layout.simple_spinner_item);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedFilter = (String) parentView.getItemAtPosition(position);
                if (selectedFilter.equals("Dia")) spinnerData.setAdapter(adapterDataPie);
                else spinnerData.setAdapter(adapterDataBar);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        spinnerFilter.setAdapter(adapterFilter);

        spinnerData.setAdapter(adapterDataPie);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        if (date == null) {
            date = DateManager.getCurrentDate();
        }
        btDate.setText(date);

        btnConsult = findViewById(R.id.chartConsult);
        btnConsult.setOnClickListener(view -> {
            int filter = spinnerFilter.getSelectedItemPosition();
            int data = spinnerData.getSelectedItemPosition();
            goToVisualizer(data, filter, date);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme();
    }

    private void goToVisualizer(int data, int filter, String date) {
        if (filter == 0) goToPieVisualizer(date);
        else goToBarVisualizer(data, filter, date);
    }

    private void goToPieVisualizer(String date) {
        Intent intent = new Intent(this, ChartPieVisualizer.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }
    private void goToBarVisualizer(int data, int filter, String date) {
        Intent intent = new Intent(this, ChartBarVisualizer.class);
        intent.putExtra("data", data);
        intent.putExtra("filter", filter);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    private void chooseDate() {
        SelectDateFragment selectDate = new SelectDateFragment();
        selectDate.show(getSupportFragmentManager(), "Select Date");
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.chartSelector));
        Themes.setButtonTheme(this, btnConsult);
        Themes.setButtonDataTheme(this, btDate);
    }
}