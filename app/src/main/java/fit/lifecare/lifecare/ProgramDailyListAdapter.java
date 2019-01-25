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


        String date = programItem.getDate();
        String program_name = programItem.getName();
        String total_cal = programItem.getToplamCalory();

        String meal1_str = programItem.getSabahKahvaltisi();
        String meal1_cal = programItem.getSabahKahvaltisiCalory();

        String meal2_str = programItem.getBirinciAraOgun();
        String meal2_cal = programItem.getBirinciAraOgunCalory();

        String meal3_str = programItem.getOgleYemegi();
        String meal3_cal = programItem.getOgleYemegiCalory();

        String meal4_str = programItem.getIkinciAraOgun();
        String meal4_cal = programItem.getIkinciAraOgunCalory();

        String meal5_str = programItem.getAksamYemegi();
        String meal5_cal = programItem.getAksamYemegiCalory();

        String meal6_str = programItem.getUcuncuAraOgun();
        String meal6_cal = programItem.getUcuncuAraOgunCalory();

        convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.daily_program_list_item_layout, parent, false);

        TextView day_title = convertView.findViewById(R.id.day_title);
        TextView day_date = convertView.findViewById(R.id.day_date);

        TextView meal1_title = convertView.findViewById(R.id.meal1_title);
        TextView meal1_content = convertView.findViewById(R.id.meal1_content);

        TextView meal2_title = convertView.findViewById(R.id.meal2_title);
        TextView meal2_content = convertView.findViewById(R.id.meal2_content);

        TextView meal3_title = convertView.findViewById(R.id.meal3_title);
        TextView meal3_content = convertView.findViewById(R.id.meal3_content);

        TextView meal4_title = convertView.findViewById(R.id.meal4_title);
        TextView meal4_content = convertView.findViewById(R.id.meal4_content);

        TextView meal5_title = convertView.findViewById(R.id.meal5_title);
        TextView meal5_content = convertView.findViewById(R.id.meal5_content);

        TextView meal6_title = convertView.findViewById(R.id.meal6_title);
        TextView meal6_content = convertView.findViewById(R.id.meal6_content);


        day_title.setText(day_title.getText().toString().replace("1",String.valueOf(position +1)));
        day_date.setText(date);

        if(meal1_cal != null) {

            String new_meal1_title = meal1_title.getText().toString().replace("0",meal1_cal);
            meal1_title.setText(new_meal1_title);
        }
        if(meal2_cal != null) {

            String new_meal2_title = meal2_title.getText().toString().replace("0",meal2_cal);
            meal2_title.setText(new_meal2_title);
        }
        if(meal3_cal != null) {

            String new_meal3_title = meal3_title.getText().toString().replace("0",meal3_cal);
            meal3_title.setText(new_meal3_title);
        }
        if(meal4_cal != null) {

            String new_meal4_title = meal4_title.getText().toString().replace("0",meal4_cal);
            meal4_title.setText(new_meal4_title);
        }
        if(meal5_cal != null) {

            String new_meal5_title = meal5_title.getText().toString().replace("0",meal5_cal);
            meal5_title.setText(new_meal5_title);
        }
        if(meal6_cal != null) {

            String new_meal6_title = meal6_title.getText().toString().replace("0",meal6_cal);
            meal6_title.setText(new_meal6_title);
        }
        if(total_cal != null) {
            String totalCal = day_title.getText() + "  ("  + total_cal + " cal)";
            day_title.setText(totalCal);
        }

        meal1_content.setText(meal1_str);
        meal2_content.setText(meal2_str);
        meal3_content.setText(meal3_str);
        meal4_content.setText(meal4_str);
        meal5_content.setText(meal5_str);
        meal6_content.setText(meal6_str);

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
