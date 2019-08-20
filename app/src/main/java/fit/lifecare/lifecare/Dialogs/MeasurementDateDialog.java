package fit.lifecare.lifecare.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import fit.lifecare.lifecare.OlcumGirisFragment;
import fit.lifecare.lifecare.R;

public class MeasurementDateDialog extends DialogFragment {

    int year, month, day;
    //Layout views
    private ImageView close_button;
    private ImageView tamam_button;
    private DatePicker mDatePicker;
    private String selectedDate = null;
    private OlcumGirisFragment caller;

    public MeasurementDateDialog(OlcumGirisFragment frag) {
        caller = frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_measurement_date, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        tamam_button = view.findViewById(R.id.tamam_button);
        mDatePicker = view.findViewById(R.id.datePicker);

        //initialize click listeners
        initializeButtonListeners();
        //initialize datePicker
        initializeDatePicker();

        return view;
    }


    //inialize date picker
    private void initializeDatePicker() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                MeasurementDateDialog.this.year = year;
                MeasurementDateDialog.this.month = monthOfYear;
                MeasurementDateDialog.this.day = dayOfMonth;

                monthOfYear += 1;

                //this code block for getting 01.01.2018 date format
                if (dayOfMonth < 10 && monthOfYear < 10) {
                    selectedDate = "0" + dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else if (dayOfMonth < 10) {
                    selectedDate = "0" + dayOfMonth + "." + monthOfYear + "." + year;
                } else if (monthOfYear < 10) {
                    selectedDate = dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else {
                    selectedDate = dayOfMonth + "." + monthOfYear + "." + year;
                }
                //this code block for getting 01.01.2018 date format
            }
        });
    }

    private void initializeButtonListeners() {
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caller.setDate(year, month, day);
                if (selectedDate != null)
                    caller.setDateText(selectedDate);
                dismiss();
            }
        });
    }
}
