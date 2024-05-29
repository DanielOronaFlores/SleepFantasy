package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import AppContext.MyApplication;
import Database.DataAccess.TipsDataAccess;
import Database.DataUpdates.TipsDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Missions.MissionsUpdater;

public class TipFragment extends DialogFragment {
    private DatabaseConnection connection;
    private TipsDataAccess tipsDataAccess;
    private TipsDataUpdate tipsDataUpdate;
    private MissionsUpdater missionsUpdater;
    private TextView tipText;
    private Button confirmButton;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_tip, null);

        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        tipsDataAccess = new TipsDataAccess(connection);
        tipsDataUpdate = new TipsDataUpdate(connection);
        missionsUpdater = new MissionsUpdater();
        tipText = view.findViewById(R.id.tip_dialog_text);
        confirmButton = view.findViewById(R.id.tip_dialog_button);

        String tip = getCurrentTip();
        if (tip == null) dismiss();

        tipText.setText(tip);

        confirmButton.setOnClickListener(v -> {
            tipsDataUpdate.updateDisplayed(true);
            missionsUpdater.updateMission19();
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

    private String getCurrentTip() {
        int currentType = tipsDataAccess.getCurrentTipType();
        System.out.println("Current type: " + currentType);
        String[] tips = switch (currentType) {
            case 1 -> getResources().getStringArray(R.array.tips_sleepEvents);
            case 2 -> getResources().getStringArray(R.array.tips_sleepTime);
            case 3 -> getResources().getStringArray(R.array.tips_sleepEnvironment);
            default -> null;
        };

        if (tips == null) return null;

        int currentTip = tipsDataAccess.getCurrentTipId();
        System.out.println("Current tip: " + currentTip);
        return tips[currentTip];
    }
}
