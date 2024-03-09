package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import Dialogs.ShowMissionFragment;
import Models.Mission;

public class AdapterMissions extends RecyclerView.Adapter<AdapterMissions.ViewHolder> {
    private final List<Mission> missions;
    private FragmentManager fragmentManager;

    public AdapterMissions(List<Mission> missions, FragmentManager fragmentManager) {
        this.missions = missions;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AdapterMissions.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_missions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMissions.ViewHolder holder, int position) {
        holder.mission1.setOnClickListener(v -> {
            Mission mission = missions.get(position * 2);
            showMission(mission.getId());
        });
        holder.mission2.setOnClickListener(v -> {
            Mission mission = missions.get((position * 2) + 1);
            showMission(mission.getId());
        });
    }

    @Override
    public int getItemCount() {
        return missions.size() / 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mission1, mission2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mission1 = itemView.findViewById(R.id.mission1);
            mission2 = itemView.findViewById(R.id.mission2);
        }
    }

    private void showMission(int missionId) {
        Log.d("AdapterMissions", "showMission: " + missionId);
        ShowMissionFragment showMission = new ShowMissionFragment(missionId);
        showMission.show(fragmentManager, "showMission");
    }
}
