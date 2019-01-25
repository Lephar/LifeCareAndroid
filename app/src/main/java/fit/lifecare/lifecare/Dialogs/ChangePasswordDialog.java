package fit.lifecare.lifecare.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fit.lifecare.lifecare.LoginActivity;
import fit.lifecare.lifecare.R;


public class ChangePasswordDialog extends DialogFragment {

    private String TAG = "change_passi";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Layout Views
    private EditText pass0;
    private EditText pass1;
    private EditText pass2;
    private ImageView tamam_button;
    private ImageView close_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_change_pass, container, false);

        //initialize layout views
        pass0 = view.findViewById(R.id.pass0);
        pass1 = view.findViewById(R.id.pass1);
        pass2 = view.findViewById(R.id.pass2);
        tamam_button = view.findViewById(R.id.tamam_button);
        close_button = view.findViewById(R.id.close_button);

        initializeViewListeners();


        return view;
    }

    private void initializeViewListeners() {

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tamam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForPassChange();
            }
        });

    }

    private void checkForPassChange() {
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        if (!validateForm()) {
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), pass0.getText().toString());

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (pass1.getText().toString().equals(pass2.getText().toString())) {
                            if (task.isSuccessful()) {
                                user.updatePassword(pass1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Şifre Değiştirildi", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        } else {
                                            Log.d(TAG, "Hata");
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Hatalı Şifre", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Yeni Şifreler Eşleşmedi", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String password0 = pass0.getText().toString();
        if (TextUtils.isEmpty(password0)) {
            Toast.makeText(getContext(), "Lütfen e-mail adresinizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            pass0.setError(null);
        }

        String password1 = pass1.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(getContext(), "Lütfen şifrenizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            pass1.setError(null);
        }

        String password2 = pass2.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            Toast.makeText(getContext(), "Lütfen şifrenizi giriniz.",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            pass2.setError(null);
        }

        return valid;
    }
}
