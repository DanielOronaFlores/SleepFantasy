package Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.MonsterVisualizer;
import com.example.myapplication.R;

import AppContext.MyApplication;
import Avatar.NameClasses;
import Database.DataAccess.AvatarDataAccess;
import Database.DatabaseConnection;
import Styles.Themes;

public class AvatarInformationFragment extends DialogFragment {
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_avatar_information, null);

        final Context context = MyApplication.getAppContext();
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        connection.openDatabase();
        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);

        TextView txUserData = view.findViewById(R.id.userData);
        TextView txAvatarData = view.findViewById(R.id.avatarData);
        TextView txExperience = view.findViewById(R.id.avatarExperience);

        txUserData.setText(avatarDataAccess.getAvatarName() + " | " + avatarDataAccess.getAvatarAge());

        NameClasses nameClasses = new NameClasses();
        txAvatarData.setText(
                nameClasses.getNameClass(avatarDataAccess.getCharacterClass()) + " - Lvl " +
                String.valueOf(avatarDataAccess.getLevel()));
        txExperience.setText(avatarDataAccess.getCurrentExperience() + "/" + avatarDataAccess.getRequiredExperience());

        Button monsterVisualizer = view.findViewById(R.id.monster);
        monsterVisualizer.setOnClickListener(v -> {
            Intent intent = new Intent(context, MonsterVisualizer.class);
            startActivity(intent);
            dismiss();
        });

        Themes.setBackgroundColor(getActivity(), view);
        Themes.setButtonTheme(getActivity(), monsterVisualizer);

        builder.setView(view);
        return builder.create();
    }
}
