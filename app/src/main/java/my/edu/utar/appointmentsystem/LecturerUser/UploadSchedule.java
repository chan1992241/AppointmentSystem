package my.edu.utar.appointmentsystem.LecturerUser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Locale;

import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.Volley.VolleySingleton;

public class UploadSchedule extends AppCompatActivity {

    EditText et_date, et_time, uploadSchedule_duration;
    int hour, minute;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        Button upload_Schedule = findViewById(R.id.upload_Schedule);
        getSupportActionBar().setTitle("Upload Schedule");
        uploadSchedule_duration = findViewById(R.id.uploadSchedule_duration);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UploadSchedule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + (month < 10 ? "0" + month: month )+ "-" + (day < 10 ? "0" + day: day);
                        et_date.setText(date);

                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    et_date.callOnClick();
                }
            }
        });
        et_date.setInputType(InputType.TYPE_NULL);

        et_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    et_time.callOnClick();
                }
            }
        });
        et_time.setInputType(InputType.TYPE_NULL);


        upload_Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String lecturerID = intent.getStringExtra("lecturerID");

                String date = et_date.getText().toString().trim();
                String time = et_time.getText().toString().trim();
                String duration = uploadSchedule_duration.getText().toString().trim();

                if (!date.isEmpty() && !time.isEmpty() && !duration.isEmpty()) {
                    String url2 = "https://appointmentmobileapi.herokuapp.com/uploadSchedule/" + lecturerID;
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("date", date);
                        jsonBody.put("time", time);
                        jsonBody.put("duration", duration);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String requestBody = jsonBody.toString();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Toast.makeText(UploadSchedule.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(UploadSchedule.this, LecturerMainPage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("lecturerID", lecturerID);
                                        intent.putExtra("role", "lecturer");
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    try {
                                        byte[] htmlBodyBytes = error.networkResponse.data;
                                        JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                                        Toast.makeText(UploadSchedule.this, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
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
                    VolleySingleton.getInstance(UploadSchedule.this).addToRequestQueue(jsonObjectRequest);
                } else {
                    Toast.makeText(UploadSchedule.this, "Missing Information.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String role = getIntent().getStringExtra("role");
        Intent intent1 = getIntent();
        String userID = intent1.getStringExtra("lecturerID") == null ? intent1.getStringExtra("studentID"):intent1.getStringExtra("lecturerID");

        LecturerOptionMenu lecturerOptionMenu = new LecturerOptionMenu(UploadSchedule.this, menu, userID, role);
        lecturerOptionMenu.build();

        return true;
    }

    // menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                et_time.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

            }
        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }
}