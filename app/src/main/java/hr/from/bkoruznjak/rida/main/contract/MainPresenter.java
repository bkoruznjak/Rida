package hr.from.bkoruznjak.rida.main.contract;

import hr.from.bkoruznjak.rida.root.contract.RootPresenter;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface MainPresenter extends RootPresenter {
    void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults);

    void scheduleUploadJob();
}
