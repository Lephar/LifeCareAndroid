package fit.lifecare.lifecare.Dialogs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import fit.lifecare.lifecare.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LanguageSelectDialog extends DialogFragment {

    //Layout views
    private ImageView close_button;
    private TextView turkish;
    private TextView english;
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_select_language, container, false);
    
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //initialize layout views
        close_button = view.findViewById(R.id.close_button);
        turkish= view.findViewById(R.id.text_tr);
        english = view.findViewById(R.id.text_en);
    
    
        //initialize shared preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        
        //initialize click listeners
        initializeButtonListeners();

        return view;
    }

    private void initializeButtonListeners() {

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        turkish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    
                setAppLocale("tr");
    
                editor.putString("lang","tr");
                editor.commit();
                
                getActivity().finish();
                startActivity(  getActivity().getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) );
                
                dismiss();
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    
                setAppLocale("en");
    
                editor.putString("lang","en");
                editor.commit();
    
                getActivity().finish();
                startActivity( getActivity().getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) );
    
                dismiss();
            }
        });
    }
    
    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            conf.locale = new Locale(localeCode.toLowerCase());
        }
        
        res.updateConfiguration(conf,dm);
        
    }
}
