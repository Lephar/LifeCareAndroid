package fit.lifecare.lifecare;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeAfterSignup extends AppCompatActivity {

    //Layout view
    private TextView welcome_text;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserPersonalInfoDatabaseReference;
    private ValueEventListener mValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_after_signup);

        //initialize view
        welcome_text = findViewById(R.id.welcome_text);

        //initalize Firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo").child("name");

        //read the name from firebase database
        attachPersonalDatabaseReadListener();

        //wait 2 second to pass next activity
        delay2sec();

    }

    private void attachPersonalDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue(String.class);
                    //replace "x" char  with the user name
                    welcome_text.setText(welcome_text.getText().toString().replace("x", name));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mUserPersonalInfoDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        }
    }

    private void delay2sec() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //start your activity here
                Intent intent = new Intent(WelcomeAfterSignup.this, SelectGenderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, 2000L);
    }
}
