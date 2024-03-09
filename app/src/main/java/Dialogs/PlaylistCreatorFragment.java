package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.List;

import AppContext.MyApplication;
import Database.DataAccess.PlaylistDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DataUpdates.PlaylistSongsDataUpdate;
import Database.DatabaseConnection;
import Models.Song;

public class PlaylistCreatorFragment extends DialogFragment {
    private List<Song> selectedSongs;
    private Context context;
    private PlaylistDataAccess playlistDataAccess;
    private PlaylistDataUpdate playlistDataUpdate;
    private PlaylistSongsDataUpdate playlistSongsDataUpdate;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_playlist_creator, null);

        context = MyApplication.getAppContext();
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        connection.openDatabase();
        playlistDataAccess = new PlaylistDataAccess(connection);
        playlistDataUpdate = new PlaylistDataUpdate(connection);
        playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);

        EditText playlistName = view.findViewById(R.id.playlistName);
        Button createPlaylist = view.findViewById(R.id.createPlaylist);

        createPlaylist.setOnClickListener(v -> {
            if (isAnySongSelected()) {
                createPlaylist(playlistName.getText().toString());
                dismiss();
            } else {
                Toast toast = Toast.makeText(context, "No hay musicas seleccionadas", Toast.LENGTH_SHORT);
                toast.show();
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public void setSelectedSongs(List<Song> selectedSongs) {
        this.selectedSongs = selectedSongs;
    }

    private boolean isAnySongSelected() {
        return selectedSongs.size() > 0;
    }
    private void createPlaylist(String playlistName) {
        if (playlistDataAccess.isPlaylistCreated(playlistName)) {
            Toast toast = Toast.makeText(context, "Playlist ya existe", Toast.LENGTH_SHORT);
            toast.show();
        } else if (playlistName.isEmpty()) {
                Toast toast = Toast.makeText(context, "Nombre de playlist vacio", Toast.LENGTH_SHORT);
                toast.show();
        } else {
            playlistDataUpdate.createPlaylist(playlistName);
            int playlistId = playlistDataAccess.getPlaylistId(playlistName);
            for (Song song : selectedSongs) {
                playlistSongsDataUpdate.addSongToPlaylist(playlistId, song.getId());
            }
            Toast toast = Toast.makeText(context, "Playlist creada", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
