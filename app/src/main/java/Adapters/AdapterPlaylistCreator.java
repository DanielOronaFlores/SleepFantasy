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

import Music.Song;

public class AdapterPlaylistCreator extends RecyclerView.Adapter<AdapterPlaylistCreator.ViewHolder> {
    private List<Song> songs;
    private List<Boolean> checkedList;

    public AdapterPlaylistCreator(List<Song> songs) {
        this.songs = songs;
        checkedList = new ArrayList<>(songs.size());
        for (int i = 0; i < songs.size(); i++) {
            checkedList.add(false);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.songElement);
            checkBox = itemView.findViewById(R.id.songCheckBox);

            checkBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedList.set(position, checkBox.isChecked());
                }
            });
        }

        public void setSongs(String s) {
            textView.setText(s);
        }
    }

    @NonNull
    @Override
    public AdapterPlaylistCreator.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_creator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlaylistCreator.ViewHolder holder, int position) {
        holder.setSongs(songs.get(position).getName());
        holder.checkBox.setChecked(checkedList.get(position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public List<Song> getSelectedSongs() {
        List<Song> selectedSongs = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            if (checkedList.get(i)) {
                selectedSongs.add(songs.get(i));
            }
        }
        return selectedSongs;
    }
}
