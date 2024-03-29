package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;
import fit.lifecare.lifecare.ObjectClasses.ProgramItems;

public class ProgramGeneralActivity extends AppCompatActivity {

    // Layout Views
    private ImageView back_button;
    private TextView program_name;
    private TextView date;
    private TextView meal1_title;
    private TextView meal1_content;
    private TextView meal2_title;
    private TextView meal2_content;
    private TextView meal3_title;
    private TextView meal3_content;
    private TextView meal4_title;
    private TextView meal4_content;
    private TextView meal5_title;
    private TextView meal5_content;
    private TextView meal6_title;
    private TextView meal6_content;
    private TextView meal7_title;
    private TextView meal7_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_general);

        // Initialize views
        back_button = findViewById(R.id.back_button);
        program_name = findViewById(R.id.program_name);
        date = findViewById(R.id.date);
        meal1_content = findViewById(R.id.meal1_content);
        meal1_title = findViewById(R.id.meal1_title);
        meal2_content = findViewById(R.id.meal2_content);
        meal2_title = findViewById(R.id.meal2_title);
        meal3_content = findViewById(R.id.meal3_content);
        meal3_title = findViewById(R.id.meal3_title);
        meal4_content = findViewById(R.id.meal4_content);
        meal4_title = findViewById(R.id.meal4_title);
        meal5_content = findViewById(R.id.meal5_content);
        meal5_title = findViewById(R.id.meal5_title);
        meal6_content = findViewById(R.id.meal6_content);
        meal6_title = findViewById(R.id.meal6_title);
        meal6_content = findViewById(R.id.meal6_content);
        meal6_title = findViewById(R.id.meal6_title);
        meal7_content = findViewById(R.id.meal7_content);
        meal7_title = findViewById(R.id.meal7_title);

        ProgramItems programItem = (ProgramItems) getIntent().getSerializableExtra("ProgramItem");
        ProgramlarimData programlarimData = (ProgramlarimData) getIntent().getSerializableExtra("ProgramlarimData");

        date.setText(programItem.getDate());
        program_name.setText(programItem.getProgramName());

        if(programlarimData.getSabahKahvaltisiCalory() != null) {

            String new_meal1_title = meal1_title.getText().toString().replace("0",programlarimData.getSabahKahvaltisiCalory());
            meal1_title.setText(new_meal1_title);
        }
        if(programlarimData.getBirinciAraOgunCalory() != null) {

            String new_meal2_title = meal2_title.getText().toString().replace("0",programlarimData.getBirinciAraOgunCalory());
            meal2_title.setText(new_meal2_title);
        }
        if(programlarimData.getOgleYemegiCalory() != null) {

            String new_meal3_title = meal3_title.getText().toString().replace("0",programlarimData.getOgleYemegiCalory());
            meal3_title.setText(new_meal3_title);
        }
        if(programlarimData.getIkinciAraOgunCalory() != null) {

            String new_meal4_title = meal4_title.getText().toString().replace("0",programlarimData.getIkinciAraOgunCalory());
            meal4_title.setText(new_meal4_title);
        }
        if(programlarimData.getAksamYemegiCalory() != null) {

            String new_meal5_title = meal5_title.getText().toString().replace("0",programlarimData.getAksamYemegiCalory());
            meal5_title.setText(new_meal5_title);
        }
        if(programlarimData.getUcuncuAraOgunCalory() != null) {

            String new_meal6_title = meal6_title.getText().toString().replace("0",programlarimData.getUcuncuAraOgunCalory());
            meal6_title.setText(new_meal6_title);
        }
        if(programlarimData.getAlternatifCalory() != null) {
        
            String new_meal7_title = meal7_title.getText().toString().replace("0",programlarimData.getAlternatifCalory());
            meal7_title.setText(new_meal7_title);
        }
        if(programlarimData.getToplamCalory() != null) {
            String totalCal = date.getText() + "\n("  +programlarimData.getToplamCalory() + " cal)";
            date.setText(totalCal);
        }
        
        String SabahKahvaltisi = "";
        String BirinciAraOgun = "";
        String OgleYemegi = "";
        String IkinciAraOgun = "";
        String AksamYemegi = "";
        String UcuncuAraOgun = "";
        String Alternatif = "";
        
        for(String str : programlarimData.getSabahKahvaltisi()){
            SabahKahvaltisi = str + "\n" + SabahKahvaltisi;
        }
    
        for(String str : programlarimData.getBirinciAraOgun()){
            BirinciAraOgun = str + "\n" + BirinciAraOgun;
        }
    
        for(String str : programlarimData.getOgleYemegi()){
            OgleYemegi = str + "\n" + OgleYemegi;
        }
    
        for(String str : programlarimData.getIkinciAraOgun()){
            IkinciAraOgun = str + "\n" + IkinciAraOgun;
        }
    
        for(String str : programlarimData.getAksamYemegi()){
            AksamYemegi = str + "\n" + AksamYemegi;
        }
    
        for(String str : programlarimData.getUcuncuAraOgun()){
            UcuncuAraOgun = str + "\n" + UcuncuAraOgun;
        }
    
        for(String str : programlarimData.getAlternatif()){
            Alternatif = str + "\n" + Alternatif;
        }

        meal1_content.setText(SabahKahvaltisi);
        meal2_content.setText(BirinciAraOgun);
        meal3_content.setText(OgleYemegi);
        meal4_content.setText(IkinciAraOgun);
        meal5_content.setText(AksamYemegi);
        meal6_content.setText(UcuncuAraOgun);
        meal7_content.setText(Alternatif);

        initializeButtonListeners();

    }

    private void initializeButtonListeners() {

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
