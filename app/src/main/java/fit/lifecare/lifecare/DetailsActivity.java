package fit.lifecare.lifecare;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;

public class DetailsActivity extends AppCompatActivity {

    private int index;
    private ViewPager pager;
    private ViewPagerAdapter adapter;

    ArrayList<Entry> yValues1 = new ArrayList<>();
    ArrayList<Entry> yValues2 = new ArrayList<>();
    ArrayList<Entry> yValues3 = new ArrayList<>();
    ArrayList<Entry> yValues4 = new ArrayList<>();
    ArrayList<Entry> yValues5 = new ArrayList<>();
    ArrayList<Entry> yValues6 = new ArrayList<>();
    ArrayList<Entry> yValues7 = new ArrayList<>();
    ArrayList<Entry> yValues8 = new ArrayList<>();
    List<Calendar> selectable_dates = new ArrayList<>();
    List<String> selectable_dates_string = new ArrayList<>();
    private DetailsFatFragment fatFragment;
    private DetailsMuscleFragment muscleFragment;
    private DetailsWaterFragment waterFragment;
    private DetailsWeightFragment weightFragment;
    private DetailsMetabolismFragment metabolismFragment;
    private DetailsEmpedansFragment empedansFragment;
    private DetailsBMIFragment bmiFragment;
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private ValueEventListener mValueEventListener;
    private ValueEventListener mSingleValueEventListener;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String height;
    private Boolean once_loaded = false;

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

        fatFragment = new DetailsFatFragment();
        muscleFragment = new DetailsMuscleFragment();
        waterFragment = new DetailsWaterFragment();
        weightFragment = new DetailsWeightFragment();
        metabolismFragment = new DetailsMetabolismFragment();
        empedansFragment = new DetailsEmpedansFragment();
        bmiFragment = new DetailsBMIFragment();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fatFragment);
        adapter.addFragment(muscleFragment);
        adapter.addFragment(waterFragment);
        adapter.addFragment(weightFragment);
        adapter.addFragment(metabolismFragment);
        adapter.addFragment(empedansFragment);
        adapter.addFragment(bmiFragment);

        pager = findViewById(R.id.detViewPager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);
        pager.setOffscreenPageLimit(7);

        //initialize shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcumlerimDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcumlerim");

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        height = preferences.getString("height", "0");

        attachSelectableDatesListener();
    }

    private void attachSelectableDatesListener() {

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing lists to prevent duplicate data
                selectable_dates.clear();
                selectable_dates_string.clear();
                yValues1.clear();
                yValues2.clear();
                yValues3.clear();
                yValues4.clear();
                yValues5.clear();
                yValues6.clear();
                yValues7.clear();
                yValues8.clear();

                for (DataSnapshot olcumlerimDataSnapshot : dataSnapshot.getChildren()) {

                    Calendar date = transformStringtoDate(olcumlerimDataSnapshot.getKey());
                    long xvalue = TimeUnit.MILLISECONDS.toDays(date.getTimeInMillis() + 1000 * 60 * 60 * 24);

                    OlcumlerimData olcumlerimData = olcumlerimDataSnapshot.getValue(OlcumlerimData.class);
                    if (olcumlerimData.getVucut_agirligi().length() > 0) {
                        yValues1.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getVucut_agirligi())));
                    }
                    if (olcumlerimData.getBeden_kutle_endeksi().length() > 0) {
                        yValues2.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getBeden_kutle_endeksi())));
                    }
                    if (olcumlerimData.getYag_orani().length() > 0) {
                        yValues3.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getYag_orani())));
                    }
                    if (olcumlerimData.getSu_orani().length() > 0) {
                        yValues4.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getSu_orani())));
                    }
                    if (olcumlerimData.getKas_orani().length() > 0) {
                        yValues5.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getKas_orani())));
                    }
                    if (olcumlerimData.getEmpedans() != null && olcumlerimData.getEmpedans().length() > 0) {
                        yValues6.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getEmpedans())));
                    }
                    if (olcumlerimData.getBazal_metabolizma_hizi().length() > 0) {
                        yValues7.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getBazal_metabolizma_hizi())));
                    }
                    if (olcumlerimData.getMetabolizma_yasi().length() > 0) {
                        yValues8.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getMetabolizma_yasi())));
                    }
                }

                for (int i = yValues1.size(); i < 2; i++)
                    yValues1.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues2.size(); i < 2; i++)
                    yValues2.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues3.size(); i < 2; i++)
                    yValues3.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues4.size(); i < 2; i++)
                    yValues4.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues5.size(); i < 2; i++)
                    yValues5.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues6.size(); i < 2; i++)
                    yValues6.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues7.size(); i < 2; i++)
                    yValues7.add(new Entry(i * 1000 * 60 * 60 * 24, -1));
                for (int i = yValues8.size(); i < 2; i++)
                    yValues8.add(new Entry(i * 1000 * 60 * 60 * 24, -1));

                fatFragment.setData(yValues3);
                fatFragment.draw();
                muscleFragment.setData(yValues5);
                muscleFragment.draw();
                waterFragment.setData(yValues4);
                waterFragment.draw();
                weightFragment.setData(yValues1);
                weightFragment.draw();
                metabolismFragment.setData(yValues7);
                metabolismFragment.draw();
                empedansFragment.setData(yValues6);
                empedansFragment.draw();
                bmiFragment.setData(yValues2);
                bmiFragment.draw();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mOlcumlerimDatabaseReference.addValueEventListener(mValueEventListener);
    }

    private Calendar transformStringtoDate(String str) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }
}
