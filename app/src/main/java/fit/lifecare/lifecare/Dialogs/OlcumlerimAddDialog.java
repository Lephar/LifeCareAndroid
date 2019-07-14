package fit.lifecare.lifecare.Dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;
import fit.lifecare.lifecare.R;

public class OlcumlerimAddDialog extends DialogFragment {
    
    //Layout views
    private View view;
    private ImageView closeButton;
    private ImageView tamamButton;
    private ImageView showDateImageView;
    private TextView showDateTextView;
    private EditText mEdittext_row1;
    private TextView mEdittext_row2;
    private EditText mEdittext_row3;
    private EditText mEdittext_row4;
    private EditText mEdittext_row5;
    private EditText mEdittext_row6;
    private EditText mEdittext_row7;
    private TextView textView_row0;
    private TextView textView_row1;
    private TextView textView_row2;
    private TextView textView_row3;
    private TextView textView_row4;
    private TextView textView_row5;
    private TextView textView_row6;
    private TextView textView_row7;
    private FloatingActionButton fab;
    
    private String theDate;
    private String height;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private DatabaseReference mPersonalDatabaseReference;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_olcum_data, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        
        String selected_date = getArguments().getString("date");
        height = getArguments().getString("height");
        
        //initialize views
        closeButton = view.findViewById(R.id.close_button);
        tamamButton = view.findViewById(R.id.tamam_button);
        showDateImageView = view.findViewById(R.id.date_show);
        showDateTextView = view.findViewById(R.id.date_show_text);
        mEdittext_row1 = view.findViewById(R.id.right1);
        mEdittext_row2 = view.findViewById(R.id.right2);
        mEdittext_row3 = view.findViewById(R.id.right3);
        mEdittext_row4 = view.findViewById(R.id.right4);
        mEdittext_row5 = view.findViewById(R.id.right5);
        mEdittext_row6 = view.findViewById(R.id.right6);
        mEdittext_row7 = view.findViewById(R.id.right7);
        textView_row0 = view.findViewById(R.id.date);
        textView_row1 = view.findViewById(R.id.left1);
        textView_row2 = view.findViewById(R.id.left2);
        textView_row3 = view.findViewById(R.id.left3);
        textView_row4 = view.findViewById(R.id.left4);
        textView_row5 = view.findViewById(R.id.left5);
        textView_row6 = view.findViewById(R.id.left6);
        textView_row7 = view.findViewById(R.id.left7);
        
        Activity parentview = getActivity();
        fab = parentview.findViewById(R.id.fab);
        
        
        showDateTextView.setText(selected_date);
        
        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcumlerimDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcumlerim");
        mPersonalDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");
        
        //initialize onClick Listeners
        initOnClickListeners();
        
        return view;
    }
    
    
    private void initOnClickListeners() {
        
        mEdittext_row1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Float total_mass = Float.parseFloat(s.toString().trim());
                    Float h = Float.parseFloat(height.trim()) / 100;
                    if (h != 0) {
                        String calculated_bmi = String.valueOf(total_mass / (h * h));
                        mEdittext_row2.setText(calculated_bmi);
                    }
                } catch (Exception e) {
                    Log.d("exsepsın", e.getMessage());
                    mEdittext_row2.setText("");
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
        
        textView_row0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTextView.performClick();
            }
        });
        
        textView_row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row1.requestFocus();
            }
        });
        
        textView_row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row3.requestFocus();
            }
        });
        textView_row4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row4.requestFocus();
            }
        });
        textView_row5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row5.requestFocus();
            }
        });
        textView_row6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row6.requestFocus();
            }
        });
        textView_row7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEdittext_row7.requestFocus();
            }
        });
        
        
        //set Onclicklistener to closeButton
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42, 48, 127)));
                dismiss();
            }
        });
        
        //set onClickListener to tamamButton for saving current content to firebase database
        tamamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (validateForm()) {
                    theDate = showDateTextView.getText().toString();
                    //change theDate format to ISO 8601 before pushing to firebase database
                    theDate = theDate.substring(6) + "-" + theDate.substring(3, 5) + "-" + theDate.substring(0, 2);
                    
                    //creating a olcumlerimData object and getting data from edittext views
                    OlcumlerimData olcumlerimData = new OlcumlerimData(
                            mEdittext_row1.getText().toString(), mEdittext_row2.getText().toString()
                            , mEdittext_row3.getText().toString(), mEdittext_row4.getText().toString()
                            , mEdittext_row5.getText().toString(), mEdittext_row6.getText().toString()
                            , mEdittext_row7.getText().toString());
                    //pushing olcumlerimData object to FirebaseDatabase
                    mOlcumlerimDatabaseReference.child(theDate).setValue(olcumlerimData);
                    mPersonalDatabaseReference.child("weight").setValue(mEdittext_row1.getText().toString());
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42, 48, 127)));
                    dismiss();
                }
            }
        });
        
        showDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the current date info
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);
                
                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                
                                //this code block for getting 01-01-2018 date format
                                if (dayOfMonth < 10 && month < 10) {
                                    showDateTextView.setText("0" + dayOfMonth + "-" + "0" + month + "-" + year);
                                } else if (dayOfMonth < 10) {
                                    showDateTextView.setText("0" + dayOfMonth + "-" + month + "-" + year);
                                } else if (month < 10) {
                                    showDateTextView.setText(dayOfMonth + "-" + "0" + month + "-" + year);
                                } else {
                                    showDateTextView.setText(dayOfMonth + "-" + month + "-" + year);
                                }
                                //this code block for getting 01-01-2018 date format
                            }
                        }, yil, ay, gun);
                
                //prevent selecting future dates
                dpd.getDatePicker().setMaxDate(takvim.getTimeInMillis());
                
                //set button text
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
        
        if (TextUtils.isEmpty(mEdittext_row1.getText().toString())) {
            Toast.makeText(getContext(), getString(R.string.enter_weight), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEdittext_row1.getText().toString().equals("0")) {
            Toast.makeText(getContext(), getString(R.string.enter_weight), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
