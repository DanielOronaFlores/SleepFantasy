package Models;

public class Playlist {
    private int id;
    private String name;
    private int createdBySystem;

    public Playlist(int id, String name, int createdBySystem) {
        this.id = id;
        this.name = name;
        this.createdBySystem = createdBySystem;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCreatedBySystem() {
        return createdBySystem;
    }
    public void setCreatedBySystem(int createdBySystem) {
        this.createdBySystem = createdBySystem;
    }
}
