package fit.lifecare.lifecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;
import fit.lifecare.lifecare.Dialogs.OlcumlerimAddDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OlcumlerimMainFragment extends Fragment {

    //Layout Views
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private ImageView deleteButton;
    private CollapsibleCalendar collapsibleCalendar;

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
    private int yy;
    private int mm;
    private int dd;
    private String selected_date;
    private String height;
    private OlcumlerimTab1 olcumlerimTab1;
    private OlcumlerimTab2 olcumlerimTab2;
    private OlcumlerimTab3 olcumlerimTab3;
    private OlcumlerimTab4 olcumlerimTab4;
    private Boolean once_loaded = false;
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private ValueEventListener mValueEventListener;
    private ValueEventListener mSingleValueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_olcumlerim, container, false);

        //initialize Buttons
        fab = view.findViewById(R.id.fab);
        deleteButton = view.findViewById(R.id.delete);
        initializeButtonClickListener();

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
        Log.d("heightbulmaca",height);

        //attach firebase database listener to get selectable dates
        attachSelectableDatesListener();

        //initialize viewPager
        viewPager = view.findViewById(R.id.viewPager);

        //initialize calendar
        collapsibleCalendar = view.findViewById(R.id.calendarView);
        initCalendarListener();

        //initialize tablayout
        tabLayout = view.findViewById(R.id.tabs);

        return view;
    }

    private void initCalendarListener() {

        //get the current date as default date
        Calendar calendar = Calendar.getInstance();
        yy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH) + 1;
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        //this code block for getting 01-01-2018 date format
        if (dd < 10 && mm < 10) {
            selected_date = ("0" + dd + "-" + "0" + mm + "-" + yy);
        } else if (dd < 10) {
            selected_date = ("0" + dd + "-" + mm + "-" + yy);
        } else if (mm < 10) {
            selected_date = (dd + "-" + "0" + mm + "-" + yy);
        } else {
            selected_date = (dd + "-" + mm + "-" + yy);
        }
        //this code block for getting 01-01-2018 date format

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();

                yy = day.getYear();
                mm = day.getMonth() + 1;
                dd = day.getDay();

                //this code block for getting 01-01-2018 date format
                if (dd < 10 && mm < 10) {
                    selected_date = ("0" + dd + "-" + "0" + mm + "-" + yy);
                } else if (dd < 10) {
                    selected_date = ("0" + dd + "-" + mm + "-" + yy);
                } else if (mm < 10) {
                    selected_date = (dd + "-" + "0" + mm + "-" + yy);
                } else {
                    selected_date = (dd + "-" + mm + "-" + yy);
                }
                //this code block for getting 01-01-2018 date format

                //change date format from 01-01-2018  to ISO 8601 date format
                final String deleting_date = selected_date.substring(6) + "-" + selected_date.substring(3, 5) + "-" + selected_date.substring(0, 2);

                if (selectable_dates_string.contains(deleting_date)) {
                    deleteButton.setImageResource(R.drawable.delete_clicked);
                    deleteButton.setClickable(true);
                } else {
                    deleteButton.setClickable(false);
                    deleteButton.setImageResource(R.drawable.delete_nonclicked);
                }

                //listening firebase database for updating ui to show selected date's data
                mSingleValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot olcumlerimDataSnapshot : dataSnapshot.getChildren()) {


                            String date = olcumlerimDataSnapshot.getKey();
                            //change date format from ISO 8601 to 01-01-2018 format
                            date = date.substring(8) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4);

                            if (selected_date.equals(date)) {
                                OlcumlerimData olcumlerimData = olcumlerimDataSnapshot.getValue(OlcumlerimData.class);

                                olcumlerimTab1.setSelectedValues(olcumlerimData.getVucut_agirligi(), olcumlerimData.getBeden_kutle_endeksi());
                                olcumlerimTab2.setSelectedValues(olcumlerimData.getYag_orani(), olcumlerimData.getSu_orani());
                                olcumlerimTab3.setSelectedValues(olcumlerimData.getKas_orani(), olcumlerimData.getKas_orani());
                                olcumlerimTab4.setSelectedValues(olcumlerimData.getBazal_metabolizma_hizi(), olcumlerimData.getMetabolizma_yasi());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mOlcumlerimDatabaseReference.addListenerForSingleValueEvent(mSingleValueEventListener);
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });

    }

    //listening firebase database for getting previous dates that entered data
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
                    long xvalue = TimeUnit.MILLISECONDS.toDays(date.getTimeInMillis() + 1000*60*60*24 );


                    selectable_dates.add(date);
                    String str = olcumlerimDataSnapshot.getKey();
                    selectable_dates_string.add(str);

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
                    if (olcumlerimData.getKas_orani().length() > 0) {
                        yValues6.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getKas_orani())));
                    }
                    if (olcumlerimData.getBazal_metabolizma_hizi().length() > 0) {
                        yValues7.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getBazal_metabolizma_hizi())));
                    }
                    if (olcumlerimData.getMetabolizma_yasi().length() > 0) {
                        yValues8.add(new Entry(xvalue, Float.parseFloat(olcumlerimData.getMetabolizma_yasi())));
                    }
                }

                for (String date_str : selectable_dates_string) {
                    Log.d("ooooo", date_str);
                    String[] parts = date_str.split("-");
                    yy = Integer.parseInt(parts[0]);
                    mm = Integer.parseInt(parts[1]);
                    dd = Integer.parseInt(parts[2]);

                    collapsibleCalendar.addEventTag(yy, mm - 1, dd);
                }

                if (once_loaded) {
                    olcumlerimTab1.setyValues(yValues1, yValues2);
                    olcumlerimTab1.setData1();
                    olcumlerimTab1.setData2();
                    olcumlerimTab2.setyValues(yValues3, yValues4);
                    olcumlerimTab2.setData1();
                    olcumlerimTab2.setData2();
                    olcumlerimTab3.setyValues(yValues5, yValues6);
                    olcumlerimTab3.setData1();
                    olcumlerimTab4.setyValues(yValues7, yValues8);
                    olcumlerimTab4.setData1();
                    olcumlerimTab4.setData2();
                } else {
                    LoadingCompleted();
                    once_loaded = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mOlcumlerimDatabaseReference.addValueEventListener(mValueEventListener);
    }

    private void LoadingCompleted() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.weight_non_clicked);
        tabLayout.getTabAt(1).setIcon(R.drawable.fat_non_clicked);
        tabLayout.getTabAt(2).setIcon(R.drawable.muscle_non_clicked);
        tabLayout.getTabAt(3).setIcon(R.drawable.burn_non_clicked);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#f5a623"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#f5a623"), PorterDuff.Mode.SRC_IN);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        olcumlerimTab1 = new OlcumlerimTab1();
        olcumlerimTab1.setyValues(yValues1, yValues2);
        adapter.addFragment(olcumlerimTab1, "");

        olcumlerimTab2 = new OlcumlerimTab2();
        olcumlerimTab2.setyValues(yValues3, yValues4);
        adapter.addFragment(olcumlerimTab2, "");

        olcumlerimTab3 = new OlcumlerimTab3();
        olcumlerimTab3.setyValues(yValues5, yValues6);
        adapter.addFragment(olcumlerimTab3, "");

        olcumlerimTab4 = new OlcumlerimTab4();
        olcumlerimTab4.setyValues(yValues7, yValues8);
        adapter.addFragment(olcumlerimTab4, "");

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    private void initializeButtonClickListener() {

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //change date format from 01-01-2018  to ISO 8601 date format
                final String deleting_date = selected_date.substring(6) + "-" + selected_date.substring(3, 5) + "-" + selected_date.substring(0, 2);


                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setMessage(getString(R.string.sure_delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                deleteButton.setClickable(false);
                                deleteButton.setImageResource(R.drawable.delete_nonclicked);
                                mOlcumlerimDatabaseReference.child(deleting_date).removeValue();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245,166,35)));

                OlcumlerimAddDialog olcumlerimAddDialog = new OlcumlerimAddDialog();
                Bundle args = new Bundle();
                args.putString("date",selected_date);
                args.putString("height",height);
                olcumlerimAddDialog.setArguments(args);
                olcumlerimAddDialog.show(getChildFragmentManager(), "AddingDialog");
            }
        });
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
