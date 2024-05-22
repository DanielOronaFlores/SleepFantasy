package Adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.Serializable;
import java.util.List;

import Models.Audio;
import Services.AudioPlayer;
import Styles.Themes;

public class AdapterAudios extends RecyclerView.Adapter<AdapterAudios.ViewHolder> {
    private final List<Audio> audios;
    private final Activity activity;

    public AdapterAudios(List<Audio> Audios, Activity activity) {
        this.audios = Audios;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterAudios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_song_selector, parent, false);

        IntentFilter filter = new IntentFilter("RECREAR_ACTIVIDAD");
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, filter);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterAudios.ViewHolder holder, int position) {
        holder.setAudios(audios.get(position).getName());
        holder.button.setOnClickListener(v -> {

            Intent intent = new Intent(activity, AudioPlayer.class);
            intent.putExtra("audios", (Serializable) audios);
            intent.putExtra("position", position);
            intent.putExtra("activity", activity.getClass().getSimpleName());

            activity.startService(intent);
        });
    }
    @Override
    public int getItemCount() {
        return audios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.selectaudio);
        }
        public void setAudios(String s) {
            button.setText(s);
            Themes.setButtonDataTheme(button.getContext(), button);
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RECREAR_ACTIVIDAD".equals(intent.getAction())) {
                activity.recreate();
            }
        }
    };
}
