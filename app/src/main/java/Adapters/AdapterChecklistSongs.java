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

import Models.Song;
import Utils.ListSongUtil;

public class AdapterChecklistSongs extends RecyclerView.Adapter<AdapterChecklistSongs.ViewHolder> {
    private final List<Song> songs;
    private final List<Boolean> checkedList;

    public AdapterChecklistSongs(List<Song> songs) {
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
        holder.setSongs(songs.get(position).getName());
        holder.checkBox.setChecked(checkedList.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Song> getSelectedSongs() {
        ListSongUtil listSongUtil = new ListSongUtil();
        return listSongUtil.getSelectedSongs(songs, checkedList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.songElement);
            checkBox = itemView.findViewById(R.id.songCheckBox);

            checkBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedList.set(position, checkBox.isChecked());
                }
            });
        }

        public void setSongs(String songName) {
            this.songName.setText(songName);
        }
    }
}
