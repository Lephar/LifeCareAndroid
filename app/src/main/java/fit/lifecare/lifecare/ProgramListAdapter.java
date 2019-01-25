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
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        Calendar calendar = Calendar.getInstance();
        Integer yy = calendar.get(Calendar.YEAR);
        Integer mm = calendar.get(Calendar.MONTH) + 1;
        Integer dd = calendar.get(Calendar.DAY_OF_MONTH);

        String date = programItem.getDate();
        String dietitian_name = programItem.getDietitianName();
        String program_name = programItem.getProgramName();

        String[] splited_date = date.split("-");
        String starting_date = (splited_date[0] + "." + splited_date[1] + "." + splited_date[2]).trim();
        String ending_date = (splited_date[3] + "." + splited_date[4] + "." + splited_date[5]).trim();
        String current_date = yy.toString() + "." + mm.toString() + "." + dd.toString();

        Log.d("hasdfga", starting_date + " " + ending_date + " " + current_date);

        if (!(current_date.compareTo(starting_date) == -1 || ending_date.compareTo(current_date) == -1) ) {
            frame.setBackgroundResource(R.drawable.program_header);

        }

        programNameTextView.setText(program_name);
        dateTextView.setText(date);
        dietitianNameTextView.setText(dietitian_name);

        return convertView;
    }

}
