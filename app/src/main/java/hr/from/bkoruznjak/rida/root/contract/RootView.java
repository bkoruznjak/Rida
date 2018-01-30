package hr.from.bkoruznjak.rida.root.contract;

import android.support.annotation.MainThread;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

public interface RootView {

    @MainThread
    void showToast(int messageResourceId);

    void goBack();

}
