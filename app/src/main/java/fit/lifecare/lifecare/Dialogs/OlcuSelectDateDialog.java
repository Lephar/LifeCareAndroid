package fit.lifecare.lifecare.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimOlcuData;
import fit.lifecare.lifecare.R;

public class OlcuSelectDateDialog extends DialogFragment {

    //Layout views
    private ImageView closeButton;
    private ImageView tamamButton;
    private ImageView deleteButton;
    private ImageView datePicker;
    private NumberPicker numberPicker;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;


    private String selected_date;
    private ArrayList<String> arraylist_dates = new ArrayList<>();
    private String[] mydates = {};


    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcuDatabaseReference;
    private ValueEventListener mSingleValueEventListener;


    public OlcuSelectDateDialog() {
    }

    public void setDates(ArrayList<String> dates) {
        this.arraylist_dates = dates;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_date, container, false);

        //initialize buttons
        closeButton = view.findViewById(R.id.close_button);
        tamamButton = view.findViewById(R.id.tamam_button);
        deleteButton = view.findViewById(R.id.delete_button);
        numberPicker = view.findViewById(R.id.numberPicker);

        TextView title = view.findViewById(R.id.dialog_title);
        title.setText(getString(R.string.past_measurement));

        //get parent view
        Activity parentview = getActivity();
        datePicker = parentview.findViewById(R.id.date);

        //initialize textviews
        editText1 = parentview.findViewById(R.id.editText1);
        editText2 = parentview.findViewById(R.id.editText2);
        editText3 = parentview.findViewById(R.id.editText3);
        editText4 = parentview.findViewById(R.id.editText4);
        editText5 = parentview.findViewById(R.id.editText5);
        editText6 = parentview.findViewById(R.id.editText6);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcuDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("Olcu");

        //initialize numberPicker
        initializeNumberPicker();
        //initialize button listeners
        initializeButtonListeners();


        return view;
    }

    private void initializeButtonListeners() {
        //close button clicked
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setImageResource(R.drawable.date_button_purple);
                dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setMessage(getString(R.string.sure_delete))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //change date format from 01-01-2018  to ISO 8601 date format
                                String deleting_date = selected_date.substring(6) + "-" + selected_date.substring(3, 5) + "-" + selected_date.substring(0, 2);

                                mOlcuDatabaseReference.child(deleting_date).removeValue();
                                arraylist_dates.remove(selected_date);
                                initializeNumberPicker();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }
        });

        //tamam button clicked
        tamamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //listening firebase database for updating ui according to selected date
                mSingleValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String key_date;

                            for (DataSnapshot olculerimSnapshot : dataSnapshot.getChildren()) {

                                key_date = olculerimSnapshot.getKey();
                                //change date format from ISO 8601 to 01-01-2018 format
                                key_date = key_date.substring(8) + "-" + key_date.substring(5, 7) + "-" + key_date.substring(0, 4);

                                if (selected_date.equals(key_date)) {

                                    ProfilimOlcuData olcuData = olculerimSnapshot.getValue(ProfilimOlcuData.class);
                                    editText1.setText(olcuData.getOlcuRow1());
                                    editText2.setText(olcuData.getOlcuRow2());
                                    editText3.setText(olcuData.getOlcuRow3());
                                    editText4.setText(olcuData.getOlcuRow4());
                                    editText5.setText(olcuData.getOlcuRow5());
                                    editText6.setText(olcuData.getOlcuRow6());
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mOlcuDatabaseReference.addListenerForSingleValueEvent(mSingleValueEventListener);

                datePicker.setImageResource(R.drawable.date_button_purple);
                dismiss();
            }
        });
    }

    private void initializeNumberPicker() {

        if (arraylist_dates.size() > 0) {
            //set number picker min and max
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(arraylist_dates.size() - 1);
            numberPicker.setWrapSelectorWheel(false);

            //convert arraylist<String> to String[]
            mydates = arraylist_dates.toArray(new String[0]);
            //set number picker selectable values to strings (mydates)
            numberPicker.setDisplayedValues(mydates);
            //set default selected date as a first index of mydates
            selected_date = mydates[0];


            //setOnvalueChange listener to number picker to get selected date
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    selected_date = mydates[i1];

                }
            });
        } else if (arraylist_dates.size() == 0) {
            numberPicker.setDisplayedValues(null);
        }
    }
}
