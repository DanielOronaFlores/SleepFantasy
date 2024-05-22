package Models;

import android.content.Context;

import AppContext.MyApplication;

public class PlaylistAudios {
private final Context context;

    public PlaylistAudios() {
        context = MyApplication.getAppContext();
    }

    public int getResourceId(String audioName) {
        String resourceName = "music_" + audioName.trim().toLowerCase().replace(" ", "");
        return context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
    }
}