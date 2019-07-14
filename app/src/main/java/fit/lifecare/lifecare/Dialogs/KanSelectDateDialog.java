package fit.lifecare.lifecare.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimKanData;
import fit.lifecare.lifecare.R;

public class KanSelectDateDialog extends DialogFragment {

    //Layout views
    private ImageView closeButton;
    private ImageView tamamButton;
    private ImageView deleteButton;
    private NumberPicker numberPicker;
    private ImageView datePicker;
    private TextView date;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView10;
    private TextView textView11;


    private String selected_date;
    private ArrayList<String> arraylist_dates = new ArrayList<>();
    private String[] mydates = {};


    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mKanDatabaseReference;
    private ValueEventListener mSingleValueEventListener;


    public KanSelectDateDialog() {
    }

    public void setDates(ArrayList<String> dates) {
        this.arraylist_dates = dates;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_date, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize buttons
        closeButton = view.findViewById(R.id.close_button);
        tamamButton = view.findViewById(R.id.tamam_button);
        deleteButton = view.findViewById(R.id.delete_button);
        numberPicker = view.findViewById(R.id.numberPicker);

        //get parent view
        Activity parentview = getActivity();
        datePicker = parentview.findViewById(R.id.date);

        //initialize textviews
        date = parentview.findViewById(R.id.right0);
        textView1 = parentview.findViewById(R.id.right1);
        textView2 = parentview.findViewById(R.id.right2);
        textView3 = parentview.findViewById(R.id.right3);
        textView4 = parentview.findViewById(R.id.right4);
        textView5 = parentview.findViewById(R.id.right5);
        textView6 = parentview.findViewById(R.id.right6);
        textView7 = parentview.findViewById(R.id.right7);
        textView8 = parentview.findViewById(R.id.right8);
        textView9 = parentview.findViewById(R.id.right9);
        textView10 = parentview.findViewById(R.id.right10);
        textView11 = parentview.findViewById(R.id.right11);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mKanDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("KanSonuclari");

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

                                mKanDatabaseReference.child(deleting_date).removeValue();
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

                            for (DataSnapshot kanSnapshot : dataSnapshot.getChildren()) {

                                key_date = kanSnapshot.getKey();
                                //change date format from ISO 8601 to 01-01-2018 format
                                key_date = key_date.substring(8) + "-" + key_date.substring(5, 7) + "-" + key_date.substring(0, 4);

                                if (selected_date.equals(key_date)) {

                                    ProfilimKanData kanData = kanSnapshot.getValue(ProfilimKanData.class);
                                    date.setText(key_date);
                                    textView1.setText(kanData.getKan_row1());
                                    textView2.setText(kanData.getKan_row2());
                                    textView3.setText(kanData.getKan_row3());
                                    textView4.setText(kanData.getKan_row4());
                                    textView5.setText(kanData.getKan_row5());
                                    textView6.setText(kanData.getKan_row6());
                                    textView7.setText(kanData.getKan_row7());
                                    textView8.setText(kanData.getKan_row8());
                                    textView9.setText(kanData.getKan_row9());
                                    textView10.setText(kanData.getKan_row10());
                                    textView11.setText(kanData.getKan_row11());
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mKanDatabaseReference.addListenerForSingleValueEvent(mSingleValueEventListener);

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
