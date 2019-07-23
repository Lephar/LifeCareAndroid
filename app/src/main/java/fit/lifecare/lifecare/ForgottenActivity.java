package fit.lifecare.lifecare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        TextView signupButton = findViewById(R.id.signup_button);
        Button resetButton = findViewById(R.id.reset_button);
        final EditText emailField = findViewById(R.id.email_field);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    emailField.setError(getString(R.string.enter_email));
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgottenActivity.this, getString(R.string.email_sent),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgottenActivity.this, getString(R.string.no_such_mail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgottenActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
