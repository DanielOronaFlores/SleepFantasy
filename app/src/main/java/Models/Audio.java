package Models;

import java.io.Serializable;

public class Audio implements Serializable {
    private int id;
    private String name;
    private int isCreatedBySystem;

    public Audio(int id, String name, int createdBySystem) {
        this.id = id;
        this.name = name;
        this.isCreatedBySystem = createdBySystem;
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

    public int getIsCreatedBySystem() {
        return isCreatedBySystem;
    }
    public void setIsCreatedBySystem(int createdBySystem) {
        this.isCreatedBySystem = createdBySystem;
    }
}
