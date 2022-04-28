package my.edu.utar.appointmentsystem.MISC;

import org.json.JSONException;
import org.json.JSONObject;

public class Student {
    private String ID;
    private String studentID;
    private String password;
    private String name;

    public Student() {
    }

    public Student(String ID, String studentID, String password, String name) {
        this.ID = ID;
        this.studentID = studentID;
        this.password = password;
        this.name = name;
    }

    public Student(JSONObject studentObject) throws JSONException {
        this.ID = studentObject.getString("_id");
        this.studentID = studentObject.getString("studentID");
        this.password = studentObject.getString("password");
        this.name = studentObject.getString("name");
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
