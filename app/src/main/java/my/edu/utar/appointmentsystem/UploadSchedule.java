package my.edu.utar.appointmentsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class UploadSchedule extends AppCompatActivity {

    EditText et_date, et_time, uploadSchedule_duration;
    int hour, minute;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        Button upload_Schedule = findViewById(R.id.upload_Schedule);

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


        upload_Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String lecturerID = intent.getStringExtra("lecturerID");

                String url2 = "https://appointmentmobileapi.herokuapp.com/uploadSchedule/" + lecturerID;
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("date", et_date.getText().toString().trim());
                    jsonBody.put("time", et_time.getText().toString().trim());
                    jsonBody.put("duration", uploadSchedule_duration.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                System.out.println(requestBody);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(UploadSchedule.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(UploadSchedule.this, LecturerMainPage.class);
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
            }
        });


    }


    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.main,menu);
        Intent intent1 = getIntent();
        String lecturerID = intent1.getStringExtra("lecturerID");
        System.out.println(lecturerID);

        Intent intent = new Intent(UploadSchedule.this, LecturerUpcoming.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("lecturerID", lecturerID);
        menu.add("Upcoming Booking").setIntent(intent);

        Intent intent2 = new Intent(UploadSchedule.this, UploadSchedule.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.putExtra("lecturerID", lecturerID);
        menu.add("Upload Schedule").setIntent(intent2);

        Intent intent3 = new Intent(UploadSchedule.this, LecturerMainPage.class);
        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent3.putExtra("lecturerID", lecturerID);
        menu.add("My booking").setIntent(intent3);


        return true;

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