package com.pro.empdep.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pro.empdep.screens.FriendRequestFragmentItem;
import com.pro.empdep.screens.GroupRequestFragmentItem;

public class RequestPagerAdapter extends FragmentStateAdapter {


    public RequestPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FriendRequestFragmentItem();
        }
        return new GroupRequestFragmentItem();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
