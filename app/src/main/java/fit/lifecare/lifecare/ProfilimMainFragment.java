package fit.lifecare.lifecare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class ProfilimMainFragment extends Fragment {

    //Layout Views
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the fragment layout
        return inflater.inflate(R.layout.fragment_profilim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize viewPager
        viewPager = getView().findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        //initialize tablayout
        tabLayout = getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ProfilimTab1(),getString(R.string.personal));
        adapter.addFragment(new ProfilimTab2(),getString(R.string.health));
        adapter.addFragment(new ProfilimTab3(),getString(R.string.meal));
        adapter.addFragment(new ProfilimTab4(),getString(R.string.blood));
        adapter.addFragment(new ProfilimTab5(), getString(R.string.measurement));

        viewPager.setAdapter(adapter);
    }
}
