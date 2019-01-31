package fit.lifecare.lifecare;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimOlcuData;
import fit.lifecare.lifecare.Dialogs.OlcuSelectDateDialog;

public class ProfilimTab5 extends Fragment {
    
    //Layout views
    private ImageView mDatePicker;
    private ImageView body_img;
    private TextView date_text;
    private FloatingActionButton fab;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private ArrayList<String> dates = new ArrayList<>();
    private boolean state_saving = false;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimOlcuDatabaseReference;
    private DatabaseReference mPersonalInfoDatabaseReference;
    private ValueEventListener mValueEventListener;
    private ValueEventListener singleListener;
    private ChildEventListener mChildEventListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilim_tab5, container, false);
        
        //initialize layout views
        mDatePicker = view.findViewById(R.id.date);
        body_img = view.findViewById(R.id.imageView);
        date_text = view.findViewById(R.id.date_text);
        fab = view.findViewById(R.id.fab);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);
        //close edittexts to edit until mAddOlcuData button clicked
        editText1.setEnabled(false);
        editText2.setEnabled(false);
        editText3.setEnabled(false);
        editText4.setEnabled(false);
        editText5.setEnabled(false);
        editText6.setEnabled(false);
        
        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimOlcuDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcu");
        mPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo").child("gender");
        
        //decide Body Image
        decideBodyImage();
        
        //initialize Listeners
        initializeButtonListeners();
        initalizeFirebaseListeners();
        
        
        return view;
    }
    
    private void decideBodyImage() {
        
        singleListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gender = dataSnapshot.getValue().toString();
                if (gender.equals("Erkek")) {
                    body_img.setImageDrawable(getResources().getDrawable(R.drawable.boy_measurement));
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        };
        mPersonalInfoDatabaseReference.addListenerForSingleValueEvent(singleListener);
        
    }
    
    private void initalizeFirebaseListeners() {
        
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String date;
                    dates.clear();
                    
                    for (DataSnapshot olculerimSnapshot : dataSnapshot.getChildren()) {
                        
                        date = olculerimSnapshot.getKey();
                        //change date format from ISO 8601 to 01-01-2018 format
                        date = date.substring(8) + "-" + date.substring(5, 7) + "-" + date.substring(0, 4);
                        Log.d("Dates : ", date);
                        dates.add(date);
                    }
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        };
        mProfilimOlcuDatabaseReference.addValueEventListener(mValueEventListener);
        
        //listening firebase database
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                
                ProfilimOlcuData profilimOlcuData = dataSnapshot.getValue(ProfilimOlcuData.class);
                editText1.setText(profilimOlcuData.getOlcuRow1());
                editText2.setText(profilimOlcuData.getOlcuRow2());
                editText3.setText(profilimOlcuData.getOlcuRow3());
                editText4.setText(profilimOlcuData.getOlcuRow4());
                editText5.setText(profilimOlcuData.getOlcuRow5());
                editText6.setText(profilimOlcuData.getOlcuRow6());
                
            }
            
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                
                ProfilimOlcuData profilimOlcuData = dataSnapshot.getValue(ProfilimOlcuData.class);
                editText1.setText(profilimOlcuData.getOlcuRow1());
                editText2.setText(profilimOlcuData.getOlcuRow2());
                editText3.setText(profilimOlcuData.getOlcuRow3());
                editText4.setText(profilimOlcuData.getOlcuRow4());
                editText5.setText(profilimOlcuData.getOlcuRow5());
                editText6.setText(profilimOlcuData.getOlcuRow6());
                
            }
            
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            
            }
            
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        };
        mProfilimOlcuDatabaseReference.addChildEventListener(mChildEventListener);
        
    }
    
    private void initializeButtonListeners() {
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!state_saving) {
                    fab.setImageResource(R.drawable.save_icon);
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245, 166, 35)));
                    state_saving = true;
                    //set visibility of  mDatePicker to gone until fab button clicked again
                    mDatePicker.setVisibility(View.INVISIBLE);
                    date_text.setVisibility(View.INVISIBLE);
                    //set edittexts to editable until save_data_button clicked
                    editText1.setEnabled(true);
                    editText2.setEnabled(true);
                    editText3.setEnabled(true);
                    editText4.setEnabled(true);
                    editText5.setEnabled(true);
                    editText6.setEnabled(true);
                } else {
                    
                    if(validateForm()) {
    
                        fab.setImageResource(R.drawable.add);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42, 48, 127)));
                        state_saving = false;
                        //get the current date
                        final Calendar calendar = Calendar.getInstance();
                        int yy = calendar.get(Calendar.YEAR);
                        int mm = calendar.get(Calendar.MONTH) + 1;
                        int dd = calendar.get(Calendar.DAY_OF_MONTH);
                        String date;
                        //this is for achieve 01-01-2018 formatted date
                        if (dd < 10 && mm < 10) {
                            date = ("0" + dd + "-" + "0" + mm + "-" + yy);
                        } else if (dd < 10) {
                            date = ("0" + dd + "-" + mm + "-" + yy);
                        } else if (mm < 10) {
                            date = (dd + "-" + "0" + mm + "-" + yy);
                        } else {
                            date = (dd + "-" + mm + "-" + yy);
                        }
                        //this is for achieve 01-01-2018 formatted date
    
                        //change datate format to ISO 8601 before pushing to firebase database
                        date = date.substring(6) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
    
                        ProfilimOlcuData profilimOlcuData = new ProfilimOlcuData(editText1.getText().toString()
                                , editText2.getText().toString(), editText3.getText().toString()
                                , editText4.getText().toString(), editText5.getText().toString()
                                , editText6.getText().toString());
                        mProfilimOlcuDatabaseReference.child(date).setValue(profilimOlcuData);
                        mDatePicker.setVisibility(View.VISIBLE);
                        date_text.setVisibility(View.VISIBLE);
                        editText1.setEnabled(false);
                        editText2.setEnabled(false);
                        editText3.setEnabled(false);
                        editText4.setEnabled(false);
                        editText5.setEnabled(false);
                        editText6.setEnabled(false);
    
                        Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    }
                    
                }
            }
        });
        
        //set onclicklistener to date picker button
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (dates.size() != 0) {
                    mDatePicker.setImageResource(R.drawable.date_button_orange);
                    OlcuSelectDateDialog olcuSelectDateDialog = new OlcuSelectDateDialog();
                    olcuSelectDateDialog.setDates(dates);
                    olcuSelectDateDialog.show(getChildFragmentManager(), "Select Olcu Date");
                }
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
        } else {
            Toast.makeText(getContext(), getString(R.string.empty_data), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
