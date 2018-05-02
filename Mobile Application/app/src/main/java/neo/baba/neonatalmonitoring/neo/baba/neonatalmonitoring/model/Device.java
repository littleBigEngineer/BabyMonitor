package neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model;

/**
 * Created by rober on 15/02/2018.
 */

public class Device {
    private String device_id, device_name, user_one, user_two, currently_playing;
    private int active;

    public Device() {
    }

    public void setDevice_name(String device_name) { this.device_name = device_name; }
    public String getDevice_name() { return device_name; }

    public String getDevice_id() { return device_id; }
    public void setDevice_id(String device_id) { this.device_id = device_id; }

    public String getCurrently_playing() { return currently_playing; }
    public void setCurrently_playing(String currently_playing) { this.currently_playing = currently_playing; }

    public String getUser_one() {
        return user_one;
    }
    public void setUser_one(String user_one) {
        this.user_one = user_one;
    }

    public String getUser_two() { return user_two; }
    public void setUser_two(String user_two) { this.user_two = user_two; }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
}