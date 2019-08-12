package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainRevampActivity extends AppCompatActivity implements MainTab.OnFragmentInteractionListener {

    private static final int RC_PHOTO_PICKER = 2;
    private FragmentManager fragmentManager;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private FragmentTransaction fragmentTransaction;
    private FrameLayout fragmentLayout;
    private BottomNavigationView bottom_tab;
    private TextView current_tab_text;
    private TextView user_name;
    private ImageView profile_picture;
    private DatabaseReference mUserPersonalInfoDatabaseReference;
    private StorageReference mStorageReference;
    private String[] tabTexts;
    private int prevTab = 1;

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_revamp);

        bottom_tab = findViewById(R.id.bottomNavigationView);
        bottom_tab.setSelectedItemId(R.id.navigation_olcumlerim);
        current_tab_text = findViewById(R.id.current_tab);
        viewPager = findViewById(R.id.view_pager_main);
        fragmentLayout = findViewById(R.id.fragment_layout);

        tabTexts = new String[3];
        tabTexts[0] = "Besin Programım";
        tabTexts[1] = "Ölçümlerim";
        tabTexts[2] = "Sohbetlerim";

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_layout, new MainTab());
        fragmentTransaction.commit();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProgramFragment(), "Besinlerim");
        adapter.addFragment(new MainTab(), "Ölçümlerim");
        adapter.addFragment(new DanismanimFragment(), "Sohbetler");

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        current_tab_text.setText(tabTexts[1]);
        bottom_tab.setSelectedItemId(R.id.navigation_olcumlerim);

        bottom_tab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (viewPager.getVisibility() == View.GONE) {
                    fragmentLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }

                if (menuItem.getItemId() == R.id.navigation_besinlerim) {
                    current_tab_text.setText(tabTexts[0]);
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.navigation_olcumlerim) {
                    current_tab_text.setText(tabTexts[1]);
                    viewPager.setCurrentItem(1);
                } else if (menuItem.getItemId() == R.id.navigation_sohbetler) {
                    current_tab_text.setText(tabTexts[2]);
                    viewPager.setCurrentItem(2);
                }

                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottom_tab.getMenu().getItem(prevTab).setChecked(false);
                bottom_tab.getMenu().getItem(position).setChecked(true);
                current_tab_text.setText(tabTexts[position]);
                prevTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //initialize shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //initialize Firebase components
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");

        mUserPersonalInfoDatabaseReference.getParent().keepSynced(true);

        //firebase fcm token listener
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        mUserPersonalInfoDatabaseReference.child("fcm_token").setValue(token);
                    }
                });

        //firebase fcm token listener
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("appUsers").child(currentUserId)
                .child("photos");

        profile_picture = findViewById(R.id.profile_pic);
        user_name = findViewById(R.id.user_name);

        ImageButton settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout, new AyarlarFragment());
                fragmentTransaction.commit();
                current_tab_text.setText("Ayarlar");
            }
        });

        ImageButton profile = findViewById(R.id.imageButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout, new ProfilimMainFragment());
                fragmentTransaction.commit();
                current_tab_text.setText("Profil");
            }
        });

        initializeFirebaseListenersForPhoto();
        initializeFirebaseListenersForName();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getVisibility() == View.GONE) {
            fragmentLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            current_tab_text.setText(tabTexts[viewPager.getCurrentItem()]);
        } else if (bottom_tab.getSelectedItemId() != R.id.navigation_olcumlerim) {
            bottom_tab.setSelectedItemId(R.id.navigation_olcumlerim);
        } else
            super.onBackPressed();
    }

    private void initializeFirebaseListenersForPhoto() {
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                if (url != null) {
                    Glide.with(profile_picture.getContext()).load(url).into(profile_picture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserPersonalInfoDatabaseReference.child("photo_url").addListenerForSingleValueEvent(mValueEventListener);
    }

    private void initializeFirebaseListenersForName() {
        ValueEventListener mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                int index = name.lastIndexOf(' ');
                if (index == -1)
                    user_name.setText(name);
                else
                    user_name.setText(name.substring(0, index));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserPersonalInfoDatabaseReference.child("name").addListenerForSingleValueEvent(mValueEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().dontAnimate().dontTransform())
                    .into(profile_picture);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            final StorageReference photoReference = mStorageReference.child(selectedImageUri.getLastPathSegment());
            photoReference.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.e("hasanurl", downloadUri.toString());
                        mUserPersonalInfoDatabaseReference.child("photo_url").setValue(downloadUri.toString());
                    }
                }
            });

        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_CANCELED) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
