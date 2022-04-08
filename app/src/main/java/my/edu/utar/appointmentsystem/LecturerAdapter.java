package my.edu.utar.appointmentsystem;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LecturerAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Lecturer> data;
    String Student_ID;
    JSONArray lecturerDetails;

    public LecturerAdapter(@NonNull Context c, ArrayList<Lecturer> data, String Student_ID, JSONArray response) {
        super(c, android.R.layout.simple_list_item_1, new String[data.size()]);

        this.context = c;
        this.data = data;
        this.Student_ID = Student_ID;
        this.lecturerDetails = response;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) LayoutInflater.from(context);
        View lecturerEntry = layoutInflater.inflate(R.layout.available_lecturer_item, null, false);
        if ( position % 2 == 0 ){
            lecturerEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
        }else{
            lecturerEntry.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
        }

        Lecturer lecturer= data.get(position);
        TextView lecturerName = (TextView) lecturerEntry.findViewById(R.id.available_lecturer_name);
        JSONObject lecturerDetail = null;
        try {
            lecturerDetail = this.lecturerDetails.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        lecturerName.setText(lecturer.getName());

        JSONObject finalLecturerDetail = lecturerDetail;
        lecturerEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray schedules = null;
                try {
                    schedules = finalLecturerDetail.getJSONArray("schedules");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (schedules.length() == 0) {
                    Toast.makeText(context, "No Time Slot Available", Toast.LENGTH_SHORT).show();
                } else {
                    AppointmentDialog appointmentDialog = new AppointmentDialog(context, finalLecturerDetail, lecturer.getID(), Student_ID);
                    appointmentDialog.show();
                }
            }
        });




        return lecturerEntry;
    }
}
