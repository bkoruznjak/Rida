package hr.from.bkoruznjak.rida.root;

import dagger.Component;
import hr.from.bkoruznjak.rida.main.MainPresenterImpl;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@AppScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainPresenterImpl target);
}