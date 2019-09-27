package fit.lifecare.lifecare;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import fit.lifecare.lifecare.Bluetooth.DeviceScanActivity;
import fit.lifecare.lifecare.DatabaseClasses.FormulaData;
import fit.lifecare.lifecare.DatabaseClasses.OlcumlerimData;
import fit.lifecare.lifecare.DatabaseClasses.PersonalInfoData;
import fit.lifecare.lifecare.Dialogs.WeightSelect;

public class CihazimFragment extends Fragment {

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOlcumlerimDatabaseReference;
    private DatabaseReference mPersonalDatabaseReference;
    private DatabaseReference mFormulDatabaseKadin;
    private DatabaseReference mFormulDatabaseErkek;


    private ValueEventListener mValueEventListenerPersonal;
    private ValueEventListener mValueEventListenerFormulaErkek;
    private ValueEventListener mValueEventListenerFormulaKadin;

    private FormulaData formulaDataErkek;
    private FormulaData formulaDataKadin;

    private String gender, birthday, weight, height;

    // Layout views
    private ImageView order_button;
    private Button start_button;
    private TextView emp_text;

    private static final String TAG = "BluTut";

    DeviceScanActivity deviceScanActivity;

    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 5;


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (DeviceScanActivity.ACTION_GATT_CONNECTED.equals(action)) {

                Toast.makeText(getContext(), "Bluetooth connected with Lifecare", Toast.LENGTH_SHORT).show();
            } else if (DeviceScanActivity.ACTION_GATT_DISCONNECTED.equals(action)) {

                Toast.makeText(getContext(), "Bluetooth disconnected with Lifecare", Toast.LENGTH_SHORT).show();
            } else if (DeviceScanActivity.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

                Toast.makeText(getContext(), "Bluetooth services discovered", Toast.LENGTH_SHORT).show();
            } else if (DeviceScanActivity.ACTION_DATA_AVAILABLE.equals(action)) {

                String readed_value = intent.getStringExtra(DeviceScanActivity.EXTRA_DATA);
                Float emp = Float.parseFloat(readed_value.substring(5));

                if (emp < 10000) {
                    Toast.makeText(getContext(), "Ölçüm bitti ", Toast.LENGTH_LONG).show();
                    String text_toset = "emp: " + emp;
                    emp_text.setText(text_toset);

                    // calculate with formula and put it to firebase database
                    CalculateFromBluetoothData(emp);

                } else {
                    Toast.makeText(getContext(), "Hatalı ölçüm tekrar ölçünüz ", Toast.LENGTH_LONG).show();
                }


            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_cihazim, container, false);

        // Initialize layout views
        order_button = view.findViewById(R.id.order_button);
        start_button = view.findViewById(R.id.start_device);
        emp_text = view.findViewById(R.id.emp_text);

        // initialize firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOlcumlerimDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("Olcumlerim");
        mPersonalDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");

        mFormulDatabaseKadin = mFirebaseDatabase.getReference().child("AppUsers").child("Formul_Kadın");
        mFormulDatabaseErkek = mFirebaseDatabase.getReference().child("AppUsers").child("Formul_Erkek");

        deviceScanActivity = new DeviceScanActivity(getActivity());

        initializeFirebaseListeners();

        // initialize click listeners
        initializeClickListeners();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "heydiriil");
        deviceScanActivity.scanLeDevice(false);
        deviceScanActivity.disconnectDevice();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), "ble not supported", Toast.LENGTH_SHORT).show();
        } else {

            Log.d(TAG, "everything good");

            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                // everything good so far, bluetooth is enabled and active

                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                } else {
                    // Toast.makeText(getContext(), "Location permissions already granted", Toast.LENGTH_SHORT).show();

                    deviceScanActivity.scanLeDevice(true);

                    getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                    Log.d(TAG, "le scan started");
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, yay! Start the Bluetooth device scan.

            deviceScanActivity.scanLeDevice(true);

            Log.d(TAG, "le scan started");

        } else {
            // Alert the user that this application requires the location permission to perform the scan.

            Toast.makeText(getContext(), "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeClickListeners() {


        order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://lifecare.fit/"));
                startActivity(viewIntent);

            }
        });

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new WeightSelect(new CihazOlcumFragment(), deviceScanActivity).show(getChildFragmentManager(), "Select Weight");
                deviceScanActivity.setStartClicked(true);
                deviceScanActivity.writeToDevice("re");

            }
        });
    }

    public void initializeFirebaseListeners() {


        mValueEventListenerFormulaErkek = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                formulaDataErkek = dataSnapshot.getValue(FormulaData.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mFormulDatabaseErkek.addValueEventListener(mValueEventListenerFormulaErkek);

        mValueEventListenerFormulaKadin = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                formulaDataKadin = dataSnapshot.getValue(FormulaData.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mFormulDatabaseKadin.addValueEventListener(mValueEventListenerFormulaKadin);

        mValueEventListenerPersonal = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PersonalInfoData personalInfoData = dataSnapshot.getValue(PersonalInfoData.class);

                if (personalInfoData != null) {

                    gender = personalInfoData.getGender();
                    birthday = personalInfoData.getBirth_date();
                    height = personalInfoData.getHeight();
                    weight = personalInfoData.getWeight();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mPersonalDatabaseReference.addValueEventListener(mValueEventListenerPersonal);

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_CONNECTED);
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(DeviceScanActivity.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void CalculateFromBluetoothData(Float emp) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String current_date = formatter.format(date);

        int yil = 2019;

        double boy = Double.parseDouble(height);
        double kilo = Double.parseDouble(weight);
        int yas = yil - Integer.parseInt(birthday.substring(6));

        Log.d(TAG,yas + "**" + -yas);


        if(gender.equals("Erkek")) {

            double fatfreeMass = formulaDataErkek.getK() + formulaDataErkek.getX() * ( boy*boy) / (emp + 100* formulaDataErkek.getY()) +
                    boy * formulaDataErkek.getL()  + formulaDataErkek.getZ()*(kilo + formulaDataErkek.getM()) +
                    formulaDataErkek.getT()*(-yas + formulaDataErkek.getN()) + 1 * formulaDataErkek.getO();

            double suKutlesi = fatfreeMass * 0.73;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatfreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatfreeMass;
            double yagYuzdesi = (yagKutlesi* 100) / kilo;

            double bmi = kilo / (boy * boy / 10000);
            double bazal = 88.362 + (4.799*boy) + 13.397*kilo - 5.677 * yas;

            Log.d("SONN", yagYuzdesi + "****" + emp );

            //creating a olcumlerimData object and getting data from edittext views
            OlcumlerimData olcumlerimData = new OlcumlerimData( String.valueOf(kilo) ,
                    String.valueOf(bmi ) , String.valueOf(yagYuzdesi ), String.valueOf(suYuzdesi ) ,
                    String.valueOf(kasYuzdesi), String.valueOf(bazal), "0", "0");
            //pushing olcumlerimData object to FirebaseDatabase
            mOlcumlerimDatabaseReference.child(current_date).setValue(olcumlerimData);


        } else {

            double fatfreeMass = formulaDataKadin.getK() + formulaDataKadin.getX() * ( boy*boy) / (emp + 100*formulaDataKadin.getY()) +
                    boy * formulaDataKadin.getL()  + formulaDataKadin.getZ()*(kilo + formulaDataKadin.getM()) +
                    formulaDataKadin.getT()*(-yas + formulaDataKadin.getN()) + 0 * formulaDataKadin.getO();

            double suKutlesi = fatfreeMass * 0.73;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatfreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatfreeMass;
            double yagYuzdesi = (yagKutlesi* 100) / kilo;

            double bmi = kilo / (boy * boy / 10000);
            double bazal = 88.362 + (4.799*boy) + 13.397*kilo - 5.677 * yas;

            Log.d("SONN", yagYuzdesi + "****" + emp );

            //creating a olcumlerimData object and getting data from edittext views
            OlcumlerimData olcumlerimData = new OlcumlerimData( String.valueOf(kilo) ,
                    String.valueOf(bmi ) , String.valueOf(yagYuzdesi ), String.valueOf(suYuzdesi ) ,
                    String.valueOf(kasYuzdesi), String.valueOf(bazal), "0", "0");
            //pushing olcumlerimData object to FirebaseDatabase
            mOlcumlerimDatabaseReference.child(current_date).setValue(olcumlerimData);

        }

    }

}
