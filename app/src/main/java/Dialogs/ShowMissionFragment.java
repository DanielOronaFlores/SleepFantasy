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
import Styles.Themes;

public class ShowMissionFragment extends DialogFragment {
    private DatabaseConnection connection;
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

        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        missionDataAccess = new MissionDataAccess(connection);

        missionDescription = view.findViewById(R.id.missionDescription);
        missionPeriod = view.findViewById(R.id.missionPeriod);

        missionDescription.setText(getMissionDescription(missionId));
        missionPeriod.setText(getMissionPeriod(missionId));

        Themes.setBackgroundColor(getActivity(), view);

        builder.setView(view);
        return builder.create();
    }

    private int getMissionDifficult(int missionId) {
        return missionDataAccess.getCurrentDifficult(missionId);
    }
    private String getMissionPeriod(int id) {
        int difficulty = getMissionDifficult(id);
        int resourceId = switch (id) {
            case 7 -> R.string.avatar_level1;
            case 13 -> R.string.times_level1;
            case 18 -> R.string.monsters_level1;
            case 19 -> R.string.tips_level1;
            default -> R.string.days_level1;
        };

        if (difficulty == 2) resourceId = resourceId + 1;
        else if (difficulty == 3) resourceId = resourceId + 2;

        return getResources().getString(resourceId);
    }
    private String getMissionDescription(int id) {
        int resourceId = switch (id) {
            case 1 -> R.string.mission1;
            case 2 -> R.string.mission2;
            case 3 -> R.string.mission3;
            case 4 -> R.string.mission4;
            case 5 -> R.string.mission5;
            case 6 -> R.string.mission6;
            case 7 -> R.string.mission7;
            case 8 -> R.string.mission8;
            case 9 -> R.string.mission9;
            case 10 -> R.string.mission10;
            case 11 -> R.string.mission11;
            case 12 -> R.string.mission12;
            case 13 -> R.string.mission13;
            case 14 -> R.string.mission14;
            case 15 -> R.string.mission15;
            case 16 -> R.string.mission16;
            case 17 -> R.string.mission17;
            case 18 -> R.string.mission18;
            case 19 -> R.string.mission19;
            case 20 -> R.string.mission20;
            default -> R.string.missionDefault;
        };
        return getResources().getString(resourceId);
    }
}