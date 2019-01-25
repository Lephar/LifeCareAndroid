package fit.lifecare.lifecare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fit.lifecare.lifecare.DatabaseClasses.PersonalInfoData;
import fit.lifecare.lifecare.Dialogs.PasswordResetDialog;

public class LoginActivity extends AppCompatActivity {

    //state holder, 0 means app_user_button clicked,
    //1 means dietitian_button clicked, 2 means sport_coach_button clicked
    private int state = 0;

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private String user_name;
    private String user_eMail;

    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    //Layout views
    private ImageView app_user_button;
    private TextView app_user_text;
    private ImageView dietitian_button;
    private TextView dietitian_text;
    private ImageView sport_coach_button;
    private TextView sport_coach_text;
    private ImageView back_button;
    private ImageView login_button;
    private ImageView facebook_login;
    private LoginButton facebook_real_login_button;
    private ImageView google_login;
    private TextView email_field;
    private TextView password_field;
    private TextView forgotten_password;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mUsersPersonalInfoReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize views
        app_user_button = findViewById(R.id.app_user_button);
        app_user_text = findViewById(R.id.app_user_text);
        dietitian_button = findViewById(R.id.dietitian_button);
        dietitian_button.setClickable(false);
        dietitian_text = findViewById(R.id.dietitian_text);
        sport_coach_button = findViewById(R.id.sport_coach_button);
        sport_coach_button.setClickable(false);
        sport_coach_text = findViewById(R.id.sport_coach_text);
        back_button = findViewById(R.id.back_button);
        login_button = findViewById(R.id.login_button);
        facebook_login = findViewById(R.id.facebook_login);
        facebook_real_login_button = findViewById(R.id.facebook_real_login_button);
        google_login = findViewById(R.id.google_login);
        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        forgotten_password = findViewById(R.id.forgotten_password);

        //initalize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers");

        //handle the clicks, switch the state accordingly
        stateHandler();

        //initialize button listeners
        initializeViewListeners();
        initializeFacebookLogin();
        initializeGoogleLogin();

    }


    private void initializeViewListeners() {

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        forgotten_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PasswordResetDialog().show(getSupportFragmentManager(), "Code Adder");
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn(email_field.getText().toString(), password_field.getText().toString());
            }
        });

        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook_real_login_button.performClick();
            }
        });

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void initializeGoogleLogin() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initializeFacebookLogin() {
        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        facebook_real_login_button.setReadPermissions("email", "public_profile");
        facebook_real_login_button.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.w(TAG, "Google sign in success");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                // updateUI(null);
                // [END_EXCLUDE]
            }
        } else {
            // Facebook login activity result
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_eMail = user.getEmail();
                            user_name = user.getDisplayName();
                            mUsersPersonalInfoReference = mUsersDatabaseReference.child(user.getUid()).child("PersonalInfo");
                            checkGoogleSignedInBefore();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user_eMail = user.getEmail();
                            user_name = user.getDisplayName();
                            mUsersPersonalInfoReference = mUsersDatabaseReference.child(user.getUid()).child("PersonalInfo");
                            checkFacebookSignedInBefore();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    // [START sign_in_with_email]
    private void LogIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Kullanıcı adı yada şifre hatalı.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END sign_in_with_email]

    private boolean validateForm() {
        boolean valid = true;

        String email = email_field.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Lütfen e-mail adresinizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            email_field.setError(null);
        }

        String password = password_field.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Lütfen şifrenizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            password_field.setError(null);
        }

        return valid;
    }

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

    private void stateHandler() {

        app_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 1) {

                    app_user_button.setBackgroundResource(R.drawable.buttonclicked);
                    app_user_text.setTextColor(Color.WHITE);
                    dietitian_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    dietitian_text.setTextColor(Color.parseColor("#2A307F"));
                    sport_coach_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    sport_coach_text.setTextColor(Color.parseColor("#2A307F"));
                    state = 0;
                } else if (state == 2) {

                    app_user_button.setBackgroundResource(R.drawable.buttonclicked);
                    app_user_text.setTextColor(Color.WHITE);
                    dietitian_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    dietitian_text.setTextColor(Color.parseColor("#2A307F"));
                    sport_coach_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    sport_coach_text.setTextColor(Color.parseColor("#2A307F"));
                    state = 0;
                }
            }
        });
/*
        dietitian_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {

                    app_user_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    app_user_text.setTextColor(Color.parseColor("#2A307F"));
                    dietitian_button.setBackgroundResource(R.drawable.buttonclicked);
                    dietitian_text.setTextColor(Color.WHITE);
                    sport_coach_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    sport_coach_text.setTextColor(Color.parseColor("#2A307F"));
                    state = 1;
                } else if (state == 2) {

                    app_user_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    app_user_text.setTextColor(Color.parseColor("#2A307F"));
                    dietitian_button.setBackgroundResource(R.drawable.buttonclicked);
                    dietitian_text.setTextColor(Color.WHITE);
                    sport_coach_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    sport_coach_text.setTextColor(Color.parseColor("#2A307F"));
                    state = 1;
                }
            }
        });

        sport_coach_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state == 0) {

                    app_user_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    app_user_text.setTextColor(Color.parseColor("#2A307F"));
                    dietitian_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    dietitian_text.setTextColor(Color.parseColor("#2A307F"));
                    sport_coach_button.setBackgroundResource(R.drawable.buttonclicked);
                    sport_coach_text.setTextColor(Color.WHITE);
                    state = 2;
                } else if (state == 1) {

                    app_user_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    app_user_text.setTextColor(Color.parseColor("#2A307F"));
                    dietitian_button.setBackgroundResource(R.drawable.buttonnonclicked);
                    dietitian_text.setTextColor(Color.parseColor("#2A307F"));
                    sport_coach_button.setBackgroundResource(R.drawable.buttonclicked);
                    sport_coach_text.setTextColor(Color.WHITE);
                    state = 2;
                }
            }
        });*/
    }

    private void checkGoogleSignedInBefore() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String gender = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "hayda2 ");
                    if (gender == null) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        PersonalInfoData personalInfoData = new PersonalInfoData(user_name, user_eMail);
                        mUsersDatabaseReference.child(user.getUid()).child("PersonalInfo").setValue(personalInfoData);
                        Intent intent = new Intent(LoginActivity.this, WelcomeAfterSignup.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUsersPersonalInfoReference.child("gender").addListenerForSingleValueEvent(mValueEventListener);
        }
    }

    private void checkFacebookSignedInBefore() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String gender = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "hayda2 ");
                    if (gender == null) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        PersonalInfoData personalInfoData = new PersonalInfoData(user_name, user_eMail);
                        mUsersDatabaseReference.child(user.getUid()).child("PersonalInfo").setValue(personalInfoData);
                        Intent intent = new Intent(LoginActivity.this, WelcomeAfterSignup.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUsersPersonalInfoReference.child("gender").addListenerForSingleValueEvent(mValueEventListener);
        }
    }
}
