package my.edu.utar.appointmentsystem.StudentUser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.Volley.VolleySingleton;

public class AppointmentDialog extends Dialog implements AdapterView.OnItemSelectedListener {
    Context context;
    ArrayList<String> schedulesID = new ArrayList<String>();
    JSONObject lecturer;
    private String selectedSchedule;
    private String lecturerID;
    private String studentID;

    public AppointmentDialog(@NonNull Context context, JSONObject lecturer, String lecturerID, String studentID) {
        super(context);
        this.context = context;
        this.lecturer = lecturer;
        this.lecturerID = lecturerID;
        this.studentID = studentID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_dialog);
        Spinner spin = findViewById(R.id.scheduleSpinner);
        spin.setOnItemSelectedListener(this);
        List<String> spinnerArray = new ArrayList<String>();
        JSONArray schedules = null;
        try {
            schedules = this.lecturer.getJSONArray("schedules");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (schedules.length() == 0) {
            schedulesID.add("No Appointment Slot");
            spinnerArray.add("No Appointment Slot");
            Button send_appointment = findViewById(R.id.send_appointment);
            send_appointment.setText("No Slot Available");
            send_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            ArrayAdapter ad = new ArrayAdapter(context, R.layout.spinner_list, spinnerArray);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(ad);
        }else{
            for (int i = 0; i < schedules.length(); i++) {
                try {
                    JSONObject scheduleDetail = schedules.getJSONObject(i);
                    String scheduleID = scheduleDetail.getString("_id");
                    schedulesID.add(scheduleID);
                    String duration = scheduleDetail.getString("duration");
                    String dateTime = scheduleDetail.getString("dateTime");
                    dateTime = formatDateTime(dateTime);
                    spinnerArray.add(dateTime + " " + duration + " minutes");
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
//            ArrayAdapter ad = new ArrayAdapter(context, android.R.layout.simple_spinner_item, spinnerArray);
            ArrayAdapter ad = new ArrayAdapter(context, R.layout.spinner_list, spinnerArray);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(ad);

            // Submit detail
            Button send_appointment = findViewById(R.id.send_appointment);
            send_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText newAppointmentTitle = findViewById(R.id.newAppointmentTitle);
                    EditText newAppointmentDesc = findViewById(R.id.newAppointmentDesc);
                    String title = newAppointmentTitle.getText().toString().trim();
                    String description = newAppointmentDesc.getText().toString().trim();

                    if (!title.isEmpty() && !description.isEmpty()) {
                        String url2 = "https://appointmentmobileapi.herokuapp.com/sentAppointmentRequest";
                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("studentID", studentID);
                            jsonBody.put("lecturerID", lecturerID);
                            jsonBody.put("title", title);
                            jsonBody.put("description", description);
                            jsonBody.put("scheduleID", selectedSchedule);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String requestBody = jsonBody.toString();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(context, StudentMainPage.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("studentID", studentID);
                                            intent.putExtra("role", "student");
                                            context.startActivity(intent);
                                            dismiss();
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
                                            Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
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
                        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                    } else {
                        Toast.makeText(context, "Missing Information.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        selectedSchedule = schedulesID.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String formatDateTime(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = sdf.parse(dateTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 8);
        Date newDate = c.getTime();
        sdf.applyPattern("EEEE (dd-MM-yyyy) h:mm a");
        String newDateString = sdf.format(newDate);
        return newDateString;
    }
}
