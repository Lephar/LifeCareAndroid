package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsWaterFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_water, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detWatChart);
        layout = getView().findViewById(R.id.detWatConstLayout);

        topDateText = getView().findViewById(R.id.detWatMeasurementDate);
        topValueText = getView().findViewById(R.id.detWatAmountText);

        backButton = getView().findViewById(R.id.detWatBackButton);
        backText = getView().findViewById(R.id.detWatBackText);

        pattern = "0.#";
        unit = "lt";
        color = R.color.waterColor;
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
