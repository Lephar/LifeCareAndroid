package fit.lifecare.lifecare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fit.lifecare.lifecare.DatabaseClasses.PersonalInfoData;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //Layout views
    private ImageView back_button;
    private ImageView signup_button;
    private ImageView facebook_signup;
    private LoginButton facebook_real_login_button;
    private ImageView google_signup;
    private TextView name_field;
    private TextView email_field;
    private TextView password_field;
    private CheckBox checkBox;

    private static final int RC_SIGN_IN = 9001;
    private String user_eMail;
    private String user_name;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    //firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersPersonalInfoReference;
    private DatabaseReference mUsersDatabaseReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize views
        back_button = findViewById(R.id.back_button);
        signup_button = findViewById(R.id.signup_button);
        facebook_signup = findViewById(R.id.facebook_signup);
        facebook_real_login_button = findViewById(R.id.login_button);
        google_signup = findViewById(R.id.google_signup);
        name_field = findViewById(R.id.name_field);
        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        checkBox = findViewById(R.id.checkBox);

        //initalize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers");

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

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(email_field.getText().toString(), password_field.getText().toString());
            }
        });

        facebook_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Lütfen gizlilik sözleşmelerini onaylayın", Toast.LENGTH_SHORT).show();
                } else {
                    facebook_real_login_button.performClick();
                }

            }
        });

        google_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Lütfen gizlilik sözleşmelerini onaylayın", Toast.LENGTH_SHORT).show();
                } else {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
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
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
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
                        Intent intent = new Intent(SignupActivity.this, WelcomeAfterSignup.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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
                        Intent intent = new Intent(SignupActivity.this, WelcomeAfterSignup.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
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



    // [START create_user_with_email]
    private void createAccount(String email, final String password) {
        Log.d(TAG, "createAccount:" + email);

        email = email.replace(" ", "");

        //if user try to sign with empty field do not allow them
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            user_name = name_field.getText().toString();
                            user_eMail = email_field.getText().toString();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent");
                                    }
                                }

                            });
                            PersonalInfoData personalInfoData = new PersonalInfoData(user_name, user_eMail);
                            mUsersDatabaseReference.child(user.getUid()).child("PersonalInfo").setValue(personalInfoData);
                            Intent intent = new Intent(SignupActivity.this, WelcomeAfterSignup.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Bu mail adresi kullanılıyor.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Lütfen geçerli bir mail adresi giriniz.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END create_user_with_email]

    //validating signin form to prevent user enter empty field
    private boolean validateForm() {
        boolean valid = true;

        if (!checkBox.isChecked()) {
            Toast.makeText(SignupActivity.this, "Lütfen gizlilik sözleşmelerini onaylayın", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String name = name_field.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SignupActivity.this, "Lütfen isminizi adresinizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            name_field.setError(null);
        }

        String email = email_field.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignupActivity.this, "Lütfen e-mail adresinizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            email_field.setError(null);
        }

        String password = password_field.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignupActivity.this, "Lütfen en az 6 haneli şifre giriniz",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (password.length() < 6) {
            Toast.makeText(SignupActivity.this, "Lütfen en az 6 haneli şifre giriniz", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            password_field.setError(null);
        }

        return valid;
    }

    //after touching somewhere different than edittext, hide the softkeyboard
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
}
