package Models;

import android.content.Context;

import AppContext.MyApplication;

public class PlaylistAudios {
private Context context;

    public PlaylistAudios() {
        MyApplication myApplication = new MyApplication();
        context = myApplication.getAppContext();
    }

    public int getResourceId(String audioName) {
        String resourceName = "music_" + audioName.trim().toLowerCase().replace(" ", "");
        return context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
    }
}
