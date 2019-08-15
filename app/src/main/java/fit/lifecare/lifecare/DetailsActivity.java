package fit.lifecare.lifecare;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class DetailsActivity extends AppCompatActivity {

    private int index;
    private ViewPager pager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        Bundle extra = getIntent().getExtras();
        index = extra.getInt("index");

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DetailsFatFragment());
        adapter.addFragment(new DetailsMuscleFragment());
        adapter.addFragment(new DetailsWaterFragment());
        adapter.addFragment(new DetailsWeightFragment());
        adapter.addFragment(new DetailsBMIFragment());
        adapter.addFragment(new DetailsMetabolismFragment());

        pager = findViewById(R.id.detViewPager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);
    }
}
