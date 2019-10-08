package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsEmpedansFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_empedans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = getView().findViewById(R.id.detEmpChart);
        layout = getView().findViewById(R.id.detEmpConstLayout);

        topDateText = getView().findViewById(R.id.detEmpMeasurementDate);
        topValueText = getView().findViewById(R.id.detEmpAmountText);

        backButton = getView().findViewById(R.id.detEmpBackButton);
        backText = getView().findViewById(R.id.detEmpBackText);

        doubleValued = false;
        pattern = "0";
        unit = "Ω";
        color = R.color.empColor;
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
