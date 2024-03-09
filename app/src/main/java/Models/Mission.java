package Models;

public class Mission {
    private byte id;
    private byte currentDifficult;
    private byte currentCuantity;
    private byte requiredCuantity;
    private String date;
    private boolean completed;

    public Mission(byte id, byte currentDifficult, byte currentCuantity, byte requiredCuantity, String date, boolean completed) {
        this.id = id;
        this.currentDifficult = currentDifficult;
        this.currentCuantity = currentCuantity;
        this.requiredCuantity = requiredCuantity;
        this.date = date;
        this.completed = completed;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public byte getCurrentDifficult() {
        return currentDifficult;
    }

    public void setCurrentDifficult(byte currentDifficult) {
        this.currentDifficult = currentDifficult;
    }

    public byte getCurrentCuantity() {
        return currentCuantity;
    }

    public void setCurrentCuantity(byte currentCuantity) {
        this.currentCuantity = currentCuantity;
    }

    public byte getRequiredCuantity() {
        return requiredCuantity;
    }

    public void setRequiredCuantity(byte requiredCuantity) {
        this.requiredCuantity = requiredCuantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
