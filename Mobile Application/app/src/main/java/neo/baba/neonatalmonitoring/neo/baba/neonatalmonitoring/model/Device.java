package neo.baba.neonatalmonitoring.neo.baba.neonatalmonitoring.model;

/**
 * Created by rober on 15/02/2018.
 */

public class Device {
    private String device_name, currently_playing, user_one, user_two, device_id, carbon_monoxide, carbon_dioxide, stream;
    private int active;
    private double temperature, humidity, sound;

    public Device() {
    }

    public String getDevice_name() {
        return device_name;
    }
    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getCurrently_playing() {
        return currently_playing;
    }
    public void setCurrently_playing(String currently_playing) {
        this.currently_playing = currently_playing;
    }

    public String getUser_one() {
        return user_one;
    }
    public void setUser_one(String user_one) {
        this.user_one = user_one;
    }

    public String getUser_two() {
        return user_two;
    }
    public void setUser_two(String user_two) {
        this.user_two = user_two;
    }

    public String getDevice_id() {
        return device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCarbon_monoxide() {
        return carbon_monoxide;
    }
    public void setCarbon_monoxide(String carbon_monoxide) {
        this.carbon_monoxide = carbon_monoxide;
    }

    public String getCarbon_dioxide() {
        return carbon_dioxide;
    }
    public void setCarbon_dioxide(String carbon_dioxide) {
        this.carbon_dioxide = carbon_dioxide;
    }

    public String getStream() {
        return stream;
    }
    public void setStream(String stream) {
        this.stream = stream;
    }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }

    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getSound() {
        return sound;
    }
    public void setSound(double sound) {
        this.sound = sound;
    }


}
