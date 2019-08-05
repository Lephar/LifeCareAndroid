package fit.lifecare.lifecare;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class KasAnaliziActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView backText;

    private LineChart mChart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kas_analizi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        backButton = findViewById(R.id.imageButton21);
        backText = findViewById(R.id.textView40);
        mChart1 = findViewById(R.id.chart1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KasAnaliziActivity.this.onBackPressed();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KasAnaliziActivity.this.onBackPressed();
            }
        });

        setData();
    }

    public void setData() {

        ArrayList<Entry> yValues1 = new ArrayList<>();
        yValues1.add(new Entry(1, 40));
        yValues1.add(new Entry(2, 48));
        yValues1.add(new Entry(3, 40));
        yValues1.add(new Entry(4, 44));
        yValues1.add(new Entry(5, 34));
        yValues1.add(new Entry(6, 42));
        yValues1.add(new Entry(7, 40));

        LineDataSet set;

        set = new LineDataSet(yValues1, "");
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setCubicIntensity(0.1f);
        set.setDrawFilled(false);
        set.setDrawCircles(true);
        set.setCircleColor(ContextCompat.getColor(getApplicationContext(), R.color.muscleColor));
        set.setCircleRadius(4);
        set.setDrawValues(false);
        set.setColor(ContextCompat.getColor(getApplicationContext(), R.color.muscleColor));
        set.setLineWidth(4);

        LineData data = new LineData(set);

        mChart1.setTouchEnabled(true);
        mChart1.setDragDecelerationFrictionCoef(0.9f);
        mChart1.setDragEnabled(true);
        mChart1.setScaleEnabled(true);
        mChart1.setDrawGridBackground(false);
        mChart1.setHighlightPerTapEnabled(true);

        XAxis chart1_Xaxis = mChart1.getXAxis();
        chart1_Xaxis.setEnabled(false);
        /*chart1_Xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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
        });*/
        chart1_Xaxis.setGranularity(1f);
        mChart1.setViewPortOffsets(0f, 0f, 0f, 0f);

        mChart1.setDescription(null);
        mChart1.getLegend().setEnabled(false);
        mChart1.getAxisRight().setEnabled(false);
        mChart1.getAxisLeft().setEnabled(false);
        mChart1.getAxisLeft().setDrawGridLines(false);
        mChart1.animateX(1000, Easing.Linear);
        mChart1.setData(data);
    }
}
