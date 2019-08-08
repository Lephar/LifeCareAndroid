package fit.lifecare.lifecare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.ArrayList;

public class MainTab extends Fragment {

    protected final String[] parties = new String[]{
            "Party A", "Party B", "Party C"
    };

    private PieChart chart;
    private CollapsibleCalendar calendar;
    private ImageButton olcum;
    private TextView olcumText;

    private ImageView yagAnalizi;
    private ImageView kasAnalizi;
    private ImageView suAnalizi;
    private ImageView bmiAnalizi;

    private OnFragmentInteractionListener mListener;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yagAnalizi = getActivity().findViewById(R.id.imageView18);
        yagAnalizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YagAnaliziActivity.class);
                startActivity(intent);
            }
        });

        kasAnalizi = getActivity().findViewById(R.id.imageView22);
        kasAnalizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), KasAnaliziActivity.class);
                startActivity(intent);
            }
        });

        suAnalizi = getActivity().findViewById(R.id.imageView23);
        suAnalizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SuAnaliziActivity.class);
                startActivity(intent);
            }
        });

        bmiAnalizi = getActivity().findViewById(R.id.imageView25);
        bmiAnalizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BMIActivity.class);
                startActivity(intent);
            }
        });

        olcum = getView().findViewById(R.id.imageButton2);
        olcumText = getView().findViewById(R.id.textView22);

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

        calendar = getView().findViewById(R.id.calendarView);
        calendar.setExpandIconVisible(true);
        calendar.addEventTag(2019, 7, 11);
        calendar.addEventTag(2019, 7, 12);
        calendar.addEventTag(2019, 7, 13);
        calendar.addEventTag(2019, 7, 14);
        calendar.addEventTag(2019, 7, 15);
        calendar.addEventTag(2019, 7, 16);
        calendar.addEventTag(2019, 7, 17);

        chart = getView().findViewById(R.id.chart1);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);

        //chart.setCenterTextTypeface(tfLight);
        //chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(0);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(60f);

        chart.setExtraBottomOffset(12);

        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        chart.animateY(1500, Easing.EaseInOutSine);
        chart.getRenderer().getPaintRender().setShadowLayer(12, 0, 8, ContextCompat.getColor(getContext(), R.color.shadowColor));
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setEnabled(false);
        /*l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
*/

        setData();
    }

    private void setData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        entries.add(new PieEntry((float) 48, parties[0]));
        entries.add(new PieEntry((float) 36, parties[1]));
        entries.add(new PieEntry((float) 16, parties[2]));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        /*for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        */
        //colors.add(ColorTemplate.getHoloBlue());

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

        chart.invalidate();
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
