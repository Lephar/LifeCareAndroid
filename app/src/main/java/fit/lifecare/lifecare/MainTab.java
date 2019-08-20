package fit.lifecare.lifecare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MainTab extends Fragment {

    private PieChart chart;
    private ImageButton olcum;
    private TextView olcumText;

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

    private ConstraintLayout[] carts = new ConstraintLayout[6];
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private ValueEventListener mValueEventListener;
    private ValueEventListener mSingleValueEventListener;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView deleteButton;
    private ImageView calendarBackground;
    private CollapsibleCalendar collapsibleCalendar;
    private int yy;
    private int mm;
    private int dd;
    private String selected_date;

    private String height;
    private float fat, muscle, water, weight, bmi, meta;
    private TextView fatValueText, muscleValueText, waterValueText, weightValueText, bmiValueText, metaValueText;
    private TextView fatPercentText, musclePercentText, waterPercentText, fatDescribeText, muscleDescribeText, waterDescribeText;
    private TextView lastMeasureText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_tab, container, false);
    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carts[0] = getView().findViewById(R.id.cart1);
        carts[1] = getView().findViewById(R.id.cart2);
        carts[2] = getView().findViewById(R.id.cart3);
        carts[3] = getView().findViewById(R.id.cart4);
        carts[4] = getView().findViewById(R.id.cart5);
        carts[5] = getView().findViewById(R.id.cart6);

        for (int i = 0; i < carts.length; i++) {
            final int index = i;
            carts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("index", index);
                    startActivity(intent);
                }
            });
        }

        fatValueText = getView().findViewById(R.id.fatValueText);
        muscleValueText = getView().findViewById(R.id.muscleValueText);
        waterValueText = getView().findViewById(R.id.waterValueText);
        weightValueText = getView().findViewById(R.id.weightValueText);
        bmiValueText = getView().findViewById(R.id.bmiValueText);
        metaValueText = getView().findViewById(R.id.metaValueText);

        fatPercentText = getView().findViewById(R.id.fatPercentText);
        musclePercentText = getView().findViewById(R.id.musclePercentText);
        waterPercentText = getView().findViewById(R.id.waterPercentText);
        fatDescribeText = getView().findViewById(R.id.fatDescribeText);
        muscleDescribeText = getView().findViewById(R.id.muscleDescribeText);
        waterDescribeText = getView().findViewById(R.id.waterDescribeText);

        Animation textFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        textFadeIn.setStartOffset(1000);
        lastMeasureText = getView().findViewById(R.id.lastMeasureText);
        lastMeasureText.startAnimation(textFadeIn);

        Animation textLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.text_left);
        Animation textRight = AnimationUtils.loadAnimation(getActivity(), R.anim.text_right);
        textLeft.setStartOffset(800);
        textRight.setStartOffset(800);
/*
        fatPercentText.startAnimation(textRight);
        fatDescribeText.startAnimation(textRight);
        musclePercentText.startAnimation(textRight);
        muscleDescribeText.startAnimation(textRight);
        waterPercentText.startAnimation(textLeft);
        waterDescribeText.startAnimation(textLeft);
*/
        fatPercentText.startAnimation(textFadeIn);
        fatDescribeText.startAnimation(textFadeIn);
        musclePercentText.startAnimation(textFadeIn);
        muscleDescribeText.startAnimation(textFadeIn);
        waterPercentText.startAnimation(textFadeIn);
        waterDescribeText.startAnimation(textFadeIn);

        olcum = getView().findViewById(R.id.imageButton2);
        olcumText = getView().findViewById(R.id.textView22);

        olcum.startAnimation(textFadeIn);
        olcumText.startAnimation(textFadeIn);

        olcum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OlcumActivity.class);
                startActivity(intent);
            }
        });

        olcumText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                olcum.performClick();
            }
        });

        chart = getView().findViewById(R.id.mainChart);
        collapsibleCalendar = getView().findViewById(R.id.calendarView);
        calendarBackground = getView().findViewById(R.id.imageView17);

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcumlerimDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcumlerim");

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        height = preferences.getString("height", "0");

        Animation slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.calendar_down);
        calendarBackground.startAnimation(slideDown);
        collapsibleCalendar.startAnimation(slideDown);

        Animation slideLeft[] = new Animation[6];
        for (int i = 0; i < carts.length; i++) {
            slideLeft[i] = AnimationUtils.loadAnimation(getActivity(), R.anim.cart_left);
            slideLeft[i].setStartOffset(i * 200 + 200);
            carts[i].startAnimation(slideLeft[i]);
        }

        calendarBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collapsibleCalendar.isCollapsed())
                    collapsibleCalendar.expand(400);
                else
                    collapsibleCalendar.collapse(400);
            }
        });

        initializeCalendar();
        attachSelectableDatesListener();
        initializeChart();
    }

    private void initializeChart() {

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(0);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(60f);

        chart.setExtraBottomOffset(12);
        chart.setHighlightPerTapEnabled(true);
        chart.getRenderer().getPaintRender().setShadowLayer(12, 0, 8, ContextCompat.getColor(getContext(), R.color.shadowColor));

        Legend l = chart.getLegend();
        l.setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        entries.add(new PieEntry(water, "Water"));
        entries.add(new PieEntry(muscle, "Muscle"));
        entries.add(new PieEntry(fat, "Fat"));

        PieDataSet dataSet = new PieDataSet(entries, "Measurements");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(getContext(), R.color.waterColor));
        colors.add(ContextCompat.getColor(getContext(), R.color.muscleColor));
        colors.add(ContextCompat.getColor(getContext(), R.color.fatColor));
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        chart.setData(data);

        // undo all highlights
        chart.setDrawEntryLabels(false);
        chart.highlightValues(null);
        chart.setRotationAngle(270f);
        chart.setRotationEnabled(false);

        chart.animateY(1600, Easing.EaseInOutSine);
        chart.invalidate();
    }

    private void initializeCalendar() {

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

/*                if (selectable_dates_string.contains(deleting_date)) {
                    deleteButton.setImageResource(R.drawable.delete_clicked);
                    deleteButton.setClickable(true);
                } else {
                    deleteButton.setClickable(false);
                    deleteButton.setImageResource(R.drawable.delete_nonclicked);
                }
*/
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

                                Log.d("Vucut_agirligi", olcumlerimData.getVucut_agirligi());
                                Log.d("Beden_kutle_endeksi", olcumlerimData.getBeden_kutle_endeksi());
                                Log.d("Yag_orani", olcumlerimData.getYag_orani());
                                Log.d("Su_orani", olcumlerimData.getSu_orani());
                                Log.d("Kas_orani", olcumlerimData.getKas_orani());
                                Log.d("Bazal_metabolizma_hizi", olcumlerimData.getBazal_metabolizma_hizi());
                                Log.d("Metabolizma_yasi", olcumlerimData.getMetabolizma_yasi());

                                fat = Float.parseFloat(olcumlerimData.getYag_orani());
                                muscle = Float.parseFloat(olcumlerimData.getKas_orani());
                                water = Float.parseFloat(olcumlerimData.getSu_orani());
                                weight = Float.parseFloat(olcumlerimData.getVucut_agirligi());
                                bmi = Float.parseFloat(olcumlerimData.getBeden_kutle_endeksi());
                                meta = Float.parseFloat(olcumlerimData.getBazal_metabolizma_hizi());

                                initializeChart();
                                collapsibleCalendar.collapse(400);

                                fatValueText.setText((int) (fat) + " %, " + (int) (fat * weight / 100) + " kg");
                                muscleValueText.setText((int) (muscle) + " %, " + (int) (muscle * weight / 100) + " kg");
                                waterValueText.setText((int) (water) + " %, " + (int) (water * weight / 100) + " lt");
                                weightValueText.setText((int) (weight) + " kg");
                                bmiValueText.setText(new DecimalFormat("#.00").format(bmi * 10000));
                                metaValueText.setText(new DecimalFormat("#").format(meta));

                                fatPercentText.setText("%" + (int) fat);
                                musclePercentText.setText("%" + (int) muscle);
                                waterPercentText.setText("%" + (int) water);

                                lastMeasureText.setText("Son Ölçüm\n" + (dd < 10 ? "0" : "") + dd + (mm < 10 ? ".0" : ".") + mm + "." + yy);
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
                    long xvalue = TimeUnit.MILLISECONDS.toDays(date.getTimeInMillis() + 1000 * 60 * 60 * 24);

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

                int lyy = -1, lmm = -1, ldd = -1;

                for (String date_str : selectable_dates_string) {
                    Log.d("ooooo", date_str);
                    String[] parts = date_str.split("-");
                    yy = Integer.parseInt(parts[0]);
                    mm = Integer.parseInt(parts[1]);
                    dd = Integer.parseInt(parts[2]);

                    collapsibleCalendar.addEventTag(yy, mm - 1, dd);
                    lyy = yy;
                    lmm = mm;
                    ldd = dd;
                }

                if (lyy != -1 && lmm != -1 && ldd != -1) {
                    fat = -1;
                    muscle = -1;
                    water = -1;
                    collapsibleCalendar.select(new Day(lyy, lmm - 1, ldd));
                    lastMeasureText.setText("Son Ölçüm\n" + dd + (mm >= 10 ? "." : ".0") + mm + "." + yy);
                }

                /*
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
                }*/
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
