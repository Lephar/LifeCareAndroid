package fit.lifecare.lifecare.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fit.lifecare.lifecare.R;

public class SaglikActivitySelect extends DialogFragment {

    //Layout views
    private ImageView close_button;
    private TextView level1;
    private TextView level2;
    private TextView level3;
    private TextView level4;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimSaglikDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_activity, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        level1 = view.findViewById(R.id.level1);
        level2 = view.findViewById(R.id.level2);
        level3 = view.findViewById(R.id.level3);
        level4 = view.findViewById(R.id.level4);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimSaglikDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("ProfilimSaglik").child("ProfilimSaglikRow12");

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

        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimSaglikDatabaseReference.setValue(getString(R.string.sedanter));
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimSaglikDatabaseReference.setValue(getString(R.string.mild));
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimSaglikDatabaseReference.setValue(getString(R.string.moderate));
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        level4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimSaglikDatabaseReference.setValue(getString(R.string.heavy));
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
