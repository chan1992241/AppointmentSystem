package my.edu.utar.appointmentsystem.LecturerUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import my.edu.utar.appointmentsystem.Utils.DateTimeFormater;
import my.edu.utar.appointmentsystem.R;
import my.edu.utar.appointmentsystem.Volley.VolleySingleton;

public class ManageSchedule extends AppCompatActivity {
    private CardView cardView;
    private LinearLayoutCompat container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);
        getSupportActionBar().setTitle("Manage Schedule");
        container = findViewById(R.id.schedules_container);
        getData();
    }
    // inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        String role = getIntent().getStringExtra("role");
        Intent intent1 = getIntent();
        String userID = intent1.getStringExtra("lecturerID") == null ? intent1.getStringExtra("studentID"):intent1.getStringExtra("lecturerID");

        LecturerOptionMenu lecturerOptionMenu = new LecturerOptionMenu(ManageSchedule.this, menu, userID, role);
        lecturerOptionMenu.build();

        return true;
    }

    // menu item selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void getData() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String lecturerID = intent.getStringExtra("lecturerID");
        String url2 = "https://appointmentmobileapi.herokuapp.com/listSpecificLecturerDeleteAbleSchedule/" + lecturerID;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONArray schedules = response;
                            if (schedules.length() == 0) {
                                TextView tv = findViewById(R.id.message);
                                tv.setVisibility(View.VISIBLE);

                            } else {
                                for (int i = 0; i < schedules.length(); i++){
                                    JSONObject schedule = schedules.getJSONObject(i);
                                    String _id = schedule.getString("_id");
                                    System.out.println(_id + " loop");
                                    String duration = schedule.getString("duration");
                                    String dateTime = DateTimeFormater.formatDateTime(schedule.getString("dateTime"));

                                    cardView = new CardView(ManageSchedule.this);
                                    LinearLayout.LayoutParams layoutparamsCard = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    cardView.setLayoutParams(layoutparamsCard);
                                    cardView.setRadius(15);
                                    cardView.setPadding(25, 25, 25, 25);
                                    cardView.setCardBackgroundColor(Color.WHITE);
                                    cardView.setMaxCardElevation(12);
                                    cardView.setPreventCornerOverlap(true);
                                    cardView.setUseCompatPadding(true);

                                    LinearLayout ll2 = new LinearLayout(ManageSchedule.this);
                                    ll2.setOrientation(LinearLayout.HORIZONTAL);

                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                    lp.weight = 1;
                                    TextView textView = new TextView(ManageSchedule.this);
                                    textView.setText(dateTime + " " + duration + " mins");
                                    textView.setTextSize(15);
                                    textView.setGravity(Gravity.CENTER);
                                    textView.setBackgroundColor(ContextCompat.getColor(ManageSchedule.this, i % 2 == 0? R.color.light_blue:  R.color.blue));
                                    textView.setLayoutParams(lp);
                                    ll2.addView(textView);

                                    Button btn = new Button(ManageSchedule.this);
                                    btn.setText("Delete");
                                    btn.setBackgroundColor(ContextCompat.getColor(ManageSchedule.this, R.color.red));
                                    btn.setTextColor(Color.WHITE);
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String url2 = "https://appointmentmobileapi.herokuapp.com/deleteSpecificSchedule/" + _id;
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                    (Request.Method.DELETE, url2, null, new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            try{
                                                                Toast.makeText(ManageSchedule.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                                            }catch (JSONException e){
                                                                System.out.println(e.toString());
                                                            }
                                                            Intent intent1 = new Intent(ManageSchedule.this, ManageSchedule.class);
                                                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent1.putExtra("lecturerID", lecturerID);
                                                            startActivity(intent1);
                                                        }
                                                    }, new Response.ErrorListener() {

                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            try {
                                                                byte[] htmlBodyBytes = error.networkResponse.data;
                                                                JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
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
                                            VolleySingleton.getInstance(ManageSchedule.this).addToRequestQueue(jsonObjectRequest);
                                        }
                                    });
                                    ll2.addView(btn);
                                    cardView.addView(ll2);
                                    container.addView(cardView);
                                }
                            }
                        }
                        catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Log.e("JSONException", "e: " + e.getMessage(), e.getCause());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            byte[] htmlBodyBytes = error.networkResponse.data;
                            JSONObject errorRes =new JSONObject(new String(htmlBodyBytes));
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
        VolleySingleton.getInstance(ManageSchedule.this).addToRequestQueue(jsonObjectRequest);
    }
}