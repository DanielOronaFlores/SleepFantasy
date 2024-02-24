package Music;

import android.content.Context;

import AppContext.MyApplication;

public class PlaylistSongs {
private Context context;

    public PlaylistSongs() {
        MyApplication myApplication = new MyApplication();
        context = myApplication.getAppContext();
    }

    public int getResourceId(String songName) {
        String resourceName = "music_" + songName.trim().toLowerCase().replace(" ", "");
        return context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
    }
}
