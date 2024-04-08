package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.PlaylistVisualizer;

import java.util.List;

import Models.Playlist;
import Styles.Themes;

public class AdapterPlaylists extends RecyclerView.Adapter<AdapterPlaylists.ViewHolder>{
    private final List<Playlist> playlists;

    public AdapterPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public AdapterPlaylists.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_playlist_selector, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterPlaylists.ViewHolder holder, int position) {
        holder.setPlaylist(playlists.get(position).getName());
        holder.button.setOnClickListener(v -> selectPlaylist(v.getContext(), playlists.get(position).getId()));
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    private void selectPlaylist(Context context, int playlistID) {
        Intent intent = new Intent(context, PlaylistVisualizer.class);
        intent.putExtra("playlistID", playlistID);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.selectPlaylist);
        }
        public void setPlaylist(String s) {
            button.setText(s);
            Themes.setButtonTheme(button.getContext(), button);
        }
    }
}
