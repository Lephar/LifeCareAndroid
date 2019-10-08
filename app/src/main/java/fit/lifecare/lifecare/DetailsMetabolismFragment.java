package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsMetabolismFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_metabolism, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detMetChart);
        layout = getView().findViewById(R.id.detMetConstLayout);

        topDateText = getView().findViewById(R.id.detMetMeasurementDate);
        topValueText = getView().findViewById(R.id.detMetAmountText);

        backButton = getView().findViewById(R.id.detMetBackButton);
        backText = getView().findViewById(R.id.detMetBackText);

        doubleValued = false;
        pattern = "0";
        unit = getString(R.string.meta_unit);
        color = R.color.metaColor;
        initialized = true;
        draw();
    }

    void draw() {

        if (!initialized || !loaded || painted)
            return;

        fillChart();
        fillLayout();
        painted = true;
    }
}
