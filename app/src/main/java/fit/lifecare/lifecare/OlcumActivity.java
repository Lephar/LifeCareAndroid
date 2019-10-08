package fit.lifecare.lifecare;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class OlcumActivity extends AppCompatActivity
        implements CihazOlcumFragment.OnFragmentInteractionListener, OlcumGirisFragment.OnFragmentInteractionListener {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String height;

    private ImageButton backButton;
    private TextView backText;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olcum);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        backButton = findViewById(R.id.imageButton16);
        backText = findViewById(R.id.textView20);
        viewPager = findViewById(R.id.viewPagerOlcum);
        tabLayout = findViewById(R.id.tabLayout);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcumActivity.this.onBackPressed();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OlcumActivity.this.onBackPressed();
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        height = preferences.getString("height", "0");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CihazOlcumFragment(viewPager), getString(R.string.measure));
        adapter.addFragment(new OlcumGirisFragment(height), getString(R.string.enter));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
