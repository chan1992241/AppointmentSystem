package my.edu.utar.appointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LecturerLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_login);
        Button loginLecturer_loginBtn = findViewById(R.id.loginLecturer_loginBtn);
        EditText loginLecturer_lecturerID = findViewById(R.id.loginLecturer_lecturerID);
        EditText loginLecturer_lecturerPassword = findViewById(R.id.loginLecturer_lecturerPassword);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        loginLecturer_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://10.0.2.2:8080/lecturerLogin";
                String url2 = "https://appointmentmobileapi.herokuapp.com/lecturerLogin";
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("lecturerID", loginLecturer_lecturerID.getText().toString());
                    jsonBody.put("password", loginLecturer_lecturerPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                progressBar.setVisibility(View.VISIBLE);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    Toast.makeText(LecturerLogin.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                     /* TODO: route to main page */
                                    Intent intent = new Intent(LecturerLogin.this, LecturerMainPage.class);
                                    intent.putExtra("lecturerID", response.getString("lecturerID"));
                                    intent.putExtra("role", "lecturer");
                                    startActivity(intent);
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
                                    Toast.makeText(LecturerLogin.this, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
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
                VolleySingleton.getInstance(LecturerLogin.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}