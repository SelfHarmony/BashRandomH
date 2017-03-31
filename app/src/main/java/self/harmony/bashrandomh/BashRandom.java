package self.harmony.bashrandomh;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import self.harmony.bashrandomh.dagger.components.DaggerNetComponent;
import self.harmony.bashrandomh.dagger.components.NetComponent;
import self.harmony.bashrandomh.dagger.modules.AppModule;
import self.harmony.bashrandomh.dagger.modules.NetModule;

//Created by selfharmony
public class BashRandom extends Application {
    private static Context context;
    private static NetComponent netComponent;

    public BashRandom() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .build());

        initNetComponent(this);
    }

    public void initNetComponent(BashRandom app) {
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(app))
                .netModule(new NetModule("")).build();
    }

    public static NetComponent getNetComponent() {
        return ((BashRandom)   getContext().getApplicationContext()).netComponent;
    }


    public static final class DaggerComponentInitializer {
        public static NetComponent init(BashRandom app) {
            return DaggerNetComponent.builder()
                    .appModule(new AppModule(app))
                    .netModule(new NetModule("")).build();
        }
    }
}
