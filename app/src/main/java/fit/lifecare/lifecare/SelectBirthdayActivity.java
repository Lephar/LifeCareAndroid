package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

public class SelectBirthdayActivity extends AppCompatActivity {

    //Layout views
    private ImageView back_button;
    private ImageView next_button;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String birthdate = "01.01.1990";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_birthday);

        //initialize views
        back_button = findViewById(R.id.back_button);
        next_button = findViewById(R.id.next_button);

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //initialize datePicker
        initializeDatePicker();

        //initialize button listeners
        initializeViewListeners();

    }

    private void initializeDatePicker() {
        DatePicker dp = findViewById(R.id.datePicker);
        dp.setMaxDate(System.currentTimeMillis());
        dp.init(1990, 0, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;

                //this code block for getting 01.01.2018 date format
                if (dayOfMonth < 10 && monthOfYear < 10) {
                    birthdate = "0" + dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else if (dayOfMonth < 10) {
                    birthdate = "0" + dayOfMonth + "." + monthOfYear + "." + year;
                } else if (monthOfYear < 10) {
                    birthdate = dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else {
                    birthdate = dayOfMonth + "." + monthOfYear + "." + year;
                }
                //this code block for getting 01.01.2018 date format
            }
        });
    }

    private void initializeViewListeners() {

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("birthday", birthdate);
                editor.commit();
                Intent intent = new Intent(SelectBirthdayActivity.this, SelectHeightActivity.class);
                startActivity(intent);
            }
        });
    }
}
