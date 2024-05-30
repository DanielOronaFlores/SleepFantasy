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
import Database.DataUpdates.PlaylistAudiosDataUpdate;
import Database.DatabaseConnection;
import Models.Audio;
import Styles.Themes;

public class PlaylistCreatorFragment extends DialogFragment {
    private List<Audio> selectedAudios;
    private PlaylistDataAccess playlistDataAccess;
    private PlaylistDataUpdate playlistDataUpdate;
    private PlaylistAudiosDataUpdate PlaylistAudiosDataUpdate;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_playlist_creator, null);

        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        playlistDataAccess = new PlaylistDataAccess(connection);
        playlistDataUpdate = new PlaylistDataUpdate(connection);
        PlaylistAudiosDataUpdate = new PlaylistAudiosDataUpdate(connection);

        EditText playlistName = view.findViewById(R.id.playlistName);
        Button createPlaylist = view.findViewById(R.id.createPlaylist);

        createPlaylist.setOnClickListener(v -> {
            if (!selectedAudios.isEmpty()) {
                createPlaylist(playlistName.getText().toString());
            } else {
                Toast toast = Toast.makeText(MyApplication.getAppContext(), "No hay musicas seleccionadas", Toast.LENGTH_SHORT);
                toast.show();
            }
            dismiss();
        });

        Themes.setBackgroundColor(getActivity(), view);

        builder.setView(view);
        return builder.create();
    }

    public void setSelectedAudios(List<Audio> selectedAudios) {
        this.selectedAudios = selectedAudios;
    }

    private void createPlaylist(String playlistName) {
        Context context = MyApplication.getAppContext();

        if (playlistDataAccess.isPlaylistCreated(playlistName)) {
            Toast toast = Toast.makeText(context, "Playlist ya existe", Toast.LENGTH_SHORT);
            toast.show();
        } else if (playlistName.isEmpty()) {
                Toast toast = Toast.makeText(context, "Nombre de playlist vacio", Toast.LENGTH_SHORT);
                toast.show();
        } else {
            playlistDataUpdate.createPlaylist(playlistName, false);
            int playlistId = playlistDataAccess.getPlaylistId(playlistName);
            for (Audio audio: selectedAudios) {
                PlaylistAudiosDataUpdate.addaudioToPlaylist(playlistId, audio.getId());
            }
            Toast toast = Toast.makeText(context, "Playlist creada", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
