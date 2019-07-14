package fit.lifecare.lifecare;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
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

        ArrayList<String> meal1_str = programItem.getSabahKahvaltisi();
        String meal1_cal = programItem.getSabahKahvaltisiCalory();
    
        ArrayList<String> meal2_str = programItem.getBirinciAraOgun();
        String meal2_cal = programItem.getBirinciAraOgunCalory();
    
        ArrayList<String> meal3_str = programItem.getOgleYemegi();
        String meal3_cal = programItem.getOgleYemegiCalory();
    
        ArrayList<String> meal4_str = programItem.getIkinciAraOgun();
        String meal4_cal = programItem.getIkinciAraOgunCalory();
    
        ArrayList<String> meal5_str = programItem.getAksamYemegi();
        String meal5_cal = programItem.getAksamYemegiCalory();
    
        ArrayList<String> meal6_str = programItem.getUcuncuAraOgun();
        String meal6_cal = programItem.getUcuncuAraOgunCalory();
    
        ArrayList<String> meal7_str = programItem.getAlternatif();
        String meal7_cal = programItem.getAlternatifCalory();

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
    
        TextView meal7_title = convertView.findViewById(R.id.meal7_title);
        TextView meal7_content = convertView.findViewById(R.id.meal7_content);


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
        if(meal7_cal != null) {
        
            String new_meal7_title = meal7_title.getText().toString().replace("0",meal7_cal);
            meal7_title.setText(new_meal7_title);
        }
        if(total_cal != null) {
            String totalCal = day_title.getText() + "  ("  + total_cal + " cal)";
            day_title.setText(totalCal);
        }
    
        String SabahKahvaltisi = "";
        String BirinciAraOgun = "";
        String OgleYemegi = "";
        String IkinciAraOgun = "";
        String AksamYemegi = "";
        String UcuncuAraOgun = "";
        String Alternatif = "";
    
        for(String str : programItem.getSabahKahvaltisi()){
            SabahKahvaltisi = str + "\n" + SabahKahvaltisi;
        }
    
        for(String str : programItem.getBirinciAraOgun()){
            BirinciAraOgun = str + "\n" + BirinciAraOgun;
        }
    
        for(String str : programItem.getOgleYemegi()){
            OgleYemegi = str + "\n" + OgleYemegi;
        }
    
        for(String str : programItem.getIkinciAraOgun()){
            IkinciAraOgun = str + "\n" + IkinciAraOgun;
        }
    
        for(String str : programItem.getAksamYemegi()){
            AksamYemegi = str + "\n" + AksamYemegi;
        }
    
        for(String str : programItem.getUcuncuAraOgun()){
            UcuncuAraOgun = str + "\n" + UcuncuAraOgun;
        }
    
        for(String str : programItem.getAlternatif()){
            Alternatif = str + "\n" + Alternatif;
        }
    
        meal1_content.setText(SabahKahvaltisi);
        meal2_content.setText(BirinciAraOgun);
        meal3_content.setText(OgleYemegi);
        meal4_content.setText(IkinciAraOgun);
        meal5_content.setText(AksamYemegi);
        meal6_content.setText(UcuncuAraOgun);
        meal7_content.setText(Alternatif);

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
