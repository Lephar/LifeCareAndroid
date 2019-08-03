package fit.lifecare.lifecare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import fit.lifecare.lifecare.Dialogs.ChangePasswordDialog;
import fit.lifecare.lifecare.Dialogs.LanguageSelectDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AyarlarFragment extends Fragment {
    
    // Layout Views
    private TextView language;
    private TextView password;
    private TextView hesap;
    private static final int RC_PHOTO_PICKER = 2;
    private Switch mSwitch;
    private Switch mSwitch2;
    private TextView hesap2;
    private CircleImageView picChange;
    
    // Firebase instance variables
    private FirebaseUser user;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView about;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        
        // inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_ayarlar, container, false);
        
        // initialize Layout Views
        language = view.findViewById(R.id.lang);
        password = view.findViewById(R.id.password_field);
        hesap = view.findViewById(R.id.hesap);
        hesap2 = view.findViewById(R.id.hesap2);
        mSwitch = view.findViewById(R.id.switch_view);
        mSwitch2 = view.findViewById(R.id.switch_view2);
        picChange = view.findViewById(R.id.profile_pic);
        about = view.findViewById(R.id.about);
        
        // initialize firebase components
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("AppUsers").child(userID);
        
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        
        initializeViewListeners();
        
        return view;
    }
    
    private void initializeViewListeners() {
    
        boolean showNotification = preferences.getBoolean("SHOW_NOTIFICATION", true);
        boolean showNotificationWater = preferences.getBoolean("SHOW_NOTIFICATION_WATER", true);
        
        mSwitch.setChecked(showNotification);
        mSwitch2.setChecked(showNotificationWater);
        
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                
                if (mSwitch.isChecked()) {
                    editor.putBoolean("SHOW_NOTIFICATION", true);
                    editor.commit();
                } else {
                    editor.putBoolean("SHOW_NOTIFICATION", false);
                    editor.commit();
                }
                
            }
        });
    
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            
                if (mSwitch2.isChecked()) {
                    editor.putBoolean("SHOW_NOTIFICATION_WATER", true);
                    editor.commit();
                } else {
                    editor.putBoolean("SHOW_NOTIFICATION_WATER", false);
                    editor.commit();
                }
            
            }
        });
        
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LanguageSelectDialog().show(getChildFragmentManager(), "Select Language Dialog");
        
            }
        });
        
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePasswordDialog().show(getChildFragmentManager(), "Change Pass");
            }
        });

        hesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setMessage(getString(R.string.sure_delete_account))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    FirebaseAuth.getInstance().signOut();
                                                    LoginManager.getInstance().logOut();
                                                    mDatabaseReference.removeValue();
                                                }
                                            }
                                        });

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();

            }
        });

        hesap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the app
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        picChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new HakkindaFragment()).commit();
            }
        });
    }
}
