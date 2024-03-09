package Models;

public class Song {
    private int id;
    private String name;
    private int ibBySystem;

    public Song(int id, String name, int ibBySystem) {
        this.id = id;
        this.name = name;
        this.ibBySystem = ibBySystem;
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

    public int getIbBySystem() {
        return ibBySystem;
    }
    public void setIbBySystem(int ibBySystem) {
        this.ibBySystem = ibBySystem;
    }
}
