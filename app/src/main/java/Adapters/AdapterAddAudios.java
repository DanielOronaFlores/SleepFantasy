package Adapters;

import android.annotation.SuppressLint;
import android.media.MediaMetadataRetriever;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import AppContext.MyApplication;
import Database.DataAccess.AudiosDataAccess;
import Database.DatabaseConnection;
import Files.AudiosPaths;

public class AdapterAddAudios extends RecyclerView.Adapter<AdapterAddAudios.ViewHolder> {
    private static List<Boolean> checkedList;
    private final List<String> audiosFileName;

    public AdapterAddAudios() {
        audiosFileName = new ArrayList<>();
        initializeAudioFilesList();

        checkedList = new ArrayList<>(audiosFileName.size());
        for (int i = 0; i < audiosFileName.size(); i++) {
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
        holder.setAudios(audiosFileName.get(position));
        holder.checkBox.setChecked(checkedList.get(position));
    }
    @Override
    public int getItemCount() {
        return audiosFileName.size();
    }

    public List<String> getSelectedAudios() {
        List<String> selectedAudios = new ArrayList<>();
        for (int i = 0; i < audiosFileName.size(); i++) {
            if (checkedList.get(i)) {
                selectedAudios.add(audiosFileName.get(i));
            }
        }
        return selectedAudios;
    }

    private void initializeAudioFilesList() {
        File folder = new File(AudiosPaths.getMusicPath());
        System.out.println("Exits: " + folder.exists() + " Is directory: " + folder.isDirectory());
        if (folder.exists() && folder.isDirectory()) {
            File[] audioFiles = folder.listFiles();
            System.out.println("Audio files: ");
            System.out.println(Arrays.toString(audioFiles));

            if (audioFiles != null) {
                System.out.println("Audio files: ");
                getAudioFilesFromDevice(audioFiles);
                System.out.println(audiosFileName);

                System.out.println("Deleted audios already in database");
                deletedAudiosAlreadyInDatabase();
                System.out.println(audiosFileName);

                System.out.println("Deleted audios not in range");
                deleteAudiosNotInRange();
                System.out.println(audiosFileName);
            }
        }
    }

    private void getAudioFilesFromDevice(File[] audioFiles) {
        for (File audioFile : audioFiles) {
            if (audioFile.getName().endsWith(".mp3")){
                audiosFileName.add(audioFile.getName());
            }
        }
    }
    private void deletedAudiosAlreadyInDatabase() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        AudiosDataAccess AudiosDataAccess = new AudiosDataAccess(connection);
        List<String> audiosInDatabase = AudiosDataAccess.getAudiosNotCreatedBySystem();
        audiosFileName.removeIf(audiosInDatabase::contains);
    }
    @SuppressLint("NewApi")
    private void deleteAudiosNotInRange() {
        Iterator<String> iterator = audiosFileName.iterator();

        while (iterator.hasNext()) {
            String audioPath = AudiosPaths.getMusicPath() + iterator.next();
            File audioFile = new File(audioPath);

            try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
                retriever.setDataSource(audioPath);
                if (audioFile.exists()) {
                    long audioDuration = Long.parseLong(Objects.requireNonNull(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                    if (audioDuration < 10000 || audioDuration > 600000) {
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView audioName;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioElement);
            checkBox = itemView.findViewById(R.id.audioCheckBox);

            checkBox.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedList.set(position, checkBox.isChecked());
                }
            });
        }

        private void setAudios(String audioName) {
            audioName = audioName.substring(0, Math.min(audioName.length(), 7));
            this.audioName.setText(audioName);
        }
    }
}
