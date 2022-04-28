package my.edu.utar.appointmentsystem.MISC;

import org.json.JSONException;
import org.json.JSONObject;

public class Schedule {
    private String ID;
    private String datetime;
    private String duration;

    public Schedule() {
    }

    public Schedule(String ID, String datetime, String duration) {
        this.ID = ID;
        this.datetime = datetime;
        this.duration = duration;
    }

    public Schedule(JSONObject scheduleObject) throws JSONException {
        this.ID = scheduleObject.getString("_id");
        this.datetime = scheduleObject.getString("dateTime");
        this.duration = scheduleObject.getString("duration");
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDateTime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
