package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import Dates.DateManager;

public class SelectDate extends DialogFragment {
    Context context = AppContext.MyApplication.getAppContext();
    DatePicker datePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_picker, null);

        datePicker = view.findViewById(R.id.datePicker);

        Button btnSelectDate = view.findViewById(R.id.okButton);
        btnSelectDate.setOnClickListener(v -> {
            String date = selectDate();
            showSelectedDate(date);
            returnSelector(date);
        });
        builder.setView(view);
        return builder.create();
    }

    private String selectDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        DateManager dateManager = new DateManager();
        return dateManager.formatDate(year + "-" + (month + 1) + "-" + day);
    }

    private void returnSelector(String date) {
        Intent intent = new Intent(context, com.example.myapplication.chartSelector.class);
        intent.putExtra("date", date);
        Log.d("SelectDate", "returnSelector: " + date);
        startActivity(intent);
    }

    private void showSelectedDate(String date) {
        Toast.makeText(context, date, Toast.LENGTH_SHORT).show();
    }
}