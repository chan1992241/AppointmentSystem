package my.edu.utar.appointmentsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StudentLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        Button loginStudent_loginBtn = findViewById(R.id.loginStudent_loginBtn);
        EditText loginStudent_studentID = findViewById(R.id.loginStudent_studentID);
        EditText loginStudent_studentPassword = findViewById(R.id.loginStudent_studentPassword);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        loginStudent_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://10.0.2.2:8080/studentLogin";
                String url2 = "https://appointmentmobileapi.herokuapp.com/studentLogin";
                JSONObject jsonBody = new JSONObject();
                progressBar.setVisibility(View.VISIBLE);
                try {
                    jsonBody.put("studentID", loginStudent_studentID.getText().toString());
                    jsonBody.put("password", loginStudent_studentPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                try {
                                    Toast.makeText(StudentLogin.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                    /*
                                     * TODO: route to main page
                                    Intent intent = new Intent(StudentLogin.this, studentMainPage.class);
                                    intent.putExtra("studentID", response.getString("studentID"));
                                    intent.putExtra("role", "lecturer");
                                    startActivity(intent); */
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
                                    Toast.makeText(StudentLogin.this, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
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
                VolleySingleton.getInstance(StudentLogin.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}