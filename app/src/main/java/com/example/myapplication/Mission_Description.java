package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import Database.DatabaseConnection;
import Database.Missions.MissionDataAccess;

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
        String period;
        switch (id) {
            case 7:
                if (getMissionDifficult(id) == 1) {
                    period = getResources().getString(R.string.avatar_level1);
                } else if (getMissionDifficult(id) == 2) {
                    period = getResources().getString(R.string.avatar_level2);
                } else {
                    period = getResources().getString(R.string.avatar_level3);
                }
                break;
            case 13:
                if (getMissionDifficult(id) == 1) {
                    period = getResources().getString(R.string.times_level1);
                } else if (getMissionDifficult(id) == 2) {
                    period = getResources().getString(R.string.times_level2);
                } else {
                    period = getResources().getString(R.string.times_level3);
                }
                break;
            case 18:
                if (getMissionDifficult(id) == 1) {
                    period = getResources().getString(R.string.monsters_level1);
                } else if (getMissionDifficult(id) == 2) {
                    period = getResources().getString(R.string.monsters_level2);
                } else {
                    period = getResources().getString(R.string.monsters_level3);
                }
                break;
            case 19:
                if (getMissionDifficult(id) == 1) {
                    period = getResources().getString(R.string.tips_level1);
                } else if (getMissionDifficult(id) == 2) {
                    period = getResources().getString(R.string.tips_level2);
                } else {
                    period = getResources().getString(R.string.tips_level3);
                }
                break;
            default:
                if (getMissionDifficult(id) == 1) {
                    period = getResources().getString(R.string.days_level1);
                } else if (getMissionDifficult(id) == 2) {
                    period = getResources().getString(R.string.days_level2);
                } else {
                    period = getResources().getString(R.string.days_level3);
                }
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