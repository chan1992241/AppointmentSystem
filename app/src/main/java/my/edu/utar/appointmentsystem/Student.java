package my.edu.utar.appointmentsystem;

import org.json.JSONException;
import org.json.JSONObject;

public class Student {
    private String ID;
    private String studentID;
    private String password;

    public Student() {
    }

    public Student(String ID, String studentID, String password) {
        this.ID = ID;
        this.studentID = studentID;
        this.password = password;
    }

    public Student(JSONObject studentObject) throws JSONException {
        this.ID = studentObject.getString("_id");
        this.studentID = studentObject.getString("studentID");
        this.password = studentObject.getString("password");
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
}
