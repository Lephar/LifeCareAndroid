package fit.lifecare.lifecare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;
import fit.lifecare.lifecare.ObjectClasses.ProgramItems;

public class ProgramDailyActivity extends AppCompatActivity {


    // Layout Views
    private ListView day_list;
    private ImageView back_button;

    private ArrayAdapter<ProgramlarimData> mProgramItemsAdapter;
    private ArrayList<ProgramlarimData> programlarimData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_daily);

        // initialize layout views
        day_list = findViewById(R.id.day_list);
        back_button = findViewById(R.id.back_button);

        // Initialize ListView and its adapter
        List<ProgramlarimData> program_items = new ArrayList<>();
        mProgramItemsAdapter = new ProgramDailyListAdapter(this , R.layout.daily_program_list_item_layout, program_items);
        day_list.setAdapter(mProgramItemsAdapter);

        ProgramItems programItem = (ProgramItems) getIntent().getSerializableExtra("ProgramItem");
        programlarimData = (ArrayList<ProgramlarimData>) getIntent().getSerializableExtra("DailyProgramlarimData");

        Log.d("hasada", programlarimData.get(0).getSabahKahvaltisi());

        putProgramstoList();

        initializeButtonListeners();

    }

    private void putProgramstoList() {

        for(int i= 0; i<programlarimData.size();i++) {
            ProgramlarimData pd = programlarimData.get(i);
            mProgramItemsAdapter.add(pd);
        }
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
