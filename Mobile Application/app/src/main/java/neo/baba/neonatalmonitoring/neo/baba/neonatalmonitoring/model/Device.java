package neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model;

/**
 * Created by rober on 15/02/2018.
 */

public class Device {
    private String device_name, child, user_one;
    private int active;

    public Device() {
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getUser_one() {
        return user_one;
    }

    public void setUser_one(String user_one) {
        this.user_one = user_one;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}