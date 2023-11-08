package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Mission_Description extends Fragment {
    private TextView missionDescription;
    private TextView missionPeriod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mission__description, container, false);
        missionDescription = view.findViewById(R.id.missionDescription);
        missionPeriod = view.findViewById(R.id.missionPeriod);
        Bundle args = getArguments();

        int missionId = args.getInt("missionId", -1);
        missionDescription.setText(getMissionDescription(missionId));
        missionPeriod.setText(getMissionPeriod(missionId));

        return view;
    }

    private String getMissionDifficultText() {
        String difficultText = "";
        return difficultText;
    }

    private String getMissionPeriod(int id) {
        String period;
        switch (id) {
            case 7:
                period = getResources().getString(R.string.avatar_level1);
                break;
            case 13:
                period = getResources().getString(R.string.times_level1);
                break;
            case 18:
                period = getResources().getString(R.string.monsters_level1);
                break;
            case 19:
                period = getResources().getString(R.string.tips_level1);
                break;
            default:
                period = getResources().getString(R.string.days_level1);
                break;
        }
        return period;
    }

    private String getMissionDescription(int id) {
        String mission;
        switch (id) {
            case 1:
                mission = getResources().getString(R.string.mission1);
                break;
            case 2:
                mission = getResources().getString(R.string.mission2);
                break;
            case 3:
                mission = getResources().getString(R.string.mission3);
                break;
            case 4:
                mission = getResources().getString(R.string.mission4);
                break;
            case 5:
                mission = getResources().getString(R.string.mission5);
                break;
            case 6:
                mission = getResources().getString(R.string.mission6);
                break;
            case 7:
                mission = getResources().getString(R.string.mission7);
                break;
            case 8:
                mission = getResources().getString(R.string.mission8);
                break;
            case 9:
                mission = getResources().getString(R.string.mission9);
                break;
            case 10:
                mission = getResources().getString(R.string.mission10);
                break;
            case 11:
                mission = getResources().getString(R.string.mission11);
                break;
            case 12:
                mission = getResources().getString(R.string.mission12);
                break;
            case 13:
                mission = getResources().getString(R.string.mission13);
                break;
            case 14:
                mission = getResources().getString(R.string.mission14);
                break;
            case 15:
                mission = getResources().getString(R.string.mission15);
                break;
            case 16:
                mission = getResources().getString(R.string.mission16);
                break;
            case 17:
                mission = getResources().getString(R.string.mission17);
                break;
            case 18:
                mission = getResources().getString(R.string.mission18);
                break;
            case 19:
                mission = getResources().getString(R.string.mission19);
                break;
            case 20:
                mission = getResources().getString(R.string.mission20);
                break;
            default:
                mission = getResources().getString(R.string.missionDefault);
        }
        return mission;
    }
}