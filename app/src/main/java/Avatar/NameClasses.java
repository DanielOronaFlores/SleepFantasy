package Avatar;

public class NameClasses { //TODO: Revisar la implementación de esta clase.
    private String name;

    public String getNameClass(byte avatarClass) {
        switch (avatarClass) { //TODO: Agregar los nombres de las clases reales.
            case 0:
                name = "Placeholder";
                break;
            default:
                name = null;
                break;
        }
        return name;
    }
}
