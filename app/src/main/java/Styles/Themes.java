package Styles;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class Themes {
    public static void setBackgroundColor(Context context, View view) {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(context));

        switch (preferencesDataAccess.getThemeSelected()) {
            case 1:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_red));
                break;
            case 2:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_blue));
                break;
            case 3:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_green));
                break;
            case 4:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_yellow));
                break;
            case 5:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_brown));
                break;
            default:
                view.setBackgroundColor(context.getResources().getColor(R.color.layout_background_purple));
                break;
        }
    }

    public static void setButtonTheme(Context context, Button button) {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(context));

        switch (preferencesDataAccess.getThemeSelected()) {
            case 1:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_red));
                break;
            case 2:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_blue));
                break;
            case 3:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_green));
                break;
            case 4:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_yellow));
                break;
            case 5:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_brown));
                break;
            default:
                button.setBackgroundColor(context.getResources().getColor(R.color.button_background_purple));
                break;
        }
    }

    public static void setButtonDataTheme(Context context, Button button) {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(context));

        switch (preferencesDataAccess.getThemeSelected()) {
          case 1:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_red));
                break;
            case 2:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_blue));
                break;
            case 3:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_green));
                break;
            case 4:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_yellow));
                break;
            case 5:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_brown));
                break;
            default:
                button.setBackgroundColor(context.getResources().getColor(R.color.buttonData_background_purple));
                break;
        }
        button.setTextColor(context.getResources().getColor(R.color.white));
    }

    public static void setChallengeTextViewTheme(Context context, TextView textView) {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(context));

        switch (preferencesDataAccess.getThemeSelected()) {
            case 1:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_red));
                break;
            case 2:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_blue));
                break;
            case 3:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_green));
                break;
            case 4:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_yellow));
                break;
            case 5:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_brown));
                break;
            default:
                textView.setBackgroundColor(context.getResources().getColor(R.color.layout_background_purple));
                break;
        }
    }
}
