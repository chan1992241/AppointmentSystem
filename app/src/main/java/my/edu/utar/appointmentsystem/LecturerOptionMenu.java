package my.edu.utar.appointmentsystem;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class LecturerOptionMenu {
    private Context context;
    private Menu menu;
    private String lecturerID, role;
    public LecturerOptionMenu(Context context, Menu menu, String lecturerID, String role){
       this.context = context;
       this.menu = menu;
       this.lecturerID = lecturerID;
       this.role = role;
    }
    public void build(){
        Intent intent = new Intent(context, LecturerUpcoming.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("lecturerID", lecturerID);
        intent.putExtra("role", this.role);
        menu.add("Upcoming Booking").setIntent(intent);

        Intent intent2 = new Intent(context, UploadSchedule.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.putExtra("lecturerID", lecturerID);
        intent2.putExtra("role", this.role);
        menu.add("Upload Schedule").setIntent(intent2);

        Intent intent3 = new Intent(context, LecturerMainPage.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent3.putExtra("lecturerID", lecturerID);
        intent3.putExtra("role", this.role);
        menu.add("My booking").setIntent(intent3);

        Intent intent5 = new Intent(context, ManageSchedule.class);
        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent5.putExtra("lecturerID", lecturerID);
        intent5.putExtra("role", this.role);
        menu.add("Manage Schedule").setIntent(intent5);

        Intent intent6 = new Intent(context, UserProfile.class);
        intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent6.putExtra("lecturerID", lecturerID);
        intent6.putExtra("role", this.role);
        menu.add("Profile").setIntent(intent6);

        Intent intent4 = new Intent(context, roleSelection.class);
        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        menu.add("Logout").setIntent(intent4);
    }
}
