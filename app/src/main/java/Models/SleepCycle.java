package Models;

public class SleepCycle {
    private String dateTime;
    private String sleepCycle;
    private double mrcData;
    private double sdnn;
    private double hrv;

    public SleepCycle(String dateTime, String sleepCycle, double mrcData, double sdnn, double hrv) {
        this.dateTime = dateTime;
        this.sleepCycle = sleepCycle;
        this.mrcData = mrcData;
        this.sdnn = sdnn;
        this.hrv = hrv;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSleepCycle() {
        return sleepCycle;
    }
    public void setSleepCycle(String sleepCycle) {
        this.sleepCycle = sleepCycle;
    }

    public double getMrcData() {
        return mrcData;
    }
    public void setMrcData(double mrcData) {
        this.mrcData = mrcData;
    }

    public double getSdnn() {
        return sdnn;
    }
    public void setSdnn(double sdnn) {
        this.sdnn = sdnn;
    }

    public double getHrv() {
        return hrv;
    }
    public void setHrv(double hrv) {
        this.hrv = hrv;
    }

}
