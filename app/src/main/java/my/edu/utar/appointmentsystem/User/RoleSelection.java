package my.edu.utar.appointmentsystem.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import my.edu.utar.appointmentsystem.LecturerUser.LecturerLogin;
import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.StudentUser.StudentLogin;

public class RoleSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        setTitle("BookNow");
        TextView studentRole = findViewById(R.id.studentRole);
        TextView lecturerRole = findViewById(R.id.lecturerRole);
        studentRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleSelection.this, StudentLogin.class);
                intent.putExtra("role", "student");
                startActivity(intent);
            }
        });
        lecturerRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoleSelection.this, LecturerLogin.class);
                intent.putExtra("role", "lecturer");
                startActivity(intent);
            }
        });
    }
}