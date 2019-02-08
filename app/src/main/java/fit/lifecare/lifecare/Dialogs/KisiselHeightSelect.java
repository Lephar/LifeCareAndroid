package fit.lifecare.lifecare.Dialogs;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fit.lifecare.lifecare.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class KisiselHeightSelect extends DialogFragment {

    //Layout views
    private ImageView close_button;
    private ImageView tamam_button;
    private NumberPicker mNumberPicker;

    private String selected_value = "170";
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimKisiselDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_height, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        tamam_button = view.findViewById(R.id.tamam_button);
        mNumberPicker = view.findViewById(R.id.numberPicker);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimKisiselDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("PersonalInfo").child("height");
    
        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //initialize click listeners
        initializeButtonListeners();
        //initialize datePicker
        initializeNumberPicker();

        return view;
    }

    private void initializeNumberPicker() {
        mNumberPicker.setMinValue(100);
        mNumberPicker.setMaxValue(250);
        mNumberPicker.setValue(170);

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
                selected_value = Integer.toString(mNumberPicker.getValue());
                editor.putString("height", selected_value);
                editor.commit();
                mProfilimKisiselDatabaseReference.setValue(selected_value);
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
