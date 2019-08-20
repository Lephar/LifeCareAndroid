package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DetailsWeightFragment extends Fragment {

    private ImageButton backButton;
    private TextView backText;

    private LineChart chart;

    public DetailsWeightFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detWghChart);

        backButton = getView().findViewById(R.id.detWghBackButton);
        backText = getView().findViewById(R.id.detWghBackText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButton.performClick();
            }
        });

        setData();
    }

    public void setData() {
        ArrayList<Entry> yValues1 = new ArrayList<>();
        yValues1.add(new Entry(1, 36));
        yValues1.add(new Entry(2, 33));
        yValues1.add(new Entry(3, 43));
        yValues1.add(new Entry(4, 38));
        yValues1.add(new Entry(5, 44));
        yValues1.add(new Entry(6, 35));
        yValues1.add(new Entry(7, 39));

        LineDataSet set;
        set = new LineDataSet(yValues1, "");
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setCubicIntensity(0.1f);
        set.setDrawFilled(false);
        set.setDrawCircles(true);
        set.setCircleColor(ContextCompat.getColor(getContext(), R.color.weightColor));
        set.setCircleRadius(4);
        set.setDrawValues(false);
        set.setColor(ContextCompat.getColor(getContext(), R.color.weightColor));
        set.setLineWidth(4);

        LineData data = new LineData(set);

        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerTapEnabled(true);
        chart.setScaleEnabled(false);

        XAxis chart1_Xaxis = chart.getXAxis();
        chart1_Xaxis.setEnabled(false);
        chart1_Xaxis.setGranularity(1f);

        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.setDescription(null);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.animateX(1000, Easing.Linear);
        chart.setData(data);
    }
}
