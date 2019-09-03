package fit.lifecare.lifecare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;

import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;
import fit.lifecare.lifecare.Dialogs.MeasurementDateDialog;

public class OlcumGirisFragment extends Fragment {

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private DatabaseReference mPersonalDatabaseReference;

    private ImageButton dateButton;
    private TextView dateText;
    private int year, month, day;

    private EditText weightText, bmiText, metaText, fatPText, fatMText, watPText, watMText, musPText, musMText;
    private float height, weight, bmi, fatPer, watPer, musPer, fatMass, watMass, musMass, epsilon;

    private Button saveButton;

    public OlcumGirisFragment() {
    }

    public OlcumGirisFragment(String val) {
        height = Integer.parseInt(val);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_olcum_giris, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcumlerimDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcumlerim");
        mPersonalDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");

        dateButton = getView().findViewById(R.id.measurementSelectDateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MeasurementDateDialog(OlcumGirisFragment.this).show(getChildFragmentManager(), "Select Date Dialog");
            }
        });

        saveButton = getView().findViewById(R.id.measSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a olcumlerimData object and getting data from edittext views

                if (!fatPText.getText().toString().isEmpty() || !musPText.getText().toString().isEmpty() || !watPText.getText().toString().isEmpty()) {
                    if (weightText.getText().toString().isEmpty()) weightText.setText("0");
                    if (bmiText.getText().toString().isEmpty()) bmiText.setText("0");
                    if (fatPText.getText().toString().isEmpty()) fatPText.setText("0");
                    if (musPText.getText().toString().isEmpty()) musPText.setText("0");
                    if (watPText.getText().toString().isEmpty()) watPText.setText("0");
                    if (metaText.getText().toString().isEmpty()) metaText.setText("0");

                    OlcumlerimData olcumlerimData = new OlcumlerimData(
                            weightText.getText().toString().replaceAll(",", "."),
                            bmiText.getText().toString().replaceAll(",", "."),
                            fatPText.getText().toString().replaceAll(",", "."),
                            watPText.getText().toString().replaceAll(",", "."),
                            musPText.getText().toString().replaceAll(",", "."),
                            metaText.getText().toString().replaceAll(",", "."),
                            "", "");

                    mOlcumlerimDatabaseReference.child(year + (month < 9 ? "-0" : "-") + (month + 1) + (day < 10 ? "-0" : "-") + day).setValue(olcumlerimData);
                    mPersonalDatabaseReference.child("weight").setValue(weightText.getText().toString());
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(), "En az bir veri girmeniz gerekli", Toast.LENGTH_SHORT).show();
            }
        });

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        dateText = getView().findViewById(R.id.measurementDateText);
        dateText.setText((day < 10 ? "0" : "") + day + (month < 9 ? ".0" : ".") + (month + 1) + "." + year);
        weight = 0;
        epsilon = 0.0001f;

        weightText = getView().findViewById(R.id.getWeightText);
        fatPText = getView().findViewById(R.id.getFatPercentText);
        fatMText = getView().findViewById(R.id.getFatMassText);
        watPText = getView().findViewById(R.id.getWaterPercentText);
        watMText = getView().findViewById(R.id.getWaterMassText);
        musPText = getView().findViewById(R.id.getMusclePercentText);
        musMText = getView().findViewById(R.id.getMuscleMassText);
        metaText = getView().findViewById(R.id.getMetaText);
        bmiText = getView().findViewById(R.id.getBmiText);

        weightText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (weightText.isFocused()) {
                    try {
                        weight = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        weight = 0;
                    }

                    bmi = weight / ((height / 100) * (height / 100));

                    if (bmi < epsilon)
                        bmiText.getText().clear();
                    else
                        bmiText.setText(new DecimalFormat("0.00").format(bmi));
                }
            }
        });

        bmiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (bmiText.isFocused()) {
                    try {
                        bmi = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        bmi = 0;
                    }

                    weight = bmi * ((height / 100) * (height / 100));
                    if (weight < epsilon)
                        weightText.getText().clear();
                    else
                        weightText.setText(new DecimalFormat("0.00").format(weight));
                }
            }
        });

        fatPText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fatPText.isFocused()) {
                    try {
                        fatPer = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        fatPer = 0;
                    }
                }
            }
        });

        fatMText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (fatMText.isFocused()) {
                    try {
                        fatMass = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        fatMass = 0;
                    }
                }
            }
        });

        musPText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (musPText.isFocused()) {
                    try {
                        musPer = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        musPer = 0;
                    }
                }
            }
        });

        musMText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (musMText.isFocused()) {
                    try {
                        musMass = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        musMass = 0;
                    }
                }
            }
        });

        watPText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (watPText.isFocused()) {
                    try {
                        watPer = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        watPer = 0;
                    }
                }
            }
        });

        watMText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (watMText.isFocused()) {
                    try {
                        watMass = Float.parseFloat(editable.toString());
                    } catch (Exception exc) {
                        watMass = 0;
                    }
                }
            }
        });
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDateText(String text) {
        dateText.setText(text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
