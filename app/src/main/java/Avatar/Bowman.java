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
            R.drawable.avatar_bowman_2_aurora,
            R.drawable.avatar_bowman_3_aurora,
            R.drawable.avatar_bowman_4_aurora,
            R.drawable.avatar_bowman_5_aurora,
            R.drawable.avatar_bowman_6_aurora,
            R.drawable.avatar_bowman_7_aurora
    };
    private static final int[] bowmanPhasesBlue = {
            R.drawable.avatar_bowman_1_blue,
            R.drawable.avatar_bowman_2_blue,
            R.drawable.avatar_bowman_3_blue,
            R.drawable.avatar_bowman_4_blue,
            R.drawable.avatar_bowman_5_blue,
            R.drawable.avatar_bowman_6_blue,
            R.drawable.avatar_bowman_7_blue
    };
    private static final int[] bowmanPhasesBrown = {
            R.drawable.avatar_bowman_1_brown,
            R.drawable.avatar_bowman_2_brown,
            R.drawable.avatar_bowman_3_brown,
            R.drawable.avatar_bowman_4_brown,
            R.drawable.avatar_bowman_5_brown,
            R.drawable.avatar_bowman_6_brown,
            R.drawable.avatar_bowman_7_brown
    };
    private static final int[] bowmanPhasesGray = {
            R.drawable.avatar_bowman_1_gray,
            R.drawable.avatar_bowman_2_gray,
            R.drawable.avatar_bowman_3_gray,
            R.drawable.avatar_bowman_4_gray,
            R.drawable.avatar_bowman_5_gray,
            R.drawable.avatar_bowman_6_gray,
            R.drawable.avatar_bowman_7_gray
    };
    private static final int[] bowmanPhasesMan = {
            R.drawable.avatar_bowman_1_man,
            R.drawable.avatar_bowman_2_man,
            R.drawable.avatar_bowman_3_man,
            R.drawable.avatar_bowman_4_man,
            R.drawable.avatar_bowman_5_man,
            R.drawable.avatar_bowman_6_man,
            R.drawable.avatar_bowman_7_man
    };
    private static final int[] bowmanPhasesGreen = {
            R.drawable.avatar_bowman_1_green,
            R.drawable.avatar_bowman_2_green,
            R.drawable.avatar_bowman_3_green,
            R.drawable.avatar_bowman_4_green,
            R.drawable.avatar_bowman_5_green,
            R.drawable.avatar_bowman_6_green,
            R.drawable.avatar_bowman_7_green
    };
    private static final int[] bowmanPhasesYellow = {
            R.drawable.avatar_bowman_1_gyellow,
            R.drawable.avatar_bowman_2_gyellow,
            R.drawable.avatar_bowman_3_gyellow,
            R.drawable.avatar_bowman_4_gyellow,
            R.drawable.avatar_bowman_5_gyellow,
            R.drawable.avatar_bowman_6_gyellow,
            R.drawable.avatar_bowman_7_gyellow
    };
    private static final int[] bowmanPhasesOrange = {
            R.drawable.avatar_bowman_1_orange,
            R.drawable.avatar_bowman_2_orange,
            R.drawable.avatar_bowman_3_orange,
            R.drawable.avatar_bowman_4_orange,
            R.drawable.avatar_bowman_5_orange,
            R.drawable.avatar_bowman_6_orange,
            R.drawable.avatar_bowman_7_orange
    };
    private static final int[] bowmanPhasesPink = {
            R.drawable.avatar_bowman_1_pink,
            R.drawable.avatar_bowman_2_pink,
            R.drawable.avatar_bowman_3_pink,
            R.drawable.avatar_bowman_4_pink,
            R.drawable.avatar_bowman_5_pink,
            R.drawable.avatar_bowman_6_pink,
            R.drawable.avatar_bowman_7_pink
    };
    private static final int[] bowmanPhasesPurple = {
            R.drawable.avatar_bowman_1_purple,
            R.drawable.avatar_bowman_2_purple,
            R.drawable.avatar_bowman_3_purple,
            R.drawable.avatar_bowman_4_purple,
            R.drawable.avatar_bowman_5_purple,
            R.drawable.avatar_bowman_6_purple,
            R.drawable.avatar_bowman_7_purple
    };
}
