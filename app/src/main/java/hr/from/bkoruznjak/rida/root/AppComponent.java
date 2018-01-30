package hr.from.bkoruznjak.rida.root;

import dagger.Component;
import hr.from.bkoruznjak.rida.current.CurrentRidePresenterImpl;
import hr.from.bkoruznjak.rida.main.MainPresenterImpl;
import hr.from.bkoruznjak.rida.main.RideModule;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@AppScope
@Component(modules = {AppModule.class,
        RideModule.class})
public interface AppComponent {

    void inject(MainPresenterImpl target);

    void inject(CurrentRidePresenterImpl target);
}