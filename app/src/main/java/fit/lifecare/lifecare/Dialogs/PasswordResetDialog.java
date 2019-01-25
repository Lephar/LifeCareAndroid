package fit.lifecare.lifecare.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import fit.lifecare.lifecare.R;

public class PasswordResetDialog extends DialogFragment {

    //Layout Views
    private ImageView closeButton;
    private ImageView tamamButton;
    private EditText mEditText;


    //firebase instance variables
    private FirebaseAuth mAuth;

    public PasswordResetDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dialog_reset_password, container, false);

        //initialize buttons
        closeButton = view.findViewById(R.id.dialog_close_button);
        tamamButton = view.findViewById(R.id.tamam_button);
        mEditText = view.findViewById(R.id.password_reset);

        //initalize Firebase components
        mAuth = FirebaseAuth.getInstance();

        initializeViewListeners();

        return view;
    }

    private void initializeViewListeners() {

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tamamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEditText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEditText.setError("Lütfen mail adresinizi giriniz.");
                } else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "E-mail gönderildi.",
                                        Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                Toast.makeText(getContext(), "Girdiğiniz mail adresi kayıtlı bulunmamaktadır.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
