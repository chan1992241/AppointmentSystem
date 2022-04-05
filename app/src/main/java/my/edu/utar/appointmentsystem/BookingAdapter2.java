package my.edu.utar.appointmentsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookingAdapter2 extends ArrayAdapter<String> {
    Context context;
    ArrayList<Appointment> data;
    String Student_ID;

    public BookingAdapter2(@NonNull Context c, ArrayList<Appointment> data, String Student_ID) {
        super(c, android.R.layout.simple_list_item_1, new String[data.size()]);

        this.context = c;
        this.data = data;
        this.Student_ID = Student_ID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View bookingEntry = layoutInflater.inflate(R.layout.booking_item_student, null, false);
        if ( position % 2 == 0 ){
            bookingEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
        }else{
            bookingEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        }

        Appointment appointment = data.get(position);
        TextView studentAppointmentTitle = (TextView) bookingEntry.findViewById(R.id.student_appointment_title);
        TextView studentTime = (TextView) bookingEntry.findViewById(R.id.student_time);
        TextView studentDuration = (TextView) bookingEntry.findViewById(R.id.student_duration);
        TextView lecturerName = (TextView) bookingEntry.findViewById(R.id.lecturer_name);
        Button studentActionBtn = (Button)  bookingEntry.findViewById(R.id.student_action_btn);

        studentAppointmentTitle.setText(appointment.getTitle());
        try {
            studentTime.setText(formatDateTime(appointment.getSchedule().getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        studentDuration.setText("Duration: " + appointment.getSchedule().getDuration() + " minutes");
        lecturerName.setText("Lecturer: " + appointment.getLecturer().getName());
        studentActionBtn.setText(appointment.getStatus());
        studentActionBtn.setTag(R.id.appointmentID, appointment.getID());
        studentActionBtn.setTag(R.id.appointmentStatus, appointment.getStatus());
        studentActionBtn.setTag(R.id.appointmentDescription, appointment.getDescription());
        studentActionBtn.setTag(R.id.appointmentTitle, appointment.getTitle());
        studentActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Android Small - 15
                switch (view.getTag(R.id.appointmentStatus).toString()) {
                    case "accepted":
                        AlertDialog.Builder builder = new AlertDialog.Builder((context));
                        builder.setTitle(view.getTag(R.id.appointmentTitle).toString());
                        builder.setMessage(view.getTag(R.id.appointmentDescription).toString());
                        builder.setCancelable(false);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog dialogBox = builder.create();
                        dialogBox.show();
                        break;
                    case "pending":
                    case "rejected":
                    case "end":
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle(view.getTag(R.id.appointmentTitle).toString());
                        builder2.setMessage(view.getTag(R.id.appointmentDescription).toString());
                        builder2.setCancelable(false);

                        builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url2 = "https://appointmentmobileapi.herokuapp.com/deleteAppointment/" + view.getTag(R.id.appointmentID).toString();
                                JSONObject jsonBody = new JSONObject();
                                final String requestBody = jsonBody.toString();
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                        (Request.Method.POST, url2, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try{
                                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(context, StudentMainPage.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("studentID", Student_ID);
                                                    context.startActivity(intent);
                                                }catch( JSONException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                try {
                                                    byte[] htmlBodyBytes = error.networkResponse.data;
                                                    JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                                                    Toast.makeText(context, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
                                                    System.out.println(errorRes.getString("status"));
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
                            }
                        });

                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog dialogBox2 = builder2.create();
                        dialogBox2.show();
                        break;
                }
            }
        });

        return bookingEntry;
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
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy) h:mm a", Locale.getDefault());
//        LocalDateTime datetime = LocalDateTime.parse(dateTime, inputFormatter);
//        String formattedDatetime = outputFormatter.format(datetime);
//        return formattedDatetime;
    }
}