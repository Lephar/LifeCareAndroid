package fit.lifecare.lifecare.Dialogs;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fit.lifecare.lifecare.R;

public class KisiselBirthdaySelect extends DialogFragment {

    //Layout views
    private ImageView close_button;
    private ImageView tamam_button;
    private DatePicker mDatePicker;

    private String birthdate = "01.01.1990";

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimKisiselDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_birthday, container, false);

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        tamam_button = view.findViewById(R.id.tamam_button);
        mDatePicker = view.findViewById(R.id.datePicker);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimKisiselDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("PersonalInfo").child("birth_date");

        //initialize click listeners
        initializeButtonListeners();
        //initialize datePicker
        initializeDatePicker();

        return view;
    }


    //inialize date picker
    private void initializeDatePicker() {

        mDatePicker.init(1990, 0, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;

                //this code block for getting 01.01.2018 date format
                if (dayOfMonth < 10 && monthOfYear < 10) {
                    birthdate = "0" + dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else if (dayOfMonth < 10) {
                    birthdate = "0" + dayOfMonth + "." + monthOfYear + "." + year;
                } else if (monthOfYear < 10) {
                    birthdate = dayOfMonth + "." + "0" + monthOfYear + "." + year;
                } else {
                    birthdate = dayOfMonth + "." + monthOfYear + "." + year;
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
                mProfilimKisiselDatabaseReference.setValue(birthdate);
                Toast.makeText(getContext(), "Kaydedildi", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
