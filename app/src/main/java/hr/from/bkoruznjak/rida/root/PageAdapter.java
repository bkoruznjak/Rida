package hr.from.bkoruznjak.rida.root;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import hr.from.bkoruznjak.rida.current.CurrentRideFragment;
import hr.from.bkoruznjak.rida.history.PreviousRidesFragment;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public class PageAdapter extends FragmentPagerAdapter {

    public static final int MAX_NUMBER_OF_PAGES = 2;

    public static final int PAGE_CURRENT_RIDE = 0;
    public static final int PAGE_RIDE_HISTORY = 1;

    private int mNumOfTabs;
    private Fragment mCurrentFragment;

    public PageAdapter(FragmentManager fragmentManager, int numberOfTabs) {
        super(fragmentManager);
        this.mNumOfTabs = numberOfTabs;
    }

    @NonNull
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_CURRENT_RIDE:
                return new CurrentRideFragment();
            case PAGE_RIDE_HISTORY:
                return new PreviousRidesFragment();
            default:
                throw new IllegalStateException("Page not found");
        }
    }

    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}