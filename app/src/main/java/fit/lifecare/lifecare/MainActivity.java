package fit.lifecare.lifecare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import fit.lifecare.lifecare.Notifications.AlarmReceiver;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    //Layout views
    private DrawerLayout rootview;
    private Toolbar mToolbar_header;
    private Toolbar mToolbar_footer;
    private ImageView device_button;
    private ImageView home_button;
    private ImageView talk_button;
    private ImageView profile_picture;
    private ImageView header_profile_pic;
    private ImageView message_alert;
    private TextView user_name;
    
    private boolean backPressedToExitOnce = false;
    private static final String TAG = "Main Screen";
    private static final int RC_PHOTO_PICKER = 2;
    
    
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    
    
    private ArrayList<String> appUserChatIDs = new ArrayList<>();
    
    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserPersonalInfoDatabaseReference;
    private DatabaseReference chatListenerReference;
    private DatabaseReference appUserChatIdsReference;
    private ValueEventListener appUserChatIdsListener;
    private ValueEventListener mValueEventListener;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //initialize rootview
        rootview = findViewById(R.id.drawer_layout);
        
        // initialize sharedPreferences
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        
        //initialize toolbars
        mToolbar_header = findViewById(R.id.toolbar_header);
        mToolbar_footer = findViewById(R.id.toolbar_footer);
        
        //initialize footer buttons
        device_button = mToolbar_footer.findViewById(R.id.footer_lifecareicon);
        home_button = mToolbar_footer.findViewById(R.id.home_button);
        talk_button = mToolbar_footer.findViewById(R.id.footer_talkbutton);
        message_alert = mToolbar_footer.findViewById(R.id.message_alert);
        
        //initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserPersonalInfoDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo");
        
        mUserPersonalInfoDatabaseReference.getParent().keepSynced(true);
        
        chatListenerReference = mFirebaseDatabase.getReference().child("Chats");
        appUserChatIdsReference = mFirebaseDatabase.getReference().child("AppUsers")
                .child(currentUserId).child("PersonalInfo").child("ChatIDs");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("appUsers").child(currentUserId)
                .child("photos");
        
        //initialize navigation view and profile_picture
        NavigationView navigationView = findViewById(R.id.nav_view);
        profile_picture = navigationView.getHeaderView(0).findViewById(R.id.profile_pic);
        user_name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        navigationView.setNavigationItemSelectedListener(this);
        
        setSupportActionBar(mToolbar_header);
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar_header, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorGray100));
        toggle.syncState();
        
        Drawable drawable = getResources().getDrawable(R.drawable.header_gradient);
        mToolbar_header.setBackground(drawable);
        
        //set notification alarm
        notificationAlarms();
        
        //initialize listeners
        initializeFirebaseListenersForName();
        initializeFirebaseListenersForPhoto();
        initializeFirebaseListenerForAppUserChatIDs();
        initializeButtonClickListeners();
        //when softkeyboard is opened hide footer toolbar and when it is closed show it again
        hide_footer();
        
        //set the first fragment
        home_button.setImageResource(R.drawable.home_clicked);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OlcumlerimMainFragment()).commit();
    }
    
    //handle back button actions
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if drawer is open, close the drawer when back button clicked
        //else force user press back button second time for exiting app
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedToExitOnce) {
                super.onBackPressed();
            } else {
                this.backPressedToExitOnce = true;
                Toast.makeText(this, getString(R.string.press_again_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressedToExitOnce = false;
                    }
                }, 2000);
            }
        }
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        
        if (id == R.id.olcumlerim) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.olcumlerim));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OlcumlerimMainFragment()).commit();
        } else if (id == R.id.cihazim) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.cihazim));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CihazimFragment()).commit();
        } else if (id == R.id.danismanim) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.danismanlarim));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DanismanimFragment()).commit();
        } else if (id == R.id.program) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.beslenme_prog));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProgramFragment()).commit();
        } else if (id == R.id.profilim) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.profilim));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilimMainFragment()).commit();
        } else if (id == R.id.ayarlar) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.settings));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AyarlarFragment()).commit();
        } else if (id == R.id.hakkinda) {
            //set title according to selected item
            TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
            tv.setText(getString(R.string.about));
            //set footer images colors according to selected item
            device_button.setImageResource(R.drawable.device_non_clicked);
            home_button.setImageResource(R.drawable.home_non_clicked);
            talk_button.setImageResource(R.drawable.talk_non_clicked);
            //replace current fragment with new one
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HakkindaFragment()).commit();
        } else if (id == R.id.cikis) {
            //close the app
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    //handle profile picture select event result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().dontAnimate().dontTransform())
                    .into(profile_picture);
            Glide.with(this)
                    .load(selectedImageUri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().dontAnimate().dontTransform())
                    .into(header_profile_pic);
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
                    } else {
                        Log.d(TAG,":Fotoğraf yüklenemedi ");
                    }
                }
            });
            
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_CANCELED) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    
    //hide the keyboard if user touch somewhere else than edittext
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    
    private void initializeButtonClickListeners() {
        //header profile pic listener
        header_profile_pic = mToolbar_header.findViewById(R.id.header_profile_pic);
        header_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set title according to selected item
                TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
                tv.setText(getString(R.string.profilim));
                //set footer images colors according to selected item
                device_button.setImageResource(R.drawable.device_non_clicked);
                home_button.setImageResource(R.drawable.home_non_clicked);
                talk_button.setImageResource(R.drawable.talk_non_clicked);
                //replace current fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilimMainFragment()).commit();
            }
        });
        
        //footer cihazim button listener
        device_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set title according to selected item
                TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
                tv.setText(getString(R.string.cihazim));
                //set footer images colors according to selected item
                device_button.setImageResource(R.drawable.device_clicked);
                home_button.setImageResource(R.drawable.home_non_clicked);
                talk_button.setImageResource(R.drawable.talk_non_clicked);
                //replace current fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CihazimFragment()).commit();
            }
        });
        //footer home button listener
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set title according to selected item
                TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
                tv.setText(getString(R.string.olcumlerim));
                //set footer images colors according to selected item
                device_button.setImageResource(R.drawable.device_non_clicked);
                home_button.setImageResource(R.drawable.home_clicked);
                talk_button.setImageResource(R.drawable.talk_non_clicked);
                //replace current fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OlcumlerimMainFragment()).commit();
            }
        });
        //footer diyetisyenim button listener
        talk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set title according to selected item
                TextView tv = (TextView) mToolbar_header.findViewById(R.id.header_title_id);
                tv.setText(getString(R.string.danismanlarim));
                //set footer images colors according to selected item
                device_button.setImageResource(R.drawable.device_non_clicked);
                home_button.setImageResource(R.drawable.home_non_clicked);
                talk_button.setImageResource(R.drawable.talk_clicked);
                //replace current fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DanismanimFragment()).commit();
            }
        });
        //profile_picture view listener
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }
    
    //listening firebase database to load profile picture
    private void initializeFirebaseListenersForPhoto() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                if (url != null) {
                    Glide.with(profile_picture.getContext()).load(url).into(profile_picture);
                    Glide.with(header_profile_pic.getContext()).load(url).into(header_profile_pic);
                }
            }
            
            @Override
            public void onCancelled(DatabaseError databaseError) {
            
            }
        };
        mUserPersonalInfoDatabaseReference.child("photo_url").addListenerForSingleValueEvent(mValueEventListener);
    }
    
    //listening firebase database to load profile picture
    private void initializeFirebaseListenersForName() {
        mValueEventListener = new ValueEventListener() {
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
    
    private void initializeFirebaseListenerForAppUserChatIDs() {
        
        appUserChatIdsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getValue() != null) {
                        appUserChatIDs.add(dataSnapshot1.getValue().toString());
                    }
                }
                initializeFirebaseListenerForChat();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        };
        appUserChatIdsReference.addValueEventListener(appUserChatIdsListener);
    }
    
    private void initializeFirebaseListenerForChat() {
        
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("hasee  ", dataSnapshot1.getValue().toString());
                    if (dataSnapshot1.getKey().equals("dietitian_id")) {
                        //TODO
                        //haci simdi app user her bir chatini dinleyecekki mesaj geldiğinde direk uyarılsın
                    }
                }
                
                
            }
            
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            
            }
            
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            
            }
            
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        };
        
        chatListenerReference.addChildEventListener(mChildEventListener);
    }
    
    //when softkeyboard is opened hide footer toolbar and when it is closed show it again
    private void hide_footer() {
        rootview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                
                Rect r = new Rect();
                rootview.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootview.getRootView().getHeight();
                
                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                
                Log.d(TAG, "keypadHeight = " + keypadHeight);
                
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    mToolbar_footer.setVisibility(View.GONE);
                } else {
                    // keyboard is closed
                    mToolbar_footer.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    private void notificationAlarms() {
        
        
        checkProfileRemainderNeed();
        
        notificationTypeSu();
        
        notificationTypeProfil();
        
    }
    
    private void notificationTypeSu() {
        Calendar calendar = Calendar.getInstance();
        Calendar timeNow = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(timeNow)) {
            calendar.add(Calendar.DATE, 1);
        }
        
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        Intent intent = new Intent(MainActivity.this, alarmReceiver.getClass());
        intent.putExtra("type", "su");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    
    private void notificationTypeProfil() {
        
        Calendar calendar = Calendar.getInstance();
        Calendar timeNow = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(timeNow)) {
            calendar.add(Calendar.DATE, 1);
        }
        
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        Intent intent = new Intent(MainActivity.this, alarmReceiver.getClass());
        intent.putExtra("type", "profil");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        
        
        boolean profileFilled1 = sharedPref.getBoolean("profileFilled1", false);
        boolean profileFilled2 = sharedPref.getBoolean("profileFilled2", false);
        
        Log.d("inteny", " :" + profileFilled1 + " " + profileFilled2);
        
        if (profileFilled1 && profileFilled2) {
            am.cancel(pendingIntent);
        }
        
    }
    
    
    private void checkProfileRemainderNeed() {
        
        mUserPersonalInfoDatabaseReference.getParent().child("ProfilimSaglik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("profilimsaglikvar", " " + dataSnapshot.exists());
                    editor.putBoolean("profileFilled1", true);
                    editor.commit();
                } else {
                    editor.putBoolean("profileFilled1", false);
                    editor.commit();
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
        
        mUserPersonalInfoDatabaseReference.getParent().child("ProfilimOgun").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("profilimogunvar", " " + dataSnapshot.exists());
                    editor.putBoolean("profileFilled2", true);
                    editor.commit();
                } else {
                    Log.d("profilimogunyok", " " + dataSnapshot.exists());
                    editor.putBoolean("profileFilled2", false);
                    editor.commit();
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
    }
}
