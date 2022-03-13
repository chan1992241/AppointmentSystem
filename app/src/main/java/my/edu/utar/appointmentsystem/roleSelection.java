package my.edu.utar.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class roleSelection extends AppCompatActivity {

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
                Intent intent = new Intent(roleSelection.this, StudentLogin.class);
                intent.putExtra("role", "student");
                startActivity(intent);
            }
        });
        lecturerRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(roleSelection.this, LecturerLogin.class);
                intent.putExtra("role", "lecturer");
                startActivity(intent);
            }
        });
    }
}