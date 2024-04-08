package AppContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.R;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static void setTheme(Activity activity, int appTheme) {
        switch (appTheme) {
            case 1:
                System.out.println("Tema 1");
                break;
            default:
                //activity.setTheme(R.style.);
                break;
        }

        Intent intent = new Intent(activity, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }
}
