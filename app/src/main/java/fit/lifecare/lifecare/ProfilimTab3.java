package fit.lifecare.lifecare;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimOgunData;
import fit.lifecare.lifecare.Dialogs.EatingHabitSelect;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfilimTab3 extends Fragment {

    //Layout views
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private ImageView imageView7;
    private TextView clock1;
    private TextView clock2;
    private TextView clock3;
    private TextView eatingHabit;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;

    //firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimOgunDatabaseReference;
    private ValueEventListener mValueEventListener;
    private FirebaseAuth mAuth;
    
    // shared preferences
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilim_tab3, container, false);

        //initialize layout views
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageView5 = view.findViewById(R.id.imageView5);
        imageView6 = view.findViewById(R.id.imageView6);
        imageView7 = view.findViewById(R.id.imageView7);
        clock1 = view.findViewById(R.id.imageView8);
        clock2 = view.findViewById(R.id.imageView9);
        clock3 = view.findViewById(R.id.imageView10);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);
        editText7 = view.findViewById(R.id.editText7);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView5 = view.findViewById(R.id.textView5);
        textView6 = view.findViewById(R.id.textView6);
        textView7 = view.findViewById(R.id.textView7);
        eatingHabit = view.findViewById(R.id.imageView11);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimOgunDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("ProfilimOgun");
    
        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //initialize ontouch listeners
        initializeOnTouchListeners();
        //initialize firebase listeners
        initializeFirebaseListeners();
        //initalize setOnEditorAction listeners
        initalizeOnEditorActionListener();
        //
        initializeTextViewVisibility();
        initalizeClockViews();

        return view;
    }

    //listening firebase database to update views
    private void initializeFirebaseListeners() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && getContext() != null) {
                    ProfilimOgunData profilimOgunData = dataSnapshot.getValue(ProfilimOgunData.class);
                    editText1.setText(profilimOgunData.getProfilimOgunRow1());
                    if (editText1.getText().toString().equals("yok")) {
                        imageView1.setImageResource(R.drawable.state_no);
                    } else if (editText1.getText().toString().equals("")) {
                        imageView1.setImageResource(R.drawable.state_default);
                    } else {
                        imageView1.setImageResource(R.drawable.state_yes);
                    }
                    editText2.setText(profilimOgunData.getProfilimOgunRow2());
                    if (editText2.getText().toString().equals("yok")) {
                        imageView2.setImageResource(R.drawable.state_no);
                    } else if (editText2.getText().toString().equals("")) {
                        imageView2.setImageResource(R.drawable.state_default);
                    } else {
                        imageView2.setImageResource(R.drawable.state_yes);
                    }
                    editText3.setText(profilimOgunData.getProfilimOgunRow3());
                    if (editText3.getText().toString().equals("yok")) {
                        imageView3.setImageResource(R.drawable.state_no);
                    } else if (editText3.getText().toString().equals("")) {
                        imageView3.setImageResource(R.drawable.state_default);
                    } else {
                        imageView3.setImageResource(R.drawable.state_yes);
                    }
                    editText4.setText(profilimOgunData.getProfilimOgunRow4());
                    if (editText4.getText().toString().equals("yok")) {
                        imageView4.setImageResource(R.drawable.state_no);
                    } else if (editText4.getText().toString().equals("")) {
                        imageView4.setImageResource(R.drawable.state_default);
                    } else {
                        imageView4.setImageResource(R.drawable.state_yes);
                    }
                    editText5.setText(profilimOgunData.getProfilimOgunRow5());
                    if (editText5.getText().toString().equals("yok")) {
                        imageView5.setImageResource(R.drawable.state_no);
                    } else if (editText5.getText().toString().equals("")) {
                        imageView5.setImageResource(R.drawable.state_default);
                    } else {
                        imageView5.setImageResource(R.drawable.state_yes);
                    }
                    editText6.setText(profilimOgunData.getProfilimOgunRow6());
                    if (editText6.getText().toString().equals("yok")) {
                        imageView6.setImageResource(R.drawable.state_no);
                    } else if (editText6.getText().toString().equals("")) {
                        imageView6.setImageResource(R.drawable.state_default);
                    } else {
                        imageView6.setImageResource(R.drawable.state_yes);
                    }
                    editText7.setText(profilimOgunData.getProfilimOgunRow7());
                    if (editText7.getText().toString().equals("yok")) {
                        imageView7.setImageResource(R.drawable.state_no);
                    } else if (editText7.getText().toString().equals("")) {
                        imageView7.setImageResource(R.drawable.state_default);
                    } else {
                        imageView7.setImageResource(R.drawable.state_yes);
                    }
                    clock1.setText(profilimOgunData.getProfilimOgunRow8());
                    clock2.setText(profilimOgunData.getProfilimOgunRow9());
                    clock3.setText(profilimOgunData.getProfilimOgunRow10());
                    eatingHabit.setText(profilimOgunData.getProfilimOgunRow11());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mProfilimOgunDatabaseReference.addValueEventListener(mValueEventListener);

    }

    private void initalizeClockViews() {
        //set onclicklistener to views for hiding or showing views
        clock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        
                        editor.putString("breakfast_time", String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        editor.commit();
                        
                        if (selectedHour < 10 && selectedMinute < 10) {
                            clock1.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if (selectedHour < 10) {
                            clock1.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            clock1.setText(selectedHour + ":0" + selectedMinute);
                        } else {
                            clock1.setText(selectedHour + ":" + selectedMinute);
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow8").setValue(clock1.getText().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        //set onclicklistener to views for hiding or showing views
        clock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
    
                        editor.putString("lunch_time", String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        editor.commit();
                        
                        if (selectedHour < 10 && selectedMinute < 10) {
                            clock2.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if (selectedHour < 10) {
                            clock2.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            clock2.setText(selectedHour + ":0" + selectedMinute);
                        } else {
                            clock2.setText(selectedHour + ":" + selectedMinute);
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow9").setValue(clock2.getText().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        //set onclicklistener to views for hiding or showing views
        clock3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
    
                        editor.putString("dinner_time", String.valueOf(selectedHour) + ":" + String.valueOf(selectedMinute));
                        editor.commit();
                        
                        if (selectedHour < 10 && selectedMinute < 10) {
                            clock3.setText("0" + selectedHour + ":0" + selectedMinute);
                        } else if (selectedHour < 10) {
                            clock3.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            clock3.setText(selectedHour + ":0" + selectedMinute);
                        } else {
                            clock3.setText(selectedHour + ":" + selectedMinute);
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow10").setValue(clock3.getText().toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });
    }

    //set onclicklistener to views for hiding or showing views
    private void initializeTextViewVisibility() {

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().toString().length() != 0 && !editText1.getText().toString().equals("yok")) {
                    if (editText1.isShown()) {
                        editText1.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.VISIBLE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText2.getText().toString().length() != 0 && !editText2.getText().toString().equals("yok")) {
                    if (editText2.isShown()) {
                        editText2.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.VISIBLE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText3.getText().toString().length() != 0 && !editText3.getText().toString().equals("yok")) {
                    if (editText3.isShown()) {
                        editText3.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.VISIBLE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText4.getText().toString().length() != 0 && !editText4.getText().toString().equals("yok")) {
                    if (editText4.isShown()) {
                        editText4.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.VISIBLE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText5.getText().toString().length() != 0 && !editText5.getText().toString().equals("yok")) {
                    if (editText5.isShown()) {
                        editText5.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.VISIBLE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText6.getText().toString().length() != 0 && !editText6.getText().toString().equals("yok")) {
                    if (editText6.isShown()) {
                        editText6.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.VISIBLE);
                        editText7.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText7.getText().toString().length() != 0 && !editText7.getText().toString().equals("yok")) {
                    if (editText7.isShown()) {
                        editText7.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    //set listener to keyboard done buttons to save text to firebase database
    private void initalizeOnEditorActionListener() {

        editText1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow1").setValue(editText1.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow2").setValue(editText2.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText3.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow3").setValue(editText3.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText4.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow4").setValue(editText4.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText5.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow5").setValue(editText5.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText6.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow6").setValue(editText6.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText7.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimOgunDatabaseReference.child("ProfilimOgunRow7").setValue(editText7.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //detect which part of the image touched and handle gui accordingly
    private void initializeOnTouchListeners() {
        
        eatingHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EatingHabitSelect().show(getChildFragmentManager(), "Select Height Dialog");
        
            }
        });

        imageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView1.getWidth() / 2) {
                        editText1.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow1").setValue("yok");
                        imageView1.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText1.getText().toString().equals("yok")) {
                            editText1.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow1").setValue("var");
                        editText1.setVisibility(View.VISIBLE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        imageView1.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });

        imageView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView2.getWidth() / 2) {
                        editText2.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow2").setValue("yok");
                        imageView2.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText2.getText().toString().equals("yok")) {
                            editText2.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow2").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.VISIBLE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        imageView2.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView3.getWidth() / 2) {
                        editText3.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow3").setValue("yok");
                        imageView3.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText3.getText().toString().equals("yok")) {
                            editText3.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow3").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.VISIBLE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        imageView3.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView4.getWidth() / 2) {
                        editText4.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow4").setValue("yok");
                        imageView4.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText4.getText().toString().equals("yok")) {
                            editText4.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow4").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.VISIBLE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        imageView4.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView5.getWidth() / 2) {
                        editText5.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow5").setValue("yok");
                        imageView5.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText5.getText().toString().equals("yok")) {
                            editText5.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow5").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.VISIBLE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        imageView5.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView6.getWidth() / 2) {
                        editText6.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow6").setValue("yok");
                        imageView6.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText6.getText().toString().equals("yok")) {
                            editText6.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow6").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.VISIBLE);
                        editText7.setVisibility(View.GONE);
                        imageView6.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView7.getWidth() / 2) {
                        editText7.setVisibility(View.GONE);
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow7").setValue("yok");
                        imageView7.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText7.getText().toString().equals("yok")) {
                            editText7.setText("");
                        }
                        mProfilimOgunDatabaseReference.child("ProfilimOgunRow7").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.VISIBLE);
                        imageView7.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
    }

}
