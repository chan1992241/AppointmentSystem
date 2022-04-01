package my.edu.utar.appointmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        studentTime.setText(formatDateTime(appointment.getSchedule().getDateTime()));
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
                        break;
                    case "rejected":
                    case "end":
                    case "pending":
                        break;
                }
            }
        });

        return bookingEntry;
    }

    private String formatDateTime(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE (dd-MM-yyyy) h:mm a", Locale.getDefault());
        LocalDateTime datetime = LocalDateTime.parse(dateTime, inputFormatter);
        String formattedDatetime = outputFormatter.format(datetime);
        return formattedDatetime;
    }
}