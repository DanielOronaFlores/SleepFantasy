package Avatar;

import com.example.myapplication.R;

import AppContext.MyApplication;
import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class Bowman {
    public static int[] getBowmanPhasesPurple() {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(
                DatabaseConnection.getInstance(
                        MyApplication.getAppContext()
                )
        );

        int bowmanSkin = preferencesDataAccess.getAvatarSkin();
        switch (bowmanSkin) {
            case 1:
                return bowmanPhasesAurora;
            case 2:
                return bowmanPhasesBlue;
            case 3:
                return bowmanPhasesBrown;
            case 4:
                return bowmanPhasesGray;
            case 5:
                return bowmanPhasesMan;
            case 6:
                return bowmanPhasesGreen;
            case 7:
                return bowmanPhasesYellow;
            case 8:
                return bowmanPhasesOrange;
            case 9:
                return bowmanPhasesPink;
            case 10:
                return bowmanPhasesPurple;
            default:
                return bowmanPhasesDefault;
        }
    }

    private static final int[] bowmanPhasesDefault = {
            R.drawable.avatar_bowman_1,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesAurora = {
            R.drawable.avatar_bowman_1_aurora,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesBlue = {
            R.drawable.avatar_bowman_1_blue,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesBrown = {
            R.drawable.avatar_bowman_1_brown,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesGray = {
            R.drawable.avatar_bowman_1_gray,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesMan = {
            R.drawable.avatar_bowman_1_man,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesGreen = {
            R.drawable.avatar_bowman_1_green,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesYellow = {
            R.drawable.avatar_bowman_1_gyellow,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesOrange = {
            R.drawable.avatar_bowman_1_orange,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesPink = {
            R.drawable.avatar_bowman_1_pink,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
    private static final int[] bowmanPhasesPurple = {
            R.drawable.avatar_bowman_1_purple,
            R.drawable.avatar_bowman_2,
            R.drawable.avatar_bowman_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_bowman_5,
            R.drawable.avatar_bowman_6,
            R.drawable.avatar_bowman_7
    };
}
