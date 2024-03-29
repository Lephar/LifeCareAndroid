package fit.lifecare.lifecare;

import android.Manifest;
import android.app.Activity;
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
    private boolean authorized = false;
    private DeviceScanActivity deviceScanActivity;
    private ViewPager viewPager;
    private Button start_button;
    private boolean registered = false;

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
    private float z0, z1, r0, r1, i0, i1;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (DeviceScanActivity.ACTION_GATT_CONNECTED.equals(action)) {
                Toast.makeText(getContext(), "Bluetooth connected with Lifecare", Toast.LENGTH_SHORT).show();
            } else if (DeviceScanActivity.ACTION_GATT_DISCONNECTED.equals(action)) {
                Toast.makeText(getContext(), "Bluetooth disconnected with Lifecare", Toast.LENGTH_SHORT).show();
                //} else if (DeviceScanActivity.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //    Toast.makeText(getContext(), "Bluetooth services discovered", Toast.LENGTH_SHORT).show();
            } else if (DeviceScanActivity.ACTION_DATA_AVAILABLE.equals(action)) {

                String readed_value = intent.getStringExtra(DeviceScanActivity.EXTRA_DATA);

                if (readed_value.equals("FIN")) {
                    deviceScanActivity.writeToDevice("Z0");
                } else if (readed_value.startsWith("Z0")) {
                    z0 = Float.parseFloat(readed_value.substring(3));
                    deviceScanActivity.writeToDevice("Z1");
                } else if (readed_value.startsWith("Z1")) {
                    z1 = Float.parseFloat(readed_value.substring(3));
                    deviceScanActivity.writeToDevice("R0");
                } else if (readed_value.startsWith("R0")) {
                    r0 = Float.parseFloat(readed_value.substring(3));
                    deviceScanActivity.writeToDevice("R1");
                } else if (readed_value.startsWith("R1")) {
                    r1 = Float.parseFloat(readed_value.substring(3));
                    deviceScanActivity.writeToDevice("I0");
                } else if (readed_value.startsWith("I0")) {
                    i0 = Float.parseFloat(readed_value.substring(3));
                    deviceScanActivity.writeToDevice("I1");
                } else if (readed_value.startsWith("I1")) {
                    i1 = Float.parseFloat(readed_value.substring(3));
                    Toast.makeText(getContext(), "Ölçüm bitti ", Toast.LENGTH_LONG).show();
                    getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.GONE);
                    CalculateFromBluetoothData(z1);
                    if (deviceScanActivity != null) {
                        deviceScanActivity.setStartClicked(false);
                        deviceScanActivity.disconnectDevice();
                        deviceScanActivity = null;
                    }
                    getActivity().finish();
                }

                /*Float emp = Float.parseFloat(readed_value.substring(5));

                if (emp < 10000) {
                    Toast.makeText(getContext(), "Ölçüm bitti ", Toast.LENGTH_LONG).show();
                    getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.GONE);

                    // calculate with formula and put it to firebase database
                    CalculateFromBluetoothData(emp);
                    if(deviceScanActivity != null)
                    {
                        deviceScanActivity.disconnectDevice();
                        deviceScanActivity = null;
                    }
                    getActivity().finish();

                } else {
                    Toast.makeText(getContext(), "Hatalı ölçüm tekrar ölçünüz ", Toast.LENGTH_LONG).show();
                    getView().findViewById(R.id.olcum_progress_bar).setVisibility(View.GONE);
                }*/
                //getActivity().finish();
            }
        }
    };
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
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

    private IntentFilter makeGattUpdateIntentFilter() {
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
                            registered = true;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity activity = getActivity();
        if (activity != null && registered) {
            activity.unregisterReceiver(mGattUpdateReceiver);
            registered = false;
        }
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

        double resistance = 0.6795 * 3.7822 * emp;
        double reactance = 0.6795 * 0.4052 * emp;
        //double resistance = 0.6941 * 3.7822 * emp;
        //double reactance = 0.6941 * 0.4052 * emp;

        double bazal = 88.362 + (4.799 * boy) + 13.397 * kilo - 5.677 * yas;

        if (gender.equals("Erkek")) {

            //double fatFreeMass = 0.7344 + 0.00062709 * boy * boy + 0.4637 * kilo + 0.0996 * yas;

            double fatFreeMass = 0.125 + 0.518 * boy * boy / resistance + 0.231 * kilo + 0.130 * reactance;

            //double fatFreeMass = -25.3 + 1.84 * (boy * boy) / (emp * 7.8) + 0.142 * kilo + 0.05 * yas + emp * 7.8 * 0.0169;
            //double fatFreeMass = formulaDataErkek.getK() + formulaDataErkek.getX() * (boy * boy) / ((emp * 7.8) + 100 * formulaDataErkek.getY()) +
            //        boy * formulaDataErkek.getL() + formulaDataErkek.getZ() * (kilo + formulaDataErkek.getM()) +
            //        formulaDataErkek.getT() * (-yas + formulaDataErkek.getN()) + 1 * formulaDataErkek.getO();

            //double suKutlesi = fatFreeMass * 0.73;
            double suKutlesi = 1.2 + 0.45 * boy * boy / resistance + 0.18 * kilo;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatFreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatFreeMass;
            double yagYuzdesi = (yagKutlesi * 100) / (kilo * 1.156);

            double bmi = kilo / (boy * boy / 10000);

            Log.d("SONN", yagYuzdesi + "****" + emp);

            //creating a olcumlerimData object and getting data from edittext views
            OlcumlerimData olcumlerimData = new OlcumlerimData(String.valueOf(kilo),
                    String.valueOf(bmi), String.valueOf(yagYuzdesi), String.valueOf(suYuzdesi),
                    String.valueOf(kasYuzdesi), String.valueOf(bazal), "0", String.valueOf(emp));
            //pushing olcumlerimData object to FirebaseDatabase
            mOlcumlerimDatabaseReference.child(current_date).setValue(olcumlerimData);


        } else {

            //double fatFreeMass = 0.00069152 * boy * boy + 0.4327 * kilo - 0.0604 * yas - 0.2195;

            double fatFreeMass = 0.518 * boy * boy / resistance + 0.231 * kilo + 0.130 * reactance - 4.104;

            //double fatFreeMass = formulaDataKadin.getK() + formulaDataKadin.getX() * (boy * boy) / ((emp / 6.02 - 60) * 7.93 + 100 * formulaDataKadin.getY()) +
            //        boy * formulaDataKadin.getL() + (-0.034 * formulaDataKadin.getX()) * (kilo + formulaDataKadin.getM()) +
            //        formulaDataKadin.getT() * (-yas + formulaDataKadin.getN()) + 0 * formulaDataKadin.getO();

            //double suKutlesi = fatFreeMass * 0.73;
            double suKutlesi = 3.75 + 0.45 * boy * boy / resistance + 0.11 * kilo;
            double suYuzdesi = (suKutlesi * 100) / kilo;

            double kasKutlesi = fatFreeMass - suKutlesi;
            double kasYuzdesi = (kasKutlesi * 100) / kilo;

            double yagKutlesi = kilo - fatFreeMass;
            double yagYuzdesi = (yagKutlesi * 100) / kilo;

            double bmi = kilo / (boy * boy / 10000);

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
