package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import Dates.DateManager;

public class chartSelector extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_selector);

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
                showSelectedInformation(selectedFilter);
                if (selectedFilter.equals("Dia")) spinnerData.setAdapter(adapterDataPie);
                else spinnerData.setAdapter(adapterDataBar);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        spinnerFilter.setAdapter(adapterFilter);

        spinnerData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedData = (String) parentView.getItemAtPosition(position);
                showSelectedInformation(selectedData);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        spinnerData.setAdapter(adapterDataPie);

        String date = chooseDate();
        Button btnSelect = findViewById(R.id.btnConsultar);
        btnSelect.setOnClickListener(view -> {
            int filter = spinnerFilter.getSelectedItemPosition();
            int data = spinnerData.getSelectedItemPosition();
            goToVisualizer(data, filter, date);
        });
    }

    private void goToVisualizer(int data, int filter, String date) {
        if (filter == 0) goToPieVisualizer(date);
        //TODO: Implementar visualizador de barras
    }

    private void goToPieVisualizer(String date) {
        Intent intent = new Intent(this, chartPieVisualizer.class);
        intent.putExtra("date", date);
        startActivity(intent);
    }

    private void showSelectedInformation(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private String chooseDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year1;
            Toast.makeText(chartSelector.this, "Fecha seleccionada: " + date, Toast.LENGTH_SHORT).show();
        }, year, month, day);

        datePickerDialog.show();

        String date = year + "-" + (month + 1) + "-" + (day - 1);
        DateManager dateManager = new DateManager();
        date = dateManager.formatDate(date);
        Log.d("date", date);
        return date;
    }
}