package hr.from.bkoruznjak.rida.root;

import dagger.Component;
import hr.from.bkoruznjak.rida.current.CurrentRideModule;
import hr.from.bkoruznjak.rida.current.CurrentRidePresenterImpl;
import hr.from.bkoruznjak.rida.history.PreviousRideDetailPresenter;
import hr.from.bkoruznjak.rida.history.PreviousRidesPresenterImpl;
import hr.from.bkoruznjak.rida.main.MainPresenterImpl;
import hr.from.bkoruznjak.rida.root.database.DatabaseModule;
import hr.from.bkoruznjak.rida.root.location.GPSService;
import hr.from.bkoruznjak.rida.root.location.LocationModule;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@AppScope
@Component(modules = {AppModule.class,
        LocationModule.class,
        DatabaseModule.class,
        CurrentRideModule.class})
public interface AppComponent {

    void inject(MainPresenterImpl target);

    void inject(CurrentRidePresenterImpl target);

    void inject(PreviousRidesPresenterImpl target);

    void inject(PreviousRideDetailPresenter target);

    void inject(GPSService target);
}