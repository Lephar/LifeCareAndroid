package fit.lifecare.lifecare;

import android.app.Activity;
import android.content.Context;
import android.icu.util.TimeUnit;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.Timestamp;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import fit.lifecare.lifecare.ObjectClasses.ProgramItems;

public class ProgramListAdapter extends ArrayAdapter {

    public ProgramListAdapter(Context context, int resource, List<ProgramItems> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.program_list_item_layout, parent, false);
        TextView programNameTextView = convertView.findViewById(R.id.program_name);
        TextView dateTextView = convertView.findViewById(R.id.date);
        TextView dietitianNameTextView = convertView.findViewById(R.id.dietitian_name);
        ImageView frame = convertView.findViewById(R.id.frame);

        ProgramItems programItem = (ProgramItems) getItem(position);
    
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar calendar = Calendar.getInstance();
        Date cur_date = calendar.getTime();

        String date = programItem.getDate();
        String dietitian_name = programItem.getDietitianName();
        String program_name = programItem.getProgramName();

        String[] splited_date = date.split("/");
        String starting_date = splited_date[0].trim();
        String ending_date = splited_date[1].trim();
        String current_date = dateFormat.format(cur_date);
    
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = format.parse(starting_date);
            starting_date = new SimpleDateFormat("yyyy.MM.dd").format(date1);
        
            Date date2 = format.parse(ending_date);
            ending_date = new SimpleDateFormat("yyyy.MM.dd").format(date2);
        
        } catch (ParseException e) {
            //handle exception
        }

        if (current_date.compareTo(starting_date) <= 0 ) {
            frame.setBackgroundResource(R.drawable.program_header);

        }

        programNameTextView.setText(program_name);
        dateTextView.setText(date);
        dietitianNameTextView.setText(dietitian_name);

        return convertView;
    }
    
}
