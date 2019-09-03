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

        initialized = true;
        draw();
    }

    void draw() {

        if (!initialized || !loaded || painted)
            return;

        fillChart(R.color.metaColor);
        fillLayout("0", "kcal/gün", R.color.metaColor);
        painted = true;
    }
}
