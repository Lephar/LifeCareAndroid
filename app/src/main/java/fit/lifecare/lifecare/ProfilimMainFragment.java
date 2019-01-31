package fit.lifecare.lifecare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfilimMainFragment extends Fragment {

    //Layout Views
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_profilim, container, false);

        //initialize viewPager
        viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        //initialize tablayout
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new ProfilimTab1(),getString(R.string.personal));

        adapter.addFragment(new ProfilimTab2(),getString(R.string.health));

        adapter.addFragment(new ProfilimTab3(),getString(R.string.meal));

        adapter.addFragment(new ProfilimTab4(),getString(R.string.blood));

        adapter.addFragment(new ProfilimTab5(),getString(R.string.measure));

        viewPager.setAdapter(adapter);
    }
}
