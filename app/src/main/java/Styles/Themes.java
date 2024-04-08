package Styles;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class Themes {
    public static void setBackgroundColor(Context context, View view) {
        DatabaseConnection connection = DatabaseConnection.getInstance( context);
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);

        switch (preferencesDataAccess.getThemeSelected()) {
            case 0:
               view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_purple));
                break;
            case 1:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_red));
                break;
        }
    }

    public static void setButtonTheme(Context context, Button button) {
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);

        switch (preferencesDataAccess.getThemeSelected()) {
            case 0:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_purple));
                break;
            case 1:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_red));
                break;
        }
    }

    public static void setButtonDataTheme(Context context, Button button) {
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);

        switch (preferencesDataAccess.getThemeSelected()) {
            case 0:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_purple));
                break;
            case 1:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_red));
                break;
        }
    }

    public static void setChallengeTextViewTheme(Context context, TextView textView) {
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);

        switch (preferencesDataAccess.getThemeSelected()) {
            case 0:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_purple));
                break;
            case 1:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_red));
                break;
        }
    }
}
