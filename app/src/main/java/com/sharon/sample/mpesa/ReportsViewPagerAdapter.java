package com.sharon.sample.mpesa;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReportsViewPagerAdapter extends FragmentStateAdapter {
    private Context context;
    private int totalTabs;
    public ReportsViewPagerAdapter(Context context, int totalTabs, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                // Individual reports fragment
                PersonalReport personalReport = new PersonalReport();
                return personalReport;
            case 1:
                // MeriGo Report fragment
                MeriGoReport meriGoReport = new MeriGoReport();
                return  meriGoReport;
            case 2:
                // projects reports fragment
                ProjectsReport projectsReport =  new ProjectsReport();
                return  projectsReport;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
