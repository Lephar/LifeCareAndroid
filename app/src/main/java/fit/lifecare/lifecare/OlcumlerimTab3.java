package fit.lifecare.lifecare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import fit.lifecare.lifecare.Dialogs.OlcumlerimAddDialog;

public class OlcumlerimTab3 extends Fragment {

    //Layout views
    private TextView title1;
//    private TextView title2;
    private TextView value1;
//    private TextView value2;
    private LineChart mChart1;
//    private LineChart mChart2;

    ArrayList<Entry> yValues1 = new ArrayList<>();
//    ArrayList<Entry> yValues2 = new ArrayList<>();

    public void setyValues(ArrayList<Entry> myValues1, ArrayList<Entry> myValues2) {
        this.yValues1 = myValues1;
//        this.yValues2 = myValues2;
    }

    public void setSelectedValues(String selected_value1, String selected_value2) {
        this.value1.setText(selected_value1 + " %");
//        this.value2.setText(selected_value2 + " kg");
    }

    private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_olcumlerim2, container, false);

        //initialize layout views
        title1 = view.findViewById(R.id.textViewLeft1);
        value1 = view.findViewById(R.id.textViewRight1);
        mChart1 = view.findViewById(R.id.chart1);
//        title2 = view.findViewById(R.id.textViewLeft2);
//        value2 = view.findViewById(R.id.textViewRight2);
//        mChart2 = view.findViewById(R.id.chart2);


        title1.setText(getString(R.string.muscle_ratio));
//        title2.setText("Kas (kg)");


        if (!yValues1.isEmpty()) {
            setData1();
        }
//        if (!yValues2.isEmpty()) {
//            setData2();
//        }

        OnChartValueSelectedListener onChartValueSelectedListener1 = new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                value1.setText(String.valueOf(e.getY()) + " %");
            }

            @Override
            public void onNothingSelected() {

            }
        };
        mChart1.setOnChartValueSelectedListener(onChartValueSelectedListener1);

//
//        OnChartValueSelectedListener onChartValueSelectedListener2 = new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry e, Highlight h) {
//                value2.setText(String.valueOf(e.getY()) + " kg");
//            }
//
//            @Override
//            public void onNothingSelected() {
//
//            }
//        };
//        mChart2.setOnChartValueSelectedListener(onChartValueSelectedListener2);

        return view;
    }

    public void setData1() {

        LineDataSet set;
        set = new LineDataSet(yValues1, "");
        set.setColor(Color.rgb(42, 48, 127));
        set.setLineWidth(2);
        set.setCircleRadius(4);
        set.setCircleColor(Color.rgb(42, 48, 127));

        LineData data = new LineData(set);

        mChart1.setTouchEnabled(true);
        mChart1.setDragDecelerationFrictionCoef(0.9f);
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setDrawGridBackground(false);
        mChart1.setHighlightPerTapEnabled(true);

        XAxis chart1_Xaxis = mChart1.getXAxis();
        chart1_Xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart1_Xaxis.setTextSize(10f);
        chart1_Xaxis.setTextColor(Color.BLACK);
        chart1_Xaxis.setDrawAxisLine(false);
        chart1_Xaxis.setDrawGridLines(false);
        chart1_Xaxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });
        chart1_Xaxis.setGranularity(1f);

        mChart1.setDescription(null);
        mChart1.getLegend().setEnabled(false);
        mChart1.getAxisRight().setEnabled(false);
        mChart1.getAxisLeft().setDrawGridLines(false);
        mChart1.animateX(1000);
        mChart1.setData(data);
    }

//    public void setData2() {
//
//        LineDataSet set;
//
//        set = new LineDataSet(yValues2, "");
//        set.setColor(Color.rgb(42, 48, 127));
//        set.setLineWidth(2);
//        set.setCircleRadius(4);
//        set.setCircleColor(Color.rgb(42, 48, 127));
//
//        LineData data = new LineData(set);
//
//        mChart2.setTouchEnabled(true);
//        mChart2.setDragDecelerationFrictionCoef(0.9f);
//        mChart2.setDragEnabled(true);
//        mChart2.setScaleEnabled(true);
//        mChart2.setDrawGridBackground(false);
//        mChart2.setHighlightPerTapEnabled(true);
//
//        XAxis chart2_Xaxis = mChart2.getXAxis();
//        chart2_Xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        chart2_Xaxis.setTextSize(10f);
//        chart2_Xaxis.setTextColor(Color.BLACK);
//        chart2_Xaxis.setDrawAxisLine(false);
//        chart2_Xaxis.setDrawGridLines(false);
//
//        chart2_Xaxis.setValueFormatter(new IndexAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                long millis = TimeUnit.DAYS.toMillis((long) value);
//                return mFormat.format(new Date(millis));
//            }
//        });
//        chart2_Xaxis.setGranularity(1f);
//
//        mChart2.setDescription(null);
//        mChart2.getLegend().setEnabled(false);
//        mChart2.getAxisRight().setEnabled(false);
//        mChart2.getAxisLeft().setDrawGridLines(false);
//        mChart2.animateX(1000);
//        mChart2.setData(data);
//    }
}
