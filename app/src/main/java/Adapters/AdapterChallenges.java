package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import Styles.Themes;

public class AdapterChallenges extends RecyclerView.Adapter<AdapterChallenges.ViewHolder>{
    private List<String> challenges;

    public AdapterChallenges(List<String> challenges) {
        this.challenges = challenges;
    }

    @NonNull
    @Override
    public AdapterChallenges.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_challenges, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChallenges.ViewHolder holder, int position) {
        holder.setChallenges(challenges.get(position));
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView challenge;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            challenge = itemView.findViewById(R.id.challenge);
        }

        public void setChallenges(String challengeString) {
            challenge.setText(challengeString);
            Themes.setChallengeTextViewTheme(challenge.getContext(), challenge);
        }
    }
}
