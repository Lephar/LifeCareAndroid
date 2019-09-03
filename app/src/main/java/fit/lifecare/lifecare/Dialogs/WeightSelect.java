package fit.lifecare.lifecare.Dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fit.lifecare.lifecare.Bluetooth.DeviceScanActivity;
import fit.lifecare.lifecare.CihazOlcumFragment;
import fit.lifecare.lifecare.R;

public class WeightSelect extends DialogFragment {

    private CihazOlcumFragment cihazOlcumFragment;
    private DeviceScanActivity deviceScanActivity;
    //Layout views
    private ImageView close_button;
    private ImageView tamam_button;
    private EditText weight_select;
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProfilimKisiselDatabaseReference;

    public WeightSelect(CihazOlcumFragment cihazOlcumFragment, DeviceScanActivity deviceScanActivity) {
        this.cihazOlcumFragment = cihazOlcumFragment;
        this.deviceScanActivity = deviceScanActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_weight, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        tamam_button = view.findViewById(R.id.tamam_button);
        weight_select = view.findViewById(R.id.select_weight);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProfilimKisiselDatabaseReference = mFirebaseDatabase.getReference()
                .child("AppUsers").child(currentUserId).child("PersonalInfo").child("weight");
    

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

        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                mProfilimKisiselDatabaseReference.setValue(weight_select.getText().toString());
                final Context context = getContext();
                Toast.makeText(context, "3 saniye içinde Ölçüm başlayacak", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deviceScanActivity.setStartClicked(true);
                        deviceScanActivity.WriteToDevice("re");
                        cihazOlcumFragment.getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Ölçüm başladı", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
                dismiss();
            }
        });
    }
}
