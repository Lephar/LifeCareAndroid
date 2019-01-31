package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectGenderActivity extends AppCompatActivity {

    //Layout views
    private ImageView girl;
    private ImageView boy;
    private ImageView next_button;

    private int gender_flag = 0; // 0-nothing selected, 1-girl selected , 2-boy selected
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private boolean backPressedToExitOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gender);

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //initialize views
        girl = findViewById(R.id.girl);
        boy = findViewById(R.id.boy);
        next_button = findViewById(R.id.next_button);

        //initialize view listeners
        initializeViewListeners();

    }

    private void initializeViewListeners() {

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender_flag == 0) {
                    girl.setImageResource(R.drawable.girlclicked);
                    gender_flag = 1;
                } else if (gender_flag == 2) {
                    girl.setImageResource(R.drawable.girlclicked);
                    boy.setImageResource(R.drawable.boynonclicked);
                    gender_flag = 1;
                }
            }
        });

        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender_flag == 0) {
                    boy.setImageResource(R.drawable.boyclicked);
                    gender_flag = 2;
                } else if (gender_flag == 1) {
                    boy.setImageResource(R.drawable.boyclicked);
                    girl.setImageResource(R.drawable.girlnonclicked);
                    gender_flag = 2;
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gender_flag == 0) {
                    Toast.makeText(SelectGenderActivity.this, getString(R.string.select_gender), Toast.LENGTH_SHORT).show();
                } else {
                    if (gender_flag == 1) {
                        editor.putString("gender", "Kadın");
                    } else {
                        editor.putString("gender", "Erkek");
                    }
                    editor.commit();
                    Intent intent = new Intent(SelectGenderActivity.this, SelectBirthdayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //handle back button actions
    @Override
    public void onBackPressed() {
        //force user press back button second time for exiting app
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(this, getString(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }
}
