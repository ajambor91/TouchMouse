package aj.phone.client.Core;


import android.util.Log;

import javax.inject.Inject;

import aj.phone.client.NetworkModule.NetworkModule;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DIModule {

    private final MouseMove mouseMove;
    private final NetworkModule networkModule;
    private final ActivitiesManager activitiesManager;

    @Inject
    public DIModule() {
        Log.d("DI", "Creating injection objects");
        this.networkModule = NetworkModule.getInstance();
        this.activitiesManager = ActivitiesManager.getInstance();
        this.mouseMove = MouseMove.getInstance();
    }

    @Provides
    public MouseMove getMouseMove() {
        return this.mouseMove;
    }

    @Provides
    public NetworkModule getNetworkModule() {
        return this.networkModule;
    }

    @Provides
    public ActivitiesManager getActivitiesManager() {
        return this.activitiesManager;
    }
}
