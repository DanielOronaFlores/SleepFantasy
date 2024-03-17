package Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import AppContext.MyApplication;
import Database.DataAccess.SongsDataAccess;
import Database.DatabaseConnection;

public class AdapterAddAudios extends RecyclerView.Adapter<AdapterAddAudios.ViewHolder> {
    private static List<Boolean> checkedList;
    private final List<String> audios = new ArrayList<>();


    public AdapterAddAudios() {
        initializeAudioFilesList();
        checkedList = new ArrayList<>(audios.size());
        for (int i = 0; i < audios.size(); i++) {
            checkedList.add(false);
        }
    }

    @NonNull
    @Override
    public AdapterAddAudios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_creator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAddAudios.ViewHolder holder, int position) {
        holder.setAudios(audios.get(position));
        holder.checkBox.setChecked(checkedList.get(position));
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }

    public List<String> getSelectedAudios() {
        List<String> selectedAudios = new ArrayList<>();
        for (int i = 0; i < audios.size(); i++) {
            if (checkedList.get(i)) {
                selectedAudios.add(audios.get(i));
            }
        }
        return selectedAudios;
    }

    @SuppressLint("SdCardPath")
    private void initializeAudioFilesList() {
        File folder = new File("/sdcard/Music"); //
        Log.d("Files", "Path: " + folder.getAbsolutePath());

        if (folder.exists() && folder.isDirectory()) {
            File[] audioFiles = folder.listFiles();

            if (audioFiles != null) {
                getAudioFilesFromDevice(audioFiles);
                deletedAudiosAlreadyInDatabase();
                //deleteAudiosNotInRange();
            }
        }
    }

    private void getAudioFilesFromDevice(File[] audioFiles) {
        for (File audioFile : audioFiles) {
            if (audioFile.getName().endsWith(".MP3")){
                String audioName = audioFile.getName();
                audios.add(audioName);
            }
        }
    }
    private void deletedAudiosAlreadyInDatabase() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        SongsDataAccess songsDataAccess = new SongsDataAccess(connection);
        List<String> audiosInDatabase = songsDataAccess.getSongsNotCreatedBySystem();
        audios.removeIf(audiosInDatabase::contains);
    }
    private void deleteAudiosNotInRange() {
        Iterator<String> iterator = audios.iterator();

        while (iterator.hasNext()) {
            String audio = iterator.next();
            String audioPath = "/sdcard/Music/" + audio;
            File audioFile = new File(audioPath);
            if (audioFile.exists()) {
                long audioDuration = audioFile.length();
                if (audioDuration < 30000 || audioDuration > 600000) {
                    iterator.remove();
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView audioName;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.songElement);
            checkBox = itemView.findViewById(R.id.songCheckBox);

            checkBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedList.set(position, checkBox.isChecked());
                }
            });
        }

        private void setAudios(String audioName) {
            String shortAudioName = audioName.substring(0, Math.min(audioName.length(), 7));
            this.audioName.setText(shortAudioName);
        }
    }
}
