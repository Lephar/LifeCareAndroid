package fit.lifecare.lifecare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.lifecare.lifecare.Dialogs.ChangePasswordDialog;
import fit.lifecare.lifecare.Dialogs.KisiselBirthdaySelect;

public class AyarlarFragment extends Fragment {

    private TextView password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        //inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_ayarlar, container, false);

        password = view.findViewById(R.id.password_field);

        initializeViewListeners();

        return view;
    }

    private void initializeViewListeners() {

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePasswordDialog().show(getChildFragmentManager(), "Change Pass");
            }
        });
    }
}
