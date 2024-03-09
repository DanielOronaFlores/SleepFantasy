package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import AppContext.MyApplication;
import Database.DataAccess.MissionDataAccess;
import Database.DatabaseConnection;

public class ShowMissionFragment extends DialogFragment {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
    private TextView missionDescription, missionPeriod;
    private MissionDataAccess missionDataAccess;
    private final int missionId;

    public ShowMissionFragment(int missionId) {
        this.missionId = missionId;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_mission, null);

        connection.openDatabase();
        missionDataAccess = new MissionDataAccess(connection);
        missionDescription = view.findViewById(R.id.missionDescription);
        missionPeriod = view.findViewById(R.id.missionPeriod);


        missionDescription.setText(getMissionDescription(missionId));
        missionPeriod.setText(getMissionPeriod(missionId));

        builder.setView(view);
        return builder.create();
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