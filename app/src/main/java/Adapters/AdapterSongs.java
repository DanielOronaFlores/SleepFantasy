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

import Songs.Song;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.ViewHolder> {
    private List<Song> songs;
    private List<Boolean> checkedList;

    public AdapterSongs(List<Song> songs) {
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

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        checkedList.set(position, checkBox.isChecked());
                    }
                }
            });
        }

        public void setSongs(String s) {
            textView.setText(s);
        }
    }

    @NonNull
    @Override
    public AdapterSongs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_song_selector, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSongs.ViewHolder holder, int position) {
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
