package com.sharon.sample.mpesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class Reports extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 vpReports;
    ReportsViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        tabLayout = findViewById(R.id.tabLayout);
        vpReports = findViewById(R.id.vpReports);
        adapter = new ReportsViewPagerAdapter(this, 3, getSupportFragmentManager(), getLifecycle());

        // make viewpager get tabs from the adapter
        vpReports.setAdapter(adapter);
        // listen for tab selection and update the current tab
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // invoked on selecting tab
                vpReports.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // change fragment titles
        vpReports.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // set current tab title
                tabLayout.getTabAt(position).select();
            }
        });

    }
}