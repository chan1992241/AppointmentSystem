package my.edu.utar.appointmentsystem.MISC;

import org.json.JSONException;
import org.json.JSONObject;

public class Appointment {
    private String ID;
    private String title;
    private Schedule schedule;
    private String status;
    private Student student;
    private Lecturer lecturer;
    private String description;

    public Appointment() {
    }

    public Appointment(String ID, String title, Schedule schedule, String status, Student student, Lecturer lecturer, String description) {
        this.ID = ID;
        this.title = title;
        this.schedule = schedule;
        this.status = status;
        this.student = student;
        this.lecturer = lecturer;
        this.description = description;
    }

    public Appointment(JSONObject appointmentObject) throws JSONException {
        this.ID = appointmentObject.getString("_id");
        this.title = appointmentObject.getString("title");
        this.schedule = new Schedule(appointmentObject.getJSONObject("schedule"));
        this.status = appointmentObject.getString("status");
        this.student = new Student(appointmentObject.getJSONObject("studentID"));
        this.lecturer = new Lecturer(appointmentObject.getJSONObject("lecturerID"));
        this.description = appointmentObject.getString("description");
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
