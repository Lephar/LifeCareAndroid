package fit.lifecare.lifecare;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;

public class ProgramDailyListAdapter extends ArrayAdapter {

    public ProgramDailyListAdapter(Context context, int resource, List<ProgramlarimData> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProgramlarimData programItem = (ProgramlarimData) getItem(position);

        Log.d("hasdasd3112 ", programItem.getSabahKahvaltisi());

        String date = programItem.getDate();
        String program_name = programItem.getName();


        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.daily_program_list_item_layout, parent, false);

        TextView day_date = convertView.findViewById(R.id.date);
//        TextView dateTextView = convertView.findViewById(R.id.date);
//        TextView dietitianNameTextView = convertView.findViewById(R.id.dietitian_name);
//
//        programNameTextView.setText(program_name);
//        dateTextView.setText(date);
//        dietitianNameTextView.setText(dietitian_name);




        final ConstraintLayout day_frame = convertView.findViewById(R.id.day_frame);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(day_frame.getVisibility() == View.VISIBLE) {
                    day_frame.setVisibility(View.GONE);
                } else {
                    day_frame.setVisibility(View.VISIBLE);
                }

            }
        });

        return convertView;
    }

}
