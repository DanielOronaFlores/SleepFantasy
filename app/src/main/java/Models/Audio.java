package Models;

import java.io.Serializable;

public class Audio implements Serializable {
    private int id;
    private String name;
    private int createdBySystem;

    public Audio(int id, String name, int createdBySystem) {
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

    public int getcreatedBySystem() {
        return createdBySystem;
    }
    public void setcreatedBySystem(int createdBySystem) {
        this.createdBySystem = createdBySystem;
    }
}
