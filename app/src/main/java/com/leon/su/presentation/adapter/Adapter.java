package com.leon.su.presentation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leon.su.presentation.fragment.BevFragment;
import com.leon.su.presentation.fragment.FoodFragment;
import com.leon.su.presentation.fragment.MiscFragment;

import java.util.ArrayList;

public class Adapter extends FragmentStateAdapter {
    public Adapter(FragmentManager fragmentManager, Lifecycle lifecycle){
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch(position)
        {
            case 0:
                return new FoodFragment();
            case 1:
                return new BevFragment();
            case 2:
                return new MiscFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
