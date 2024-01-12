package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import Database.DatabaseConnection;
import DataAccess.MissionDataAccess;

public class Mission_Description extends Fragment {
    private DatabaseConnection connection;
    private MissionDataAccess missionDataAccess;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mission__description, container, false);
        TextView missionDescription = view.findViewById(R.id.missionDescription);
        TextView missionPeriod = view.findViewById(R.id.missionPeriod);
        Bundle args = getArguments();

        assert args != null;
        int missionId = args.getInt("missionId", -1);
        missionDescription.setText(getMissionDescription(missionId));
        missionPeriod.setText(getMissionPeriod(missionId));

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = DatabaseConnection.getInstance(getContext());
        connection.openDatabase();
        missionDataAccess = new MissionDataAccess(connection);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private int getMissionDifficult(int missionId) {
        return missionDataAccess.getCurrentDifficult(missionId);
    }
    private String getMissionPeriod(int id) {
        int difficulty = getMissionDifficult(id);
        int resourceId;

        switch (id) {
            case 7:
                resourceId = R.string.avatar_level1;
                break;
            case 13:
                resourceId = R.string.times_level1;
                break;
            case 18:
                resourceId = R.string.monsters_level1;
                break;
            case 19:
                resourceId = R.string.tips_level1;
                break;
            default:
                resourceId = R.string.days_level1;
        }

        if (difficulty == 2) resourceId = resourceId + 1;
        else if (difficulty == 3) resourceId = resourceId + 2;

        return getResources().getString(resourceId);
    }

    private String getMissionDescription(int id) {
        int resourceId;

        switch (id) {
            case 1: resourceId = R.string.mission1; break;
            case 2: resourceId = R.string.mission2; break;
            case 3: resourceId = R.string.mission3; break;
            case 4: resourceId = R.string.mission4; break;
            case 5: resourceId = R.string.mission5; break;
            case 6: resourceId = R.string.mission6; break;
            case 7: resourceId = R.string.mission7; break;
            case 8: resourceId = R.string.mission8; break;
            case 9: resourceId = R.string.mission9; break;
            case 10: resourceId = R.string.mission10; break;
            case 11: resourceId = R.string.mission11; break;
            case 12: resourceId = R.string.mission12; break;
            case 13: resourceId = R.string.mission13; break;
            case 14: resourceId = R.string.mission14; break;
            case 15: resourceId = R.string.mission15; break;
            case 16: resourceId = R.string.mission16; break;
            case 17: resourceId = R.string.mission17; break;
            case 18: resourceId = R.string.mission18; break;
            case 19: resourceId = R.string.mission19; break;
            case 20: resourceId = R.string.mission20; break;
            default: resourceId = R.string.missionDefault;
        }
        return getResources().getString(resourceId);
    }
}