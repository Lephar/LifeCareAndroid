package fit.lifecare.lifecare.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fit.lifecare.lifecare.R;

public class DanismanimCodeAddDialog extends DialogFragment {

    //Layout views
    private View view;
    private ImageView close_button;
    private ImageView tamam_button;
    private EditText mEditText;
    private FloatingActionButton fab;

    private String currentUserId;
    private String currentName;


    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mChatDatabaseReference;
    private DatabaseReference mAppUsersReference;
    private DatabaseReference mWebUsersReference;
    private ValueEventListener mValueEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_add_code, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        tamam_button = view.findViewById(R.id.tamam_button);
        mEditText = view.findViewById(R.id.editText);

        Activity parentview = getActivity();
        fab = parentview.findViewById(R.id.fab);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChatDatabaseReference = mFirebaseDatabase.getReference().child("Chats");
        mAppUsersReference = mFirebaseDatabase.getReference().child("AppUsers");
        mWebUsersReference = mFirebaseDatabase.getReference().child("WebUsers");


        getCurrentUserInfo();
        //initialize click listeners
        initializeViewListeners();

        return view;
    }

    //get the current user's name
    private void getCurrentUserInfo() {

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mAppUsersReference.child(currentUserId).child("PersonalInfo").child("name").addListenerForSingleValueEvent(mValueEventListener);
    }

    private void attachFirebaseListener() {

        //listening firebase database
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String chatID;

                    for (DataSnapshot olcumlerimSnapshot : dataSnapshot.getChildren()) {

                        chatID = olcumlerimSnapshot.getKey();
                        if(chatID.equals(mEditText.getText().toString())  ||  chatID.equals( "-" + mEditText.getText().toString()) ) {

                            String AppUserID = (String) olcumlerimSnapshot.child("AppUserID").getValue();

                            if(AppUserID != null) {
                                Toast.makeText(view.getContext(),getString(R.string.used_code),Toast.LENGTH_SHORT).show();
                            } else {
                                String DietitianID = (String) olcumlerimSnapshot.child("dietitian_id").getValue();

                                mChatDatabaseReference.child(chatID).child("AppUserID").setValue(currentUserId);
                                mChatDatabaseReference.child(chatID).child("AppUserName").setValue(currentName);
                                mChatDatabaseReference.child(chatID).child("connection_status").setValue("active");
                                mWebUsersReference.child("UserInfos").child(DietitianID)
                                        .child("ChatIDs").child(currentUserId).setValue(chatID);
                                mAppUsersReference.child(currentUserId)
                                        .child("PersonalInfo").child("ChatIDs").child(DietitianID).setValue(chatID);

                                Toast.makeText(view.getContext(),getString(R.string.match_succes),Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                    }
                    Toast.makeText(view.getContext(), getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mChatDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
    }

    private void initializeViewListeners() {
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42,48,127)));
                dismiss();
            }
        });

        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attachFirebaseListener();
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(42,48,127)));
                dismiss();
            }
        });


        //hide the keyboard if user touch somewhere else than edittext
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                return true;
            }
        });

    }
}
