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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import static android.app.Activity.RESULT_OK;

public class CihazOlcumFragment extends Fragment {

    private static final String TAG = "BluTut";
    private static final int REQUEST_ENABLE_BT = 5;
    boolean authorized = false;
    DeviceScanActivity deviceScanActivity;
    private ViewPager viewPager;
    private Button start_button;
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
                    getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.GONE);

                    // calculate with formula and put it to firebase database
                    CalculateFromBluetoothData(emp);
                    deviceScanActivity.setStartClicked(false);
                    deviceScanActivity = null;
                    getActivity().finish();

                } else {
                    Toast.makeText(getContext(), "Hatalı ölçüm tekrar ölçünüz ", Toast.LENGTH_LONG).show();
                    getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.GONE);
                }
            }
        }
    };
    private BluetoothAdapter bluetoothAdapter;

    public CihazOlcumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CihazOlcumFragment(ViewPager pager) {
        viewPager = pager;
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_CONNECTED);
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(DeviceScanActivity.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(DeviceScanActivity.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cihaz_olcum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        start_button = getView().findViewById(R.id.bluetoothButton);

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deviceScanActivity = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            start_button.performClick();
        } else {
            Log.d(TAG, "heydiriil");
            deviceScanActivity.scanLeDevice(false);
            deviceScanActivity.disconnectDevice();
            viewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, yay! Start the Bluetooth device scan.
            start_button.performClick();
            Log.d(TAG, "le scan started");

        } else {
            // Alert the user that this application requires the location permission to perform the scan.
            Toast.makeText(getContext(), "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            viewPager.setCurrentItem(1);
        }

    }

    private void initializeClickListeners() {

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    authorized = false;
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
                        authorized = false;
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        // everything good so far, bluetooth is enabled and active

                        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            authorized = false;
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        } else {
                            // Toast.makeText(getContext(), "Location permissions already granted", Toast.LENGTH_SHORT).show();

                            deviceScanActivity.scanLeDevice(true);

                            getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                            Log.d(TAG, "le scan started");
                            authorized = true;
                        }
                    }
                }

                if (authorized) {
                    new WeightSelect(CihazOlcumFragment.this, deviceScanActivity).show(getChildFragmentManager(), "Select Weight");

                }
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

    public void CalculateFromBluetoothData(Float emp) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String current_date = formatter.format(date);

        int yil = 2019;

        double boy = Double.parseDouble(height);
        double kilo = Double.parseDouble(weight);
        int yas = yil - Integer.parseInt(birthday.substring(6));

        Log.d(TAG, yas + "**" + -yas);


        if (gender.equals("Erkek")) {

            double fatfreeMass = formulaDataErkek.getK() + formulaDataErkek.getX() * (boy * boy) / (emp + 100 * formulaDataErkek.getY()) +
                    boy * formulaDataErkek.getL() + formulaDataErkek.getZ() * (kilo + formulaDataErkek.getM()) +
                    formulaDataErkek.getT() * (-yas + formulaDataErkek.getN()) + 1 * formulaDataErkek.getO();

            double suKutlesi = fatfreeMass * 0.73;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatfreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatfreeMass;
            double yagYuzdesi = (yagKutlesi * 100) / kilo;

            double bmi = kilo / (boy * boy / 10000);
            double bazal = 88.362 + (4.799 * boy) + 13.397 * kilo - 5.677 * yas;

            Log.d("SONN", yagYuzdesi + "****" + emp);

            //creating a olcumlerimData object and getting data from edittext views
            OlcumlerimData olcumlerimData = new OlcumlerimData(String.valueOf(kilo),
                    String.valueOf(bmi), String.valueOf(yagYuzdesi), String.valueOf(suYuzdesi),
                    String.valueOf(kasYuzdesi), String.valueOf(bazal), "0", String.valueOf(emp));
            //pushing olcumlerimData object to FirebaseDatabase
            mOlcumlerimDatabaseReference.child(current_date).setValue(olcumlerimData);


        } else {

            double fatfreeMass = formulaDataKadin.getK() + formulaDataKadin.getX() * (boy * boy) / (emp + 100 * formulaDataKadin.getY()) +
                    boy * formulaDataKadin.getL() + formulaDataKadin.getZ() * (kilo + formulaDataKadin.getM()) +
                    formulaDataKadin.getT() * (-yas + formulaDataKadin.getN()) + 0 * formulaDataKadin.getO();

            double suKutlesi = fatfreeMass * 0.73;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatfreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatfreeMass;
            double yagYuzdesi = (yagKutlesi * 100) / kilo;

            double bmi = kilo / (boy * boy / 10000);
            double bazal = 88.362 + (4.799 * boy) + 13.397 * kilo - 5.677 * yas;

            Log.d("SONN", yagYuzdesi + "****" + emp);

            //creating a olcumlerimData object and getting data from edittext views
            OlcumlerimData olcumlerimData = new OlcumlerimData(String.valueOf(kilo),
                    String.valueOf(bmi), String.valueOf(yagYuzdesi), String.valueOf(suYuzdesi),
                    String.valueOf(kasYuzdesi), String.valueOf(bazal), "0", String.valueOf(emp));
            //pushing olcumlerimData object to FirebaseDatabase
            mOlcumlerimDatabaseReference.child(current_date).setValue(olcumlerimData);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
