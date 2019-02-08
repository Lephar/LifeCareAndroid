package fit.lifecare.lifecare.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fit.lifecare.lifecare.R;

public class EatingHabitSelect extends DialogFragment {
    
    //Layout views
    private ImageView close_button;
    private TextView habit1;
    private TextView habit2;
    private TextView habit3;
    private TextView habit4;
    private TextView habit5;
    private TextView habit6;
    private TextView habit7;
    private TextView habit8;
    private TextView habit9;
    private TextView habit10;
    private TextView habit11;
    private TextView habit12;
    private TextView habit13;
    private TextView habit14;
    private TextView habit15;
    private TextView habit16;
    private TextView habit17;
    private TextView habit18;
    private TextView habit19;
    private TextView habit20;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimOgunDatabaseReference;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    
    
        View view = inflater.inflate(R.layout.dialog_select_eating_habit, container, false);
    
        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        habit1 = view.findViewById(R.id.habit1);
        habit2 = view.findViewById(R.id.habit2);
        habit3 = view.findViewById(R.id.habit3);
        habit4 = view.findViewById(R.id.habit4);
        habit5 = view.findViewById(R.id.habit5);
        habit6 = view.findViewById(R.id.habit6);
        habit7 = view.findViewById(R.id.habit7);
        habit8 = view.findViewById(R.id.habit8);
        habit9 = view.findViewById(R.id.habit9);
        habit10 = view.findViewById(R.id.habit10);
        habit11 = view.findViewById(R.id.habit11);
        habit12 = view.findViewById(R.id.habit12);
        habit13 = view.findViewById(R.id.habit13);
        habit14 = view.findViewById(R.id.habit14);
        habit15 = view.findViewById(R.id.habit15);
        habit16 = view.findViewById(R.id.habit16);
        habit17 = view.findViewById(R.id.habit17);
        habit18 = view.findViewById(R.id.habit18);
        habit19 = view.findViewById(R.id.habit19);
        habit20 = view.findViewById(R.id.habit20);
    
        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimOgunDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("ProfilimOgun").child("ProfilimOgunRow11");
    
        //initialize click listeners
        initializeButtonListeners();
        
        return view;
    }
    
    
    private void initializeButtonListeners() {
    
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    
        habit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit1.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit2.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit3.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit4.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit5.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit6.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit7.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit8.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit9.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit10.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit11.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit12.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit13.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit14.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit15.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit16.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit17.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit18.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit19.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    
        habit20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimOgunDatabaseReference.setValue(habit20.getText());
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        
    }
}
