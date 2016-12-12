package self.harmony.bashrandomh;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

//Created by selfharmony
public class BashRandom extends Application {
    private static Context context;

    public BashRandom() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
