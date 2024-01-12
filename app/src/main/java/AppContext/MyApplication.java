package AppContext;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext(); // Get the context of the application
    }

    public static Context getAppContext() {
        return context;
    }
}
