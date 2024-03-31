package Avatar;

import com.example.myapplication.R;

public class CharactersList {
    private static final int[] charactersList = {
            R.drawable.avatar_mage_4,
            R.drawable.avatar_thief_4,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_warrior_4,
            R.drawable.avatar_archaeologist_4,
            R.drawable.avatar_noble_4,
    };
    public static int[] getCharactersList() {
        return charactersList;
    }

    public static int[] getCharacterPhases(byte avatarClass) {
        int[] skins;
        switch (avatarClass) {
            case 1:
                skins = magePhases;
                break;
            case 2:
                skins = thiefPhases;
                break;
            case 3:
                skins = bowmanPhases;
                break;
            case 4:
                skins = warriorPhases;
                break;
            case 5:
                skins = archaeologistPhases;
                break;
            case 6:
                skins = noblePhases;
                break;
            default:
                skins = placeholder;
                break;
        }
        return skins;
    }
    private static final int[] bowmanPhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_bowman_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] magePhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_mage_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] thiefPhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_thief_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] warriorPhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_warrior_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] archaeologistPhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_archaeologist_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] noblePhases = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.avatar_noble_4,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
    private static final int[] placeholder = {
            R.drawable.avatar_zholder_1,
            R.drawable.avatar_zholder_2,
            R.drawable.avatar_zholder_3,
            R.drawable.fantasy,
            R.drawable.avatar_zholder_5,
            R.drawable.avatar_zholder_6,
            R.drawable.avatar_zholder_7
    };
}
