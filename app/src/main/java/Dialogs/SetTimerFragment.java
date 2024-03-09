package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import AppContext.MyApplication;
import Clocker.Clock;
import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class SetTimerFragment extends DialogFragment {
    private PreferencesDataAccess preferencesDataAccess;
    private EditText timerHoursDuration, timerMinutesDuration;
    private DatabaseConnection connection;
    private Clock clock;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_timer_duration, null);

        clock = new Clock();

        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        connection.openDatabase();
        preferencesDataAccess = new PreferencesDataAccess(connection);

        timerHoursDuration = view.findViewById(R.id.timerHoursDuration);
        timerMinutesDuration = view.findViewById(R.id.timerMinutesDuration);

        Button btnSetTimer = view.findViewById(R.id.setTimerButton);
        btnSetTimer.setOnClickListener(v -> {
            int hour = convertStringToNumber(timerHoursDuration.getText().toString());
            int minute = convertStringToNumber(timerMinutesDuration.getText().toString());
            int totalMillis = (hour * 60 * 60 * 1000) + (minute * 60 * 1000);
                preferencesDataAccess.setTimerDuration(totalMillis);
                Toast.makeText(MyApplication.getAppContext(),
                        "Temporizador establecido en: " +
                                clock.convertMillisToHours(totalMillis) + " horas y " +
                                clock.convertMillisToMinutes(totalMillis) + " minutos",
                        Toast.LENGTH_SHORT).show();
                getActivity().recreate();
                dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private int convertStringToNumber(String input) {
        if (input.isEmpty()) return 0;
        return Integer.parseInt(input);
    }
}
