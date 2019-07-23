package fit.lifecare.lifecare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SelectPhotoActivity extends AppCompatActivity {

    //Layout views
    private ImageView back_button;
    private ImageView next_button;
    private ImageView photo_select;
    private ProgressBar progressBar;

    private SharedPreferences preferences;

    private static final int RC_PHOTO_PICKER = 2;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    private DatabaseReference mUserPersonalInfoDatabaseReference;

    private String gender;
    private String birthday_date;
    private String height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        //initialize views
        back_button = findViewById(R.id.back_button);
        next_button = findViewById(R.id.next_button);
        photo_select = findViewById(R.id.photo_select);
        progressBar = findViewById(R.id.progress_bar);

        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //get saved data from shared preferences
        gender = preferences.getString("gender", "N/A");
        birthday_date = preferences.getString("birthday", "N/A");
        height = preferences.getString("height", "N/A");

        if (gender.equals("Erkek")) {
            photo_select.setImageResource(R.drawable.boyphotoselect);
        }

        //initalize Firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("appUsers").child(currentUserId)
                .child("photos");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");

        //initialize button listeners
        initializeViewListeners();
    }


    private void initializeViewListeners() {

        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                //show progressbar and set all thngs not touchable
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //put saved data to firebase database
                mUserPersonalInfoDatabaseReference.child("gender").setValue(gender);
                mUserPersonalInfoDatabaseReference.child("birth_date").setValue(birthday_date);
                mUserPersonalInfoDatabaseReference.child("height").setValue(height);

                Intent intent = new Intent(SelectPhotoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //show cardview and make things touchable again
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();

            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().dontAnimate().dontTransform())
                    .into(photo_select);

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
                            Log.e("hasanurl2", downloadUri.toString());
                            mUserPersonalInfoDatabaseReference.child("photo_url").setValue(downloadUri.toString());
                        }
                    }
                });

            progressBar.setVisibility(View.GONE);
        }
    }
}
