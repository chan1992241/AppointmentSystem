package my.edu.utar.appointmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class BookingAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Appointment> data;

    public BookingAdapter(@NonNull Context c, ArrayList<Appointment> data) {
        super(c, android.R.layout.simple_list_item_1, new String[data.size()]);

        this.context = c;
        this.data = data;
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
        Button lecturerActionBtn = (Button)  bookingEntry.findViewById(R.id.lecturer_action_btn);

        lecturerAppointmentTitle.setText(appointment.getTitle());
        lecturerTime.setText(formatDateTime(appointment.getSchedule().getDateTime()));
        lecturerDuration.setText("Duration: " + appointment.getSchedule().getDuration() + " minutes");
        lecturerActionBtn.setText(appointment.getStatus());
        lecturerActionBtn.setTag(R.id.appointmentID, appointment.getID());
        lecturerActionBtn.setTag(R.id.scheduleID, appointment.getSchedule().getID());
        lecturerActionBtn.setTag(R.id.appointmentStatus, appointment.getStatus());
        lecturerActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call respective dialog
                switch (view.getTag(R.id.appointmentStatus).toString()) {
                    case "Accepted":
                    case "Rejected":
                    case "End":
                        break;
                    case "Pending":
                        break;
                    default:
                        Toast.makeText(context, view.getTag(R.id.appointmentStatus).toString(), Toast.LENGTH_SHORT).show();
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
