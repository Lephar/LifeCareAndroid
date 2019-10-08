package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsMuscleFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_muscle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detMusChart);
        layout = getView().findViewById(R.id.detMusConstLayout);

        topDateText = getView().findViewById(R.id.detMusMeasurementDate);
        topValueText = getView().findViewById(R.id.detMusAmountText);

        backButton = getView().findViewById(R.id.detMusBackButton);
        backText = getView().findViewById(R.id.detMusBackText);

        doubleValued = true;
        pattern = "0.#";
        unit = "kg";
        color = R.color.muscleColor;
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
