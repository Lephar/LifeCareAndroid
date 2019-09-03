package fit.lifecare.lifecare;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import fit.lifecare.lifecare.DatabaseClasses.ProfilimKanData;
import fit.lifecare.lifecare.Dialogs.KanAddDialog;
import fit.lifecare.lifecare.Dialogs.KanSelectDateDialog;

public class ProfilimTab4 extends Fragment {

    //Layout views
    private FloatingActionButton fab;
    private ImageView date_picker;
    private TextView shown_date;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView10;
    private TextView textView11;


    private ArrayList<String> dates = new ArrayList<>();

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mKanDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilim_tab4, container, false);

        //initialize layout views
        fab = view.findViewById(R.id.fab);
        date_picker = view.findViewById(R.id.blood_date);
        shown_date = view.findViewById(R.id.right0);
        textView1 = view.findViewById(R.id.right1);
        textView2 = view.findViewById(R.id.right2);
        textView3 = view.findViewById(R.id.right3);
        textView4 = view.findViewById(R.id.right4);
        textView5 = view.findViewById(R.id.right5);
        textView6 = view.findViewById(R.id.right6);
        textView7 = view.findViewById(R.id.right7);
        textView8 = view.findViewById(R.id.right8);
        textView9 = view.findViewById(R.id.right9);
        textView10 = view.findViewById(R.id.right10);
        textView11 = view.findViewById(R.id.right11);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mKanDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("KanSonuclari");

        //initialize Listeners
        initializeButtonListeners();
        initializeFireBaseListeners();


        return view;
    }

    private void initializeFireBaseListeners() {
        //listening firebase database
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && getContext() != null) {

                    String date;
                    dates.clear();

                    for (DataSnapshot olcumlerimSnapshot : dataSnapshot.getChildren()) {

                        date = olcumlerimSnapshot.getKey();
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
        mKanDatabaseReference.addValueEventListener(mValueEventListener);

        //listening firebase database
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ProfilimKanData kanData = dataSnapshot.getValue(ProfilimKanData.class);

                String theDate = dataSnapshot.getKey();
                //change date format from ISO 8601 to 01-01-2018 format
                theDate = theDate.substring(8) + "-" + theDate.substring(5, 7) + "-" + theDate.substring(0, 4);
                shown_date.setText(theDate);

                textView1.setText(kanData.getKan_row1());
                textView2.setText(kanData.getKan_row2());
                textView3.setText(kanData.getKan_row3());
                textView4.setText(kanData.getKan_row4());
                textView5.setText(kanData.getKan_row5());
                textView6.setText(kanData.getKan_row6());
                textView7.setText(kanData.getKan_row7());
                textView8.setText(kanData.getKan_row8());
                textView9.setText(kanData.getKan_row9());
                textView10.setText(kanData.getKan_row10());
                textView11.setText(kanData.getKan_row11());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                ProfilimKanData kanData = dataSnapshot.getValue(ProfilimKanData.class);

                String theDate = dataSnapshot.getKey();
                //change date format from ISO 8601 to 01-01-2018 format
                theDate = theDate.substring(8) + "-" + theDate.substring(5, 7) + "-" + theDate.substring(0, 4);
                shown_date.setText(theDate);

                textView1.setText(kanData.getKan_row1());
                textView2.setText(kanData.getKan_row2());
                textView3.setText(kanData.getKan_row3());
                textView4.setText(kanData.getKan_row4());
                textView5.setText(kanData.getKan_row5());
                textView6.setText(kanData.getKan_row6());
                textView7.setText(kanData.getKan_row7());
                textView8.setText(kanData.getKan_row8());
                textView9.setText(kanData.getKan_row9());
                textView10.setText(kanData.getKan_row10());
                textView11.setText(kanData.getKan_row11());

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
        mKanDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private void initializeButtonListeners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(245,166,35)));
                new KanAddDialog().show(getChildFragmentManager(), "Add Kan");
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dates.size() != 0) {
                    date_picker.setImageResource(R.drawable.date_button_orange);
                    KanSelectDateDialog profilimKanSelectDateDialog = new KanSelectDateDialog();
                    profilimKanSelectDateDialog.setDates(dates);
                    profilimKanSelectDateDialog.show(getChildFragmentManager(), "Select Kan Date");
                }
            }
        });

    }
}
