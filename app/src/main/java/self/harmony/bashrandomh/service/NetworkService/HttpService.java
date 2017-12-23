package self.harmony.bashrandomh.service.NetworkService;


import android.content.SharedPreferences;
import android.os.Handler;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpService {

    private static HttpService httpService;

    SharedPreferences mSharedPreferences;
    Retrofit mRetrofit;
    Handler handler;
    Random mRandom;

    public HttpService(SharedPreferences sharedPreferences, Retrofit retrofit) {
        mSharedPreferences = sharedPreferences;
        mRetrofit = retrofit;
        handler = new Handler();
        mRandom = new Random();
    }


    public Observable<ResponseBody> getBashHttpBody() {
        return Observable.create(new Observable.OnSubscribe<ResponseBody>() {
            @Override
            public void call(Subscriber<? super ResponseBody> subscriber) {
                HttpRequests.HtmlRequests bodyRequest;

                for (int i = 0; i < 3; i++) {
//                    getHttp(subscriber);
                    handler.postDelayed(() -> {
                        getHttp(subscriber);
                    }, 100*i);
                }
            }

            private void getHttp(Subscriber<? super ResponseBody> subscriber) {
                HttpRequests.HtmlRequests bodyRequest;
                bodyRequest = mRetrofit.create(
                        HttpRequests.HtmlRequests.class);
                String codeForCache = String.valueOf(mRandom.nextInt(100));
                bodyRequest.getHtmlBody(codeForCache)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(Throwable::printStackTrace)
                        .subscribe(responseBody ->
                        {
                            subscriber.onNext(responseBody);
                            System.out.println("__________________");
                            //todo отписка
                            /*if (i == 5) { } */
                        });
            }
        });
    }
}
