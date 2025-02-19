package aj.phone.client.Core;


import android.util.Log;

import javax.inject.Inject;

import aj.phone.client.NetworkModule.NetworkService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DIModule {

    private final Keyboard keyboard;
    private final MouseMove mouseMove;
    private final NetworkService networkService;
    private final ActivitiesManager activitiesManager;

    @Inject
    public DIModule() {
        Log.d("DI", "Creating injection objects");
        this.networkService = NetworkService.getInstance();
        this.activitiesManager = ActivitiesManager.getInstance();
        this.mouseMove = MouseMove.getInstance();
        this.keyboard = Keyboard.getInstance();
    }

    @Provides
    public MouseMove getMouseMove() {
        return this.mouseMove;
    }

    @Provides
    public NetworkService getNetworkModule() {
        return this.networkService;
    }

    @Provides
    public ActivitiesManager getActivitiesManager() {
        return this.activitiesManager;
    }

    @Provides
    public Keyboard getKeyboard() {
        return this.keyboard;
    }
}
