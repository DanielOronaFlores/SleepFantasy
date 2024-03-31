package Avatar;

public class NameClasses {
    public String getNameClass(byte avatarClass) {
        String name;
        switch (avatarClass) {
            case 1:
                name = "Mago";
                break;
            case 2:
                name = "Ladrón";
                break;
            case 3:
                name = "Arquero";
                break;
            case 4:
                name = "Guerrero";
                break;
            case 5:
                name = "Arqueólogo";
                break;
            case 6:
                name = "Noble";
                break;
            default:
                name = "placeholder";
                break;
        }
        return name;
    }
}
