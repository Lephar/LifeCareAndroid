package fit.lifecare.lifecare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Animation slide = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.welcome_down);
        Animation fade_in = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in);
        Animation fade_in_later = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.fade_in_later);

        TextView login_button = findViewById(R.id.login_button);
        TextView welcome_text = findViewById(R.id.textView);
        TextView login_text = findViewById(R.id.login_text);
        ImageView logo = findViewById(R.id.small_logo);

        Button signup_button = findViewById(R.id.signup_button);
        ImageView welcome = findViewById(R.id.imageView);

        welcome.startAnimation(slide);
        signup_button.startAnimation(fade_in_later);
        login_button.startAnimation(fade_in_later);
        welcome_text.startAnimation(fade_in);
        login_text.startAnimation(fade_in_later);
        logo.startAnimation(fade_in);

        prepareLocale();

        if (checkConnection()) {
            if (checkUser()) {
                Intent intent = new Intent(WelcomeActivity.this, MainRevampActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

                signup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void prepareLocale() {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        conf.setLocale(new Locale(prefs.getString("lang", "en").toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }

        return false;
    }

    private boolean checkUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
