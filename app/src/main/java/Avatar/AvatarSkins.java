package Avatar;

import com.example.myapplication.R;

public class AvatarSkins {  //TODO: Revisar toda la implementación de esta clase.
    public int[] getCharacters() {
        return characters;
    }

    public void setCharacters(int[] characters) {
        this.characters = characters;
    }

    private int[] characters = { //TODO: Cambiar por los personajes reales.
            R.drawable.placeholder_cono,
            R.drawable.placeholder_jaime,
            R.drawable.placeholder_bruja,
            R.drawable.placeholder_jirafa,
            R.drawable.placeholder_quick
    };
    public int[] getAvatarSkins(byte avatarClass) { //TODO: Agregar los skins de los personajes reales.
        int[] skins;
        switch (avatarClass) {
            case 0:
                skins = placeHolderSkins;
                break;
            default:
                skins = null;
                break;
        }
        return skins;
    }
    private final int[] placeHolderSkins = {
            R.drawable.avatar_default_1,
            R.drawable.avatar_default_2,
            R.drawable.avatar_default_3,
            R.drawable.fantasy,
            R.drawable.avatar_default_5,
            R.drawable.avatar_default_6,
            R.drawable.avatar_default_7
    };
}
