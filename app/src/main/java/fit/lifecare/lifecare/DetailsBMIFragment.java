package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DetailsBMIFragment extends DetailsAbstractFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details_bmi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        indicator = getView().findViewById(R.id.detBmiValue);
        layout = getView().findViewById(R.id.detBmiConstLayout);

        topDateText = getView().findViewById(R.id.detBmiMeasurementDate);
        topValueText = getView().findViewById(R.id.detBmiAmountText);

        backButton = getView().findViewById(R.id.detBmiBackButton);
        backText = getView().findViewById(R.id.detBmiBackText);

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

        adjustIndicator(values.get(values.size() - 1).getY());
        fillLayout("0.00", "kg/m²");
        painted = true;
    }
}
