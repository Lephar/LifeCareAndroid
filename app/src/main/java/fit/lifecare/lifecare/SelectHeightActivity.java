package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class SelectHeightActivity extends AppCompatActivity {

    //Layout views
    private ImageView next_button;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_height);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //initialize views
        next_button = findViewById(R.id.next_button);

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //initialize numberPicker
        initializeNumberPicker();

        //initialize button listeners
        initializeViewListeners();
    }


    private void initializeNumberPicker() {

        np = findViewById(R.id.numberPicker);
        np.setMinValue(100);
        np.setMaxValue(250);
        np.setValue(170);
    }

    private void initializeViewListeners() {

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("height", Integer.toString(np.getValue()));
                editor.commit();
                Intent intent = new Intent(SelectHeightActivity.this, SelectPhotoActivity.class);
                startActivity(intent);
            }
        });
    }
}
