package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import Models.Audio;
import Utils.ListSongUtil;

public class AdapterChecklistSongs extends RecyclerView.Adapter<AdapterChecklistSongs.ViewHolder> {
    private final List<Audio> songs;
    private final List<Boolean> checkedList;

    public AdapterChecklistSongs(List<Audio> songs) {
        this.songs = songs;
        checkedList = new ArrayList<>(songs.size());
        for (int i = 0; i < songs.size(); i++) {
            checkedList.add(false);
        }
    }

    @NonNull
    @Override
    public AdapterChecklistSongs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_creator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChecklistSongs.ViewHolder holder, int position) {
        String shortName = songs.get(position).getName();
        if (shortName.length() > 7) {
            shortName = shortName.substring(0, 7) + "...";
        }

        holder.setSongs(shortName);
        holder.checkBox.setChecked(checkedList.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Audio> getSelectedSongs() {
        ListSongUtil listSongUtil = new ListSongUtil();
        return listSongUtil.getSelectedSongs(songs, checkedList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;
        CheckBox checkBox;
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

        public void setSongs(String songName) {
            this.audioName.setText(songName);
        }
    }
}
