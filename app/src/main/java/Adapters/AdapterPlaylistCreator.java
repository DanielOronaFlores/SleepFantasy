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
import Utils.GetSelectedAudios;

public class AdapterPlaylistCreator extends RecyclerView.Adapter<AdapterPlaylistCreator.ViewHolder> {
    private final List<Audio> Audios;
    private final List<Boolean> checkedList;

    public AdapterPlaylistCreator(List<Audio> Audios) {
        this.Audios = Audios;
        checkedList = new ArrayList<>(Audios.size());
        for (int i = 0; i < Audios.size(); i++) {
            checkedList.add(false);
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
        String shortName = Audios.get(position).getName();
        if (shortName.length() > 7) {
            shortName = shortName.substring(0, 7) + "...";
        }

        holder.setPlaylistName(shortName);
        holder.checkBox.setChecked(checkedList.get(position));
    }

    @Override
    public int getItemCount() {
        return Audios.size();
    }

    public List<Audio> getSelectedAudios() {
        GetSelectedAudios listaudioUtil = new GetSelectedAudios();
        return listaudioUtil.getSelectedAudios(Audios, checkedList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView audioName;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioElement);

            checkBox = itemView.findViewById(R.id.audioCheckBox);
            checkBox.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    checkedList.set(position, checkBox.isChecked());
                }
            });
        }

        public void setPlaylistName(String audioName) {
            this.audioName.setText(audioName);
        }
    }
}
