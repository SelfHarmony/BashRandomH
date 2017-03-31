package self.harmony.bashrandomh.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import self.harmony.bashrandomh.service.NetworkService.HttpService;
import self.harmony.bashrandomh.util.NetworkConfig;

@Module
public class NetModule {

    String mBaseUrl;

    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient() {
        OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(
                NetworkConfig.READ_TIMEOUT,
                TimeUnit.SECONDS)
                .connectTimeout(NetworkConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("BashRandom-Retrofit-Platform", "2");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);

                })
                .writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(
            OkHttpClient okHttpClient) { // TODO: 29.03.17 передать baseUrl и проверить работу
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConfig.HOST)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    HttpService provideHttpService(SharedPreferences sharedPreferences, Retrofit retrofit) {
        return new HttpService(sharedPreferences, retrofit);
    }

}