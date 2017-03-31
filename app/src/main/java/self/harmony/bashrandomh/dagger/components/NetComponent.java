package self.harmony.bashrandomh.dagger.components;


import javax.inject.Singleton;

import dagger.Component;
import self.harmony.bashrandomh.dagger.modules.AppModule;
import self.harmony.bashrandomh.dagger.modules.NetModule;
import self.harmony.bashrandomh.service.NetworkService.HttpService;
import self.harmony.bashrandomh.ui.activity.LaunchActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    // downstream components need these exposed
    void inject(HttpService httpService);

    void inject(LaunchActivity launchActivity);
}