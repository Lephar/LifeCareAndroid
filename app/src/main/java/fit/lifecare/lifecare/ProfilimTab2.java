package fit.lifecare.lifecare;

import android.content.Context;
import android.os.Bundle;
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

import fit.lifecare.lifecare.DatabaseClasses.ProfilimSaglikData;
import fit.lifecare.lifecare.Dialogs.SaglikActivitySelect;


public class ProfilimTab2 extends Fragment {

    //Layout views
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private ImageView imageView7;
    private ImageView imageView8;
    private ImageView imageView9;
    private ImageView imageView10;
    private EditText imageView11;
    private TextView imageView12;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private EditText editText9;
    private EditText editText10;
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
    private TextView textView12;

    //firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimSaglikDatabaseReference;
    private ValueEventListener mValueEventListener;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profilim_tab2, container, false);

        //initialize layout views
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);
        imageView5 = view.findViewById(R.id.imageView5);
        imageView6 = view.findViewById(R.id.imageView6);
        imageView7 = view.findViewById(R.id.imageView7);
        imageView8 = view.findViewById(R.id.imageView8);
        imageView9 = view.findViewById(R.id.imageView9);
        imageView10 = view.findViewById(R.id.imageView10);
        imageView11 = view.findViewById(R.id.imageView11);
        imageView12 = view.findViewById(R.id.imageView12);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);
        editText6 = view.findViewById(R.id.editText6);
        editText7 = view.findViewById(R.id.editText7);
        editText8 = view.findViewById(R.id.editText8);
        editText9 = view.findViewById(R.id.editText9);
        editText10 = view.findViewById(R.id.editText10);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);
        textView4 = view.findViewById(R.id.textView4);
        textView5 = view.findViewById(R.id.textView5);
        textView6 = view.findViewById(R.id.textView6);
        textView7 = view.findViewById(R.id.textView7);
        textView8 = view.findViewById(R.id.textView8);
        textView9 = view.findViewById(R.id.textView9);
        textView10 = view.findViewById(R.id.textView10);
        textView11 = view.findViewById(R.id.textView11);
        textView12 = view.findViewById(R.id.textView12);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimSaglikDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("ProfilimSaglik");

        //initialize ontouch listeners
        initializeOnTouchListeners();
        //initialize firebase listeners
        initializeFirebaseListeners();
        //initalize setOnEditorAction listeners
        initalizeOnEditorActionListener();
        initializeTextViewVisibility();

        return view;
    }

    //listening firebase database to update views
    private void initializeFirebaseListeners() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && getContext() != null) {
                    ProfilimSaglikData profilimSaglikData = dataSnapshot.getValue(ProfilimSaglikData.class);
                    editText1.setText(profilimSaglikData.getProfilimSaglikRow1());
                    if (editText1.getText().toString().equals("yok")) {
                        imageView1.setImageResource(R.drawable.state_no);
                    } else if (editText1.getText().toString().equals("")) {
                        imageView1.setImageResource(R.drawable.state_default);
                    } else {
                        imageView1.setImageResource(R.drawable.state_yes);
                    }
                    editText2.setText(profilimSaglikData.getProfilimSaglikRow2());
                    if (editText2.getText().toString().equals("yok")) {
                        imageView2.setImageResource(R.drawable.state_no);
                    } else if (editText2.getText().toString().equals("")) {
                        imageView2.setImageResource(R.drawable.state_default);
                    } else {
                        imageView2.setImageResource(R.drawable.state_yes);
                    }
                    editText3.setText(profilimSaglikData.getProfilimSaglikRow3());
                    if (editText3.getText().toString().equals("yok")) {
                        imageView3.setImageResource(R.drawable.state_no);
                    } else if (editText3.getText().toString().equals("")) {
                        imageView3.setImageResource(R.drawable.state_default);
                    } else {
                        imageView3.setImageResource(R.drawable.state_yes);
                    }
                    editText4.setText(profilimSaglikData.getProfilimSaglikRow4());
                    if (editText4.getText().toString().equals("yok")) {
                        imageView4.setImageResource(R.drawable.state_no);
                    } else if (editText4.getText().toString().equals("")) {
                        imageView4.setImageResource(R.drawable.state_default);
                    } else {
                        imageView4.setImageResource(R.drawable.state_yes);
                    }
                    editText5.setText(profilimSaglikData.getProfilimSaglikRow5());
                    if (editText5.getText().toString().equals("yok")) {
                        imageView5.setImageResource(R.drawable.state_no);
                    } else if (editText5.getText().toString().equals("")) {
                        imageView5.setImageResource(R.drawable.state_default);
                    } else {
                        imageView5.setImageResource(R.drawable.state_yes);
                    }
                    editText6.setText(profilimSaglikData.getProfilimSaglikRow6());
                    if (editText6.getText().toString().equals("yok")) {
                        imageView6.setImageResource(R.drawable.state_no);
                    } else if (editText6.getText().toString().equals("")) {
                        imageView6.setImageResource(R.drawable.state_default);
                    } else {
                        imageView6.setImageResource(R.drawable.state_yes);
                    }
                    editText7.setText(profilimSaglikData.getProfilimSaglikRow7());
                    if (editText7.getText().toString().equals("yok")) {
                        imageView7.setImageResource(R.drawable.state_no);
                    } else if (editText7.getText().toString().equals("")) {
                        imageView7.setImageResource(R.drawable.state_default);
                    } else {
                        imageView7.setImageResource(R.drawable.state_yes);
                    }
                    editText8.setText(profilimSaglikData.getProfilimSaglikRow8());
                    if (editText8.getText().toString().equals("yok")) {
                        imageView8.setImageResource(R.drawable.state_no);
                    } else if (editText8.getText().toString().equals("")) {
                        imageView8.setImageResource(R.drawable.state_default);
                    } else {
                        imageView8.setImageResource(R.drawable.state_yes);
                    }
                    editText9.setText(profilimSaglikData.getProfilimSaglikRow9());
                    if (editText9.getText().toString().equals("yok")) {
                        imageView9.setImageResource(R.drawable.state_no);
                    } else if (editText9.getText().toString().equals("")) {
                        imageView9.setImageResource(R.drawable.state_default);
                    } else {
                        imageView9.setImageResource(R.drawable.state_yes);
                    }
                    editText10.setText(profilimSaglikData.getProfilimSaglikRow10());
                    if (editText10.getText().toString().equals("yok")) {
                        imageView10.setImageResource(R.drawable.state_no);
                    } else if (editText10.getText().toString().equals("")) {
                        imageView10.setImageResource(R.drawable.state_default);
                    } else {
                        imageView10.setImageResource(R.drawable.state_yes);
                    }
                    imageView11.setText(profilimSaglikData.getProfilimSaglikRow11());
                    if (profilimSaglikData.getProfilimSaglikRow12() != null) {
                        imageView12.setText(profilimSaglikData.getProfilimSaglikRow12());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mProfilimSaglikDatabaseReference.addValueEventListener(mValueEventListener);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText8.getText().toString().length() != 0 && !editText8.getText().toString().equals("yok")) {
                    if (editText8.isShown()) {
                        editText8.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.VISIBLE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText9.getText().toString().length() != 0 && !editText9.getText().toString().equals("yok")) {
                    if (editText9.isShown()) {
                        editText9.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.VISIBLE);
                        editText10.setVisibility(View.GONE);
                    }
                }
            }
        });

        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText10.getText().toString().length() != 0 && !editText10.getText().toString().equals("yok")) {
                    if (editText10.isShown()) {
                        editText10.setVisibility(view.GONE);
                    } else {
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView11.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(imageView11, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView12.performClick();
            }
        });

    }

    //set listener to keyboard done buttons to save text to firebase database
    private void initalizeOnEditorActionListener() {
        editText1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow1").setValue(editText1.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow2").setValue(editText2.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow3").setValue(editText3.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow4").setValue(editText4.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow5").setValue(editText5.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow6").setValue(editText6.getText().toString());
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
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow7").setValue(editText7.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText8.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow8").setValue(editText8.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText9.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow9").setValue(editText9.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        editText10.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow10").setValue(editText10.getText().toString());
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Toast.makeText(getContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        imageView11.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow11").setValue(imageView11.getText().toString());
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

        imageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView1.getWidth() / 2) {
                        editText1.setVisibility(View.GONE);
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow1").setValue("yok");
                        imageView1.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText1.getText().toString().equals("yok")) {
                            editText1.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow1").setValue("var");
                        editText1.setVisibility(View.VISIBLE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow2").setValue("yok");
                        imageView2.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText2.getText().toString().equals("yok")) {
                            editText2.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow2").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.VISIBLE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow3").setValue("yok");
                        imageView3.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText3.getText().toString().equals("yok")) {
                            editText3.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow3").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.VISIBLE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow4").setValue("yok");
                        imageView4.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText4.getText().toString().equals("yok")) {
                            editText4.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow4").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.VISIBLE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow5").setValue("yok");
                        imageView5.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText5.getText().toString().equals("yok")) {
                            editText5.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow5").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.VISIBLE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow6").setValue("yok");
                        imageView6.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText6.getText().toString().equals("yok")) {
                            editText6.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow6").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.VISIBLE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
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
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow7").setValue("yok");
                        imageView7.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText7.getText().toString().equals("yok")) {
                            editText7.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow7").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.VISIBLE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
                        imageView7.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView8.getWidth() / 2) {
                        editText8.setVisibility(View.GONE);
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow8").setValue("yok");
                        imageView8.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText8.getText().toString().equals("yok")) {
                            editText8.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow8").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.VISIBLE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.GONE);
                        imageView8.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView9.getWidth() / 2) {
                        editText9.setVisibility(View.GONE);
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow9").setValue("yok");
                        imageView9.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText9.getText().toString().equals("yok")) {
                            editText9.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow9").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.VISIBLE);
                        editText10.setVisibility(View.GONE);
                        imageView9.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });
        imageView10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    if (motionEvent.getX() < imageView10.getWidth() / 2) {
                        editText10.setVisibility(View.GONE);
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow10").setValue("yok");
                        imageView10.setImageResource(R.drawable.state_no);
                    } else {
                        if (editText10.getText().toString().equals("yok")) {
                            editText10.setText("");
                        }
                        mProfilimSaglikDatabaseReference.child("ProfilimSaglikRow10").setValue("var");
                        editText1.setVisibility(View.GONE);
                        editText2.setVisibility(View.GONE);
                        editText3.setVisibility(View.GONE);
                        editText4.setVisibility(View.GONE);
                        editText5.setVisibility(View.GONE);
                        editText6.setVisibility(View.GONE);
                        editText7.setVisibility(View.GONE);
                        editText8.setVisibility(View.GONE);
                        editText9.setVisibility(View.GONE);
                        editText10.setVisibility(View.VISIBLE);
                        imageView10.setImageResource(R.drawable.state_yes);
                    }
                }

                return false;
            }
        });

        imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SaglikActivitySelect().show(getChildFragmentManager(), "Select Height Dialog");
            }
        });
    }
}
