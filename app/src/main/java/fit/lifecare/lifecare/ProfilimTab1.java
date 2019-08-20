package fit.lifecare.lifecare;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
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

import fit.lifecare.lifecare.DatabaseClasses.PersonalInfoData;
import fit.lifecare.lifecare.Dialogs.KisiselBirthdaySelect;
import fit.lifecare.lifecare.Dialogs.KisiselGenderSelect;
import fit.lifecare.lifecare.Dialogs.KisiselHeightSelect;

public class ProfilimTab1 extends Fragment {
    
    //global arrays
    private String[] countries;
    private String[] cities;
    
    //Layout views
    private TextView gender_select;
    private TextView gender_textview;
    private TextView birthday_select;
    private TextView birthday_textview;
    private TextView height_select;
    private TextView height_textview;
    private TextView phone_textview;
    private TextView country_textview;
    private EditText phone_edit;
    private TextView city_textview;
    private AutoCompleteTextView country_edit;
    private AutoCompleteTextView city_edit;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimKisiselDatabaseReference;
    private ValueEventListener mValueEventListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilim_tab1, container, false);
        
        //initialize layout views
        gender_textview = view.findViewById(R.id.textView1);
        gender_select = view.findViewById(R.id.textView2);
        birthday_textview = view.findViewById(R.id.textView3);
        birthday_select = view.findViewById(R.id.textView4);
        height_textview = view.findViewById(R.id.textView5);
        height_select = view.findViewById(R.id.textView6);
        phone_textview = view.findViewById(R.id.textView7);
        phone_edit = view.findViewById(R.id.textView8);
        country_textview = view.findViewById(R.id.textView9);
        country_edit = view.findViewById(R.id.textView10);
        city_textview = view.findViewById(R.id.textView11);
        city_edit = view.findViewById(R.id.textView12);
        
        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimKisiselDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("PersonalInfo");
        
        //initialize Listeners
        initializeButtonListeners();
        initializeFirebaseListeners();
        //initializeAutoCompleteTextViews
        initializeAutoCompleters();
        //handle edittext views actions
        editTextsActions();
        
        return view;
    }
    
    private void editTextsActions() {
        //set listener to keyboard done button to save text to firebase database
        phone_edit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimKisiselDatabaseReference.child("phone_num").setValue(phone_edit.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        
        country_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String country = (String) adapterView.getItemAtPosition(i);
                mProfilimKisiselDatabaseReference.child("country").setValue(country);
                
                if (country.equals("Türkiye")) {
                    city_textview.setVisibility(View.VISIBLE);
                    city_edit.setVisibility(View.VISIBLE);
                } else {
                    mProfilimKisiselDatabaseReference.child("city").removeValue();
                    city_edit.setVisibility(View.GONE);
                    city_textview.setVisibility(View.GONE);
                }
            }
        });
        
        city_edit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city = (String) adapterView.getItemAtPosition(i);
                mProfilimKisiselDatabaseReference.child("city").setValue(city);
            }
        });
        
    }
    
    private void initializeAutoCompleters() {
        
        countries = getResources().getStringArray(R.array.countries_array);
        cities = getResources().getStringArray(R.array.turkey_city);
        
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, countries);
        
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, cities);
        
        country_edit.setAdapter(adapter1);
        city_edit.setAdapter(adapter2);
        
    }
    
    private void initializeButtonListeners() {
        phone_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_edit.setCursorVisible(true);
            }
        });
        
        gender_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KisiselGenderSelect().show(getChildFragmentManager(), "Select Gender Dialog");
            }
        });
        
        birthday_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KisiselBirthdaySelect().show(getChildFragmentManager(), "Select Birthday Dialog");
            }
        });

        height_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KisiselHeightSelect().show(getChildFragmentManager(), "Select Height Dialog");
            }
        });
        
        gender_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender_select.performClick();
            }
        });
        
        birthday_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthday_select.performClick();
            }
        });
        
        height_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                height_select.performClick();
            }
        });
        
        phone_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_edit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(phone_edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        
        country_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country_edit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(country_edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        
        city_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_edit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(city_edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        
        
    }
    
    //listening firebase database to update ui
    private void initializeFirebaseListeners() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                PersonalInfoData personalInfoData = dataSnapshot.getValue(PersonalInfoData.class);
                
                if (personalInfoData != null) {
                    
                    String gender = personalInfoData.getGender();
                    if(gender.equals("Erkek")) {
                        gender_select.setText(getString(R.string.boy));
                    } else {
                        gender_select.setText(getString(R.string.girl));
                    }
                    birthday_select.setText(personalInfoData.getBirth_date());
                    height_select.setText(personalInfoData.getHeight());
                    //check if user entered any location before, there is no need to check for others
                    //because we get them while user creating new account
                    if (personalInfoData.getPhone_num() != null) {
                        phone_edit.setCursorVisible(false);
                        phone_edit.setText(personalInfoData.getPhone_num());
                    }
                    if (personalInfoData.getCountry() != null) {
                        country_edit.setText(personalInfoData.getCountry());
                        if (!country_edit.getText().toString().equals("Türkiye")) {
                            city_textview.setVisibility(View.GONE);
                            city_edit.setVisibility(View.GONE);
                        } else {
                            if (personalInfoData.getCity() != null) {
                                city_edit.setText(personalInfoData.getCity());
                            }
                        }
                    }
                }
                
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        };
        mProfilimKisiselDatabaseReference.addValueEventListener(mValueEventListener);
    }
}
