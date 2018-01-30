package hr.from.bkoruznjak.rida.history.contract;

import hr.from.bkoruznjak.rida.root.contract.RootPresenter;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface PreviousRidePresenter extends RootPresenter {

    void loadPreviousRides();
}