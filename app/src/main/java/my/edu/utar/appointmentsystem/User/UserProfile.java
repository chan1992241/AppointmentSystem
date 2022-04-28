package my.edu.utar.appointmentsystem.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import my.edu.utar.appointmentsystem.LecturerUser.LecturerMainPage;
import my.edu.utar.appointmentsystem.LecturerUser.LecturerOptionMenu;
import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.StudentUser.StudentMainPage;
import my.edu.utar.appointmentsystem.StudentUser.StudentOptionMenu;
import my.edu.utar.appointmentsystem.Volley.VolleySingleton;

public class UserProfile extends AppCompatActivity {

    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String role = getIntent().getStringExtra("role");
        Intent intent1 = getIntent();
        String userID = intent1.getStringExtra("lecturerID") == null ? intent1.getStringExtra("studentID"):intent1.getStringExtra("lecturerID");

        if (role.equals("lecturer")){
            LecturerOptionMenu lecturerOptionMenu = new LecturerOptionMenu(UserProfile.this, menu, userID, role);
            lecturerOptionMenu.build();
        }else{
            StudentOptionMenu studentOptionMenu = new StudentOptionMenu(UserProfile.this, menu, userID, role);
            studentOptionMenu.build();
        }
        return true;
    }

    // menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Upload Schedule");

        ProgressBar progressBar = findViewById(R.id.progressBar);
        String role = getIntent().getStringExtra("role");
        String lecturerID = null, studentID = null;
        if (role.equals("lecturer")){
            lecturerID =  getIntent().getStringExtra("lecturerID");
        }if (role.equals("student")){
            studentID = getIntent().getStringExtra("studentID");
        }
        String url2 = "https://appointmentmobileapi.herokuapp.com/getUserProfileDetail/" + role + "/" + (role.equals("lecturer")? lecturerID: studentID);
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            EditText password = findViewById(R.id.password);
                            EditText userID = findViewById(R.id.id);
                            userID.setText(response.getString(role.equals("lecturer")?"lecturerID": "studentID"));
                            password.setText(response.getString("password"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            byte[] htmlBodyBytes = error.networkResponse.data;
                            JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                            Toast.makeText(UserProfile.this, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
                            //System.out.println(errorRes.getString("status"));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        VolleySingleton.getInstance(UserProfile.this).addToRequestQueue(jsonObjectRequest);

        Button updatePassword = findViewById(R.id.updatePassword);
        String finalLecturerID = lecturerID, finalStudentID = studentID;
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url2 = "https://appointmentmobileapi.herokuapp.com/updatePassword/" + role + "/" + (role.equals("lecturer")? finalLecturerID : finalStudentID);
                JSONObject jsonBody = new JSONObject();
                try {
                    EditText password = findViewById(R.id.password);
                    jsonBody.put("password", password.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                progressBar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.PUT, url2, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    Toast.makeText(UserProfile.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                    if (role.equals("lecturer")){
                                        Intent intent = new Intent(UserProfile.this, LecturerMainPage.class);
                                        intent.putExtra("lecturerID", finalLecturerID);
                                        intent.putExtra("role", "lecturer");
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(UserProfile.this, StudentMainPage.class);
                                        intent.putExtra("studentID", finalStudentID);
                                        intent.putExtra("role", "student");
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    byte[] htmlBodyBytes = error.networkResponse.data;
                                    JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                                    Toast.makeText(UserProfile.this, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
                                    //System.out.println(errorRes.getString("status"));
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }
                };
                VolleySingleton.getInstance(UserProfile.this).addToRequestQueue(jsonObjectRequest);
            }
        });

    }
}