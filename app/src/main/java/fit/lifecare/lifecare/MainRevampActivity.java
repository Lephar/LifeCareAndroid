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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class MainRevampActivity extends AppCompatActivity
        implements MainTab.OnFragmentInteractionListener {

    private static final int RC_PHOTO_PICKER = 2;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView bottom_tab;
    private TextView current_tab;
    private TextView user_name;
    private ImageView profile_picture;
    private DatabaseReference mUserPersonalInfoDatabaseReference;
    private StorageReference mStorageReference;

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_revamp);

        bottom_tab = findViewById(R.id.bottomNavigationView);
        bottom_tab.setSelectedItemId(R.id.navigation_olcumlerim);
        current_tab = findViewById(R.id.current_tab);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_layout, new MainTab());
        fragmentTransaction.commit();

        bottom_tab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == bottom_tab.getSelectedItemId())
                    return false;

                else if (menuItem.getItemId() == R.id.navigation_besinlerim) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout, new ProgramFragment());
                    fragmentTransaction.commit();
                    current_tab.setText("Besin Programım");
                } else if (menuItem.getItemId() == R.id.navigation_olcumlerim) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout, new MainTab());
                    fragmentTransaction.commit();
                    current_tab.setText("Ölçümlerim");
                } else if (menuItem.getItemId() == R.id.navigation_sohbetler) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout, new DanismanimFragment());
                    fragmentTransaction.commit();
                    current_tab.setText("Sohbetlerim");
                }

                return true;
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

        /*profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the app
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainRevampActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/

        ImageButton settings = findViewById(R.id.settings_button);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout, new AyarlarFragment());
                fragmentTransaction.commit();
                current_tab.setText("Ayarlar");
            }
        });

        ImageButton profile = findViewById(R.id.imageButton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout, new ProfilimMainFragment());
                fragmentTransaction.commit();
                current_tab.setText("Profil");
            }
        });

        initializeFirebaseListenersForPhoto();
        initializeFirebaseListenersForName();
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
                user_name.setText(name);
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
