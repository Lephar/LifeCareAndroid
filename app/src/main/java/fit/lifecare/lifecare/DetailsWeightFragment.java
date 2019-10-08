package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsWeightFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detWghChart);
        layout = getView().findViewById(R.id.detWghConstLayout);

        topDateText = getView().findViewById(R.id.detWghMeasurementDate);
        topValueText = getView().findViewById(R.id.detWghAmountText);

        backButton = getView().findViewById(R.id.detWghBackButton);
        backText = getView().findViewById(R.id.detWghBackText);

        doubleValued = false;
        pattern = "0.#";
        unit = "kg";
        color = R.color.weightColor;
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
