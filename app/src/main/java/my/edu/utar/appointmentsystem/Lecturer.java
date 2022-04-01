package my.edu.utar.appointmentsystem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Lecturer {
    private String ID;
    private String lecturerID;
    private String password;
    private String[] schedules;
    private String name;

    public Lecturer() {
    }

    public Lecturer(String ID, String lecturerID, String password, String[] schedules, String name) {
        this.ID = ID;
        this.lecturerID = lecturerID;
        this.password = password;
        this.schedules = schedules;
        this.name = name;
    }

    public Lecturer(JSONObject lecturerObject) throws JSONException {
        this.ID = lecturerObject.getString("_id");
        this.lecturerID = lecturerObject.getString("lecturerID");
        this.password = lecturerObject.getString("password");
        this.schedules = getStringArray(lecturerObject.getJSONArray("schedules"));
        this.name = lecturerObject.getString("name");
    }

    private String[] getStringArray(JSONArray schedules) throws JSONException {
        ArrayList<String> scheduleList = new ArrayList<String>();
        for(int i=0; i< schedules.length(); i++){
            scheduleList.add(schedules.getString(i));
        }
        return scheduleList.toArray(new String[scheduleList.size()]);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getSchedules() {
        return schedules;
    }

    public void setSchedules(String[] schedules) {
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
