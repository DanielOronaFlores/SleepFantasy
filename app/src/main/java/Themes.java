import android.app.Activity;

import com.example.myapplication.R;

import Database.DataAccess.PreferencesDataAccess;

public class Themes {
    private void setBackgroundColor(Activity activity, int layoutResID, PreferencesDataAccess preferencesDataAccess) {
        switch (preferencesDataAccess.getThemeSelected()) {
            case 0:
                activity.findViewById(layoutResID).setBackgroundColor(activity.getResources().getColor(R.color.layout_background_purple));
            case 1:
                activity.findViewById(layoutResID).setBackgroundColor(activity.getResources().getColor(R.color.layout_background_red));
                break;
        }
    }
}
