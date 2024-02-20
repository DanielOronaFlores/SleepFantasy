package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import Songs.Song;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.ViewHolder> {
    List<Song> songs;

    public AdapterSongs(List<Song> songs) {
        this.songs = songs;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.songElement);
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
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
