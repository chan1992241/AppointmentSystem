package my.edu.utar.appointmentsystem.LecturerUser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import my.edu.utar.appointmentsystem.MISC.Appointment;
import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.Utils.DateTimeFormater;
import my.edu.utar.appointmentsystem.Volley.VolleySingleton;

public class LectureBookingAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Appointment> data;
    String Lecture_ID;

    public LectureBookingAdapter(@NonNull Context c, ArrayList<Appointment> data, String Lecture_ID) {
        super(c, android.R.layout.simple_list_item_1, new String[data.size()]);

        this.context = c;
        this.data = data;
        this.Lecture_ID = Lecture_ID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View bookingEntry = layoutInflater.inflate(R.layout.booking_item_lecturer, null, false);
        if ( position % 2 == 0 ){
            bookingEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
        }else{
            bookingEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        }

        Appointment appointment = data.get(position);
        TextView lecturerAppointmentTitle = (TextView) bookingEntry.findViewById(R.id.lecturer_appointment_title);
        TextView lecturerTime = (TextView) bookingEntry.findViewById(R.id.lecturer_time);
        TextView lecturerDuration = (TextView) bookingEntry.findViewById(R.id.lecturer_duration);
        TextView studentName = (TextView) bookingEntry.findViewById(R.id.student_name);
        Button lecturerActionBtn = (Button)  bookingEntry.findViewById(R.id.lecturer_action_btn);
        ImageButton lecturerAddEvent = (ImageButton) bookingEntry.findViewById(R.id.lecturer_add_event);
        if (appointment.getStatus().equals("accepted")) {
            lecturerAddEvent.setVisibility(View.VISIBLE);
        }
        lecturerAppointmentTitle.setText(appointment.getTitle());
        try {
            lecturerTime.setText(DateTimeFormater.formatDateTime(appointment.getSchedule().getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        lecturerDuration.setText("Duration: " + appointment.getSchedule().getDuration() + " minutes");
        studentName.setText("Student: " + appointment.getStudent().getName());
        lecturerActionBtn.setText(appointment.getStatus());
        lecturerActionBtn.setTag(R.id.appointmentID, appointment.getID());
        lecturerActionBtn.setTag(R.id.scheduleID, appointment.getSchedule().getID());
        lecturerActionBtn.setTag(R.id.appointmentStatus, appointment.getStatus());
        lecturerActionBtn.setTag(R.id.appointmentDescription, appointment.getDescription());
        lecturerActionBtn.setTag(R.id.appointmentTitle, appointment.getTitle());
        lecturerActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getTag(R.id.appointmentStatus).toString()) {
                    case "accepted":
                        AlertDialog.Builder builder3= new AlertDialog.Builder(context);
                        builder3.setTitle(view.getTag(R.id.appointmentTitle).toString());
                        builder3.setMessage(view.getTag(R.id.appointmentDescription).toString());
                        builder3.setCancelable(false);
                        builder3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog dialogBox3 = builder3.create();
                        dialogBox3.show();
                        break;
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
                                                    Intent intent = new Intent(context, LecturerMainPage.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("lecturerID", Lecture_ID);
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
                    case "pending":
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle(view.getTag(R.id.appointmentTitle).toString());
                        builder1.setMessage(view.getTag(R.id.appointmentDescription).toString());
                        // no cancel button is provided. if set to false, there is no way to close the dialog.
//                        builder1.setCancelable(false);

                        builder1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url2 = "https://appointmentmobileapi.herokuapp.com/changeAppointmentStatus/" + view.getTag(R.id.appointmentID).toString();
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("status", "accepted");
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
                                                    Intent intent = new Intent(context, LecturerMainPage.class);
                                                    intent.putExtra("lecturerID", Lecture_ID);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("role", "lecturer");
                                                    context.startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //progressBar.setVisibility(View.GONE);
                                                try {
                                                    byte[] htmlBodyBytes = error.networkResponse.data;
                                                    JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                                                    Toast.makeText(context, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
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
                            }
                        });

                        builder1.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url2 = "https://appointmentmobileapi.herokuapp.com/changeAppointmentStatus/" + view.getTag(R.id.appointmentID).toString();
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("status", "rejected");
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
                                                    Intent intent = new Intent(context, LecturerMainPage.class);
                                                    intent.putExtra("lecturerID", Lecture_ID);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("role", "lecturer");
                                                    context.startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //progressBar.setVisibility(View.GONE);
                                                try {
                                                    byte[] htmlBodyBytes = error.networkResponse.data;
                                                    JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
                                                    Toast.makeText(context, errorRes.getString("message"), Toast.LENGTH_SHORT).show();
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
                            }
                        });
                        AlertDialog dialogBox1 = builder1.create();
                        dialogBox1.show();
                        break;
                }
            }
        });
        lecturerAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, appointment.getTitle());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, appointment.getDescription() + "\n\nStudent: " + appointment.getStudent().getName());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, false);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, DateTimeFormater.getTimeInMillis(appointment.getSchedule().getDateTime()));
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, DateTimeFormater.getTimeInMillis(appointment.getSchedule().getDateTime()) + Integer.parseInt(appointment.getSchedule().getDuration()) * 60 *  1000);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "There is no calendar app to add event.", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return bookingEntry;
    }
}
