package fit.lifecare.lifecare.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimKanData;
import fit.lifecare.lifecare.R;

public class KanAddDialog extends DialogFragment {
    
    //Layout views
    private TextView textView0;
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
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;
    private EditText editText10;
    private EditText editText11;
    private ImageView tamam_button;
    private ImageView close_button;
    private ImageView dateImageView;
    private TextView date;
    private View view;
    
    private FloatingActionButton fab;
    
    private String theDate;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mKanDatabaseReference;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_kan_data, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        
        //initialize layout views
        textView0 = view.findViewById(R.id.date);
        textView1 = view.findViewById(R.id.left1);
        textView2 = view.findViewById(R.id.left2);
        textView3 = view.findViewById(R.id.left3);
        textView4 = view.findViewById(R.id.left4);
        textView5 = view.findViewById(R.id.left5);
        textView6 = view.findViewById(R.id.left6);
        textView7 = view.findViewById(R.id.left7);
        textView8 = view.findViewById(R.id.left8);
        textView9 = view.findViewById(R.id.left9);
        textView10 = view.findViewById(R.id.left10);
        textView11 = view.findViewById(R.id.left11);
        editText1 = view.findViewById(R.id.right1);
        editText2 = view.findViewById(R.id.right2);
        editText3 = view.findViewById(R.id.right3);
        editText4 = view.findViewById(R.id.right4);
        editText5 = view.findViewById(R.id.right5);
        editText6 = view.findViewById(R.id.right6);
        editText7 = view.findViewById(R.id.right7);
        editText8 = view.findViewById(R.id.right8);
        editText9 = view.findViewById(R.id.right9);
        editText10 = view.findViewById(R.id.right10);
        editText11 = view.findViewById(R.id.right11);
        tamam_button = view.findViewById(R.id.tamam_button);
        close_button = view.findViewById(R.id.close_button);
        dateImageView = view.findViewById(R.id.date_show);
        date = view.findViewById(R.id.date_show_text);
        
        Activity parentview = getActivity();
        fab = parentview.findViewById(R.id.fab);
        
        //set current date to date view
        //get the current date
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        
        //set the textview according to current date
        //this is for achieve 01-01-2018 formatted date
        if (dd < 10 && mm < 10) {
            date.setText("0" + dd + "-" + "0" + mm + "-" + yy);
        } else if (dd < 10) {
            date.setText("0" + dd + "." + mm + "-" + yy);
        } else if (mm < 10) {
            date.setText(dd + "-" + "0" + mm + "-" + yy);
        } else {
            date.setText(dd + "-" + mm + "-" + yy);
        }
        //this is for achieve 01-01-2018 formatted date
        
        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mKanDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("KanSonuclari");
        
        //initialize listeners
        initializeViewListeners();
        
        return view;
        
        
    }
    
    private void initializeViewListeners() {
        
        textView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.performClick();
            }
        });
        
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.requestFocus();
            }
        });
        
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText2.requestFocus();
            }
        });
        
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText3.requestFocus();
            }
        });
        
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText4.requestFocus();
            }
        });
        
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText5.requestFocus();
            }
        });
        
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText6.requestFocus();
            }
        });
        
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText7.requestFocus();
            }
        });
        
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText8.requestFocus();
            }
        });
        
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText9.requestFocus();
            }
        });
        
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText10.requestFocus();
            }
        });
        
        textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText11.requestFocus();
            }
        });
        
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42, 48, 127)));
                dismiss();
            }
        });
        
        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (validateForm()) {
                    
                    theDate = date.getText().toString();
                    //change theDate format to ISO 8601 before pushing to firebase database
                    theDate = theDate.substring(6) + "-" + theDate.substring(3, 5) + "-" + theDate.substring(0, 2);
                    
                    //creating a kanData object and getting data from edittext views
                    ProfilimKanData kanData = new ProfilimKanData(editText1.getText().toString(),
                            editText2.getText().toString(), editText3.getText().toString(),
                            editText4.getText().toString(), editText5.getText().toString(),
                            editText6.getText().toString(), editText7.getText().toString(),
                            editText8.getText().toString(), editText9.getText().toString(),
                            editText10.getText().toString(), editText11.getText().toString());
                    //pushing kanData object to FirebaseDatabase
                    mKanDatabaseReference.child(theDate).setValue(kanData);
                    
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42, 48, 127)));
                    dismiss();
                }
            }
        });
        
        
        //start a new datePickerDialog for user to pick a date
        dateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current date info
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);
                
                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // increment month value because it starts from 0 index
                                month += 1;
                                //this is for achieve 01-01-2018 formatted date
                                if (dayOfMonth < 10 && month < 10) {
                                    date.setText("0" + dayOfMonth + "-" + "0" + month + "-" + year);
                                } else if (dayOfMonth < 10) {
                                    date.setText("0" + dayOfMonth + "-" + month + "-" + year);
                                } else if (month < 10) {
                                    date.setText(dayOfMonth + "-" + "0" + month + "-" + year);
                                } else {
                                    date.setText(dayOfMonth + "-" + month + "-" + year);
                                }
                                //this is for achieve 01-01-2018 formatted date
                            }
                        }, yil, ay, gun);
                
                //prevent selecting future dates
                dpd.getDatePicker().setMaxDate(takvim.getTimeInMillis());
                
                //set button informations of dialog screen
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.select), dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), dpd);
                dpd.show();
            }
        });
        
        //hide the keyboard if user touch somewhere else than edittext
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    
                }
                return true;
            }
        });
    }
    
    //to prevent user enter totally empty data
    private boolean validateForm() {
        
        if (!TextUtils.isEmpty(editText1.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText2.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText3.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText4.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText5.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText6.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText7.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText8.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText9.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText10.getText().toString())) {
            return true;
        } else if (!TextUtils.isEmpty(editText11.getText().toString())) {
            return true;
        } else {
            Toast.makeText(getContext(), getString(R.string.empty_data), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
