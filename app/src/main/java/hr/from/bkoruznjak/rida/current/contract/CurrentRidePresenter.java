package hr.from.bkoruznjak.rida.current.contract;

import hr.from.bkoruznjak.rida.root.contract.RootPresenter;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface CurrentRidePresenter extends RootPresenter {

    void mainActionClicked();

    void secondaryActionClicked();
}