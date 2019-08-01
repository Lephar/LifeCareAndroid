package fit.lifecare.lifecare;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainRevampActivity extends AppCompatActivity implements MainTab.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_revamp);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
