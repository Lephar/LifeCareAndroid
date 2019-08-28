package fit.lifecare.lifecare;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class DetailsAbstractFragment extends Fragment {

    private final float EPSILON = 0.005f;
    private final long DAY_MILIS = 1000 * 60 * 60 * 24;
    LineChart chart = null;
    ImageView indicator = null;
    ImageButton backButton;
    TextView backText, topDateText, topValueText;
    ArrayList<Entry> values;
    ConstraintLayout layout;
    boolean initialized = false, loaded = false, painted = false;
    String unit, pattern;

    public DetailsAbstractFragment() {
    }

    public void setData(ArrayList<Entry> list) {
        values = list;
        painted = false;
        loaded = true;
    }

    void adjustIndicator(float val) {
        if (val < 12)
            val = 12;
        else if (val > 42)
            val = 42;

        float ang = -90;
        if (val < 18.5)
            ang += 30 * (val - 12) / 6.5;
        else if (val >= 18.5 && val < 25)
            ang += 30 + 30 * (val - 18.5) / 6.5;
        else if (val >= 25 && val < 30)
            ang += 60 + 60 * (val - 25) / 5;
        else if (val >= 30 && val < 35)
            ang += 120 + 30 * (val - 30) / 5;
        else
            ang += 150 + 30 * (val - 35) / 7;

        AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, ang,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 1.0f);

        animRotate.setDuration(1200);
        animRotate.setFillAfter(true);
        animSet.addAnimation(animRotate);
        indicator.startAnimation(animSet);
    }

    void fillChart(int color) {
        LineDataSet set;
        set = new LineDataSet(values, "");
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setCubicIntensity(0.1f);
        set.setDrawFilled(false);
        set.setDrawCircles(true);
        Context context = getContext();
        if (context != null) {
            set.setCircleColor(ContextCompat.getColor(context, color));
            set.setColor(ContextCompat.getColor(context, color));
        }
        set.setCircleRadius(4);
        set.setDrawValues(false);
        set.setLineWidth(4);
        LineData data = new LineData(set);
        chart.setData(data);
        chart.setVisibleXRangeMaximum(30);

        chart.setDragDecelerationFrictionCoef(0.9f);
        chart.setDragEnabled(true);
        chart.setTouchEnabled(true);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerTapEnabled(true);
        chart.setDescription(null);

        chart.setViewPortOffsets(0f, 0f, 0f, 0f);
        chart.getLegend().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setGranularity(1f);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.animateX(1000, Easing.Linear);
        chart.moveViewToX(values.get(values.size() - 1).getX());

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                update(e);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    void fillLayout(String pattern, String unit) {
        this.unit = unit;
        this.pattern = pattern;

        if (values.size() == 2)
            update(values.get(0));
        else
            update(values.get(values.size() - 1));

        ConstraintSet set = new ConstraintSet();
        View lastView = layout;
        boolean first = true;

        for (int i = values.size() - 1; i >= 0; i--) {

            final int index = i;
            final Entry value = values.get(index);

            if (value.getY() < 0)
                continue;

            ImageView separator = new ImageView(getContext());
            separator.setScaleType(ImageView.ScaleType.FIT_XY);
            separator.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.seperator));
            separator.setId(View.generateViewId());

            MontserratTextView dateText = new MontserratTextView(getContext());
            dateText.setText(date(value.getX()));
            dateText.setId(View.generateViewId());

            MontserratTextView valueText = new MontserratTextView(getContext());
            if (value.getY() > EPSILON)
                valueText.setText(new DecimalFormat(pattern).format(value.getY()) + " " + unit);
            valueText.setId(View.generateViewId());

            final View placeholder = new View(getContext());
            placeholder.setId(View.generateViewId());

            placeholder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update(value);
                    if (chart != null)
                        chart.highlightValue(value.getX(), 0);
                    //if(indicator != null)
                    //    adjustIndicator(value.getY());
                }
            });

            dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeholder.performClick();
                }
            });

            valueText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeholder.performClick();
                }
            });

            layout.addView(separator);
            layout.addView(dateText);
            layout.addView(valueText);
            layout.addView(placeholder);

            set.clone(layout);
            set.constrainHeight(separator.getId(), 1);
            set.constrainWidth(separator.getId(), ConstraintSet.MATCH_CONSTRAINT);
            if (first) {
                set.connect(separator.getId(), ConstraintSet.TOP, lastView.getId(), ConstraintSet.TOP, dp(8));
                first = false;
            } else
                set.connect(separator.getId(), ConstraintSet.TOP, lastView.getId(), ConstraintSet.BOTTOM, dp(8));
            set.connect(separator.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START, dp(24));
            set.connect(separator.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END, dp(24));

            set.constrainWidth(dateText.getId(), ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(dateText.getId(), ConstraintSet.WRAP_CONTENT);
            set.connect(dateText.getId(), ConstraintSet.TOP, separator.getId(), ConstraintSet.BOTTOM, dp(8));
            set.connect(dateText.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START, dp(24));

            set.constrainWidth(valueText.getId(), ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(valueText.getId(), ConstraintSet.WRAP_CONTENT);
            set.connect(valueText.getId(), ConstraintSet.TOP, separator.getId(), ConstraintSet.BOTTOM, dp(8));
            set.connect(valueText.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END, dp(24));

            set.constrainWidth(placeholder.getId(), ConstraintSet.MATCH_CONSTRAINT);
            set.constrainHeight(placeholder.getId(), ConstraintSet.MATCH_CONSTRAINT);
            set.connect(placeholder.getId(), ConstraintSet.TOP, dateText.getId(), ConstraintSet.TOP, 0);
            set.connect(placeholder.getId(), ConstraintSet.BOTTOM, valueText.getId(), ConstraintSet.BOTTOM, 0);
            set.connect(placeholder.getId(), ConstraintSet.START, dateText.getId(), ConstraintSet.END, 0);
            set.connect(placeholder.getId(), ConstraintSet.END, valueText.getId(), ConstraintSet.START, 0);
            set.applyTo(layout);

            lastView = dateText;
        }

        View view = new View(getContext());
        view.setId(View.generateViewId());
        layout.addView(view);
        set.clone(layout);
        set.constrainHeight(view.getId(), dp(8));
        set.constrainWidth(view.getId(), ConstraintSet.MATCH_CONSTRAINT);
        set.connect(view.getId(), ConstraintSet.TOP, lastView.getId(), ConstraintSet.BOTTOM, dp(8));
        set.connect(view.getId(), ConstraintSet.START, layout.getId(), ConstraintSet.START, dp(24));
        set.connect(view.getId(), ConstraintSet.END, layout.getId(), ConstraintSet.END, dp(24));
        set.applyTo(layout);
    }

    void update(Entry e) {
        if (e.getY() < EPSILON) {
            topValueText.setText("-");
            topDateText.setText("-");
        } else {
            topValueText.setText(new DecimalFormat(pattern).format(e.getY()) + " " + unit);
            topDateText.setText(date(e.getX()));
        }
    }

    int dp(int dp) {

        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    String date(float value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) (value) * DAY_MILIS + DAY_MILIS / 2);

        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int yy = calendar.get(Calendar.YEAR);
        return (dd < 10 ? "0" : "") + dd + "." + (mm < 10 ? "0" : "") + mm + "." + yy;
    }
}
