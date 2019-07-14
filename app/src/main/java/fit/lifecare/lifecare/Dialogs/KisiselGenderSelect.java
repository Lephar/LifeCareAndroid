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

public class KisiselGenderSelect extends DialogFragment {

    //Layout views
    private ImageView close_button;
    private TextView kadin;
    private TextView erkek;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimKisiselDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_gender, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        kadin = view.findViewById(R.id.text_kadin);
        erkek = view.findViewById(R.id.text_erkek);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimKisiselDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("PersonalInfo").child("gender");

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

        kadin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimKisiselDatabaseReference.setValue("Kadın");
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        erkek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfilimKisiselDatabaseReference.setValue("Erkek");
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
