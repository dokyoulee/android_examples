package exam.sai.com.designpattern;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import exam.sai.com.designpattern.databinding.ActivityDesignPatternBinding;

public class DesignPatternActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDesignPatternBinding binding;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_design_pattern);

        SectionsPagerAdapter sectionsPagerAdapter;
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        sectionsPagerAdapter.addFragment(UserDataFragment.class.getName(), "Provider Binding");
        sectionsPagerAdapter.addFragment(UserDataFragment.class.getName(), "RESTful Binding");

        binding.container.setAdapter(sectionsPagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);

        setSupportActionBar(binding.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_design_pattern, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context mContext = null;
        private ArrayList<FragmentInfo> mFragments = new ArrayList<>();

        SectionsPagerAdapter(FragmentManager fm, Context ctx) {
            super(fm);
            mContext = ctx;
        }

        void addFragment(String clsName, String title) {
            mFragments.add(new FragmentInfo(clsName, title));
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("SectionsPagerAdapter", "getItem called(position:" + position + ")");
            Fragment frag;

            if (mFragments.size() < position && mFragments.get(position) != null) {
                frag = mFragments.get(position).fragment;
            } else {
                frag = Fragment.instantiate(mContext, mFragments.get(position).clsName);
                mFragments.get(position).fragment = frag;
            }

            return frag;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < mFragments.size()) {
                return mFragments.get(position).title;
            } else {
                return "None";
            }
        }

        class FragmentInfo {
            String clsName;
            String title;
            Fragment fragment;

            FragmentInfo(String clsName, String title) {
                this.clsName = clsName;
                this.title = title;
            }
        }
    }
}
