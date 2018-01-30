package hr.from.bkoruznjak.rida.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.from.bkoruznjak.rida.R;

/**
 * THIS IS JUST A BASE LAYOUT CONTAINER TO HELP TRANSACTION MANAGER OUT
 * <p>
 * Created by bkoruznjak on 30/01/2018.
 */

public class PreviousRidesRootFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_ride_root, container, false);
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentRootLayout, new PreviousRidesFragment());
        transaction.commit();
        return view;
    }

}