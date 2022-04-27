package my.edu.utar.appointmentsystem;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class StudentOptionMenu {
    private Context context;
    private Menu menu;
    private String studentID, role;
    public StudentOptionMenu(Context context, Menu menu, String studentID, String role){
        this.context = context;
        this.menu = menu;
        this.studentID = studentID;
        this.role = role;
    }
    public void build(){
        Intent intent = new Intent(context, StudentUpcoming.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("studentID", studentID);
        intent.putExtra("role", "student");
        menu.add("Upcoming Booking").setIntent(intent);

        Intent intent2 = new Intent(context, MakeAppointment.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.putExtra("studentID", studentID);
        intent2.putExtra("role", "student");
        menu.add("Make Appointment").setIntent(intent2);

        Intent intent3 = new Intent(context, StudentMainPage.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent3.putExtra("studentID", studentID);
        intent3.putExtra("role", "student");
        menu.add("My booking").setIntent(intent3);

        Intent intent5 = new Intent(context, UserProfile.class);
        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent5.putExtra("studentID", studentID);
        intent5.putExtra("role", "student");
        menu.add("User Profile").setIntent(intent5);

        Intent intent4 = new Intent(context, roleSelection.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent4.putExtra("role", "student");
        menu.add("Logout").setIntent(intent4);
    }
}
