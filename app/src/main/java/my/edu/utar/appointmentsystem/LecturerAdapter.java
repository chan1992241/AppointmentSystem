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

import java.util.ArrayList;

public class LecturerAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Lecturer> data;
    String Student_ID;

    public LecturerAdapter(@NonNull Context c, ArrayList<Lecturer> data, String Student_ID) {
        super(c, android.R.layout.simple_list_item_1, new String[data.size()]);

        this.context = c;
        this.data = data;
        this.Student_ID = Student_ID;
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

        lecturerName.setText(lecturer.getName());;
//        lecturerEntry.setTag();
        lecturerEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Android Small - 4
            }
        });

        return lecturerEntry;
    }
}
