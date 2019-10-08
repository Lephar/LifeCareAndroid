package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsFatFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_fat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detFatChart);
        layout = getView().findViewById(R.id.detFatConstLayout);

        topDateText = getView().findViewById(R.id.detFatMeasurementDate);
        topValueText = getView().findViewById(R.id.detFatAmountText);

        backButton = getView().findViewById(R.id.detFatBackButton);
        backText = getView().findViewById(R.id.detFatBackText);

        doubleValued = true;
        pattern = "0.#";
        unit = "kg";
        color = R.color.fatColor;
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
