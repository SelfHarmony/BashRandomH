package self.harmony.bashrandomh.service.NetworkService;


import android.content.SharedPreferences;

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


    public HttpService(SharedPreferences sharedPreferences, Retrofit retrofit) {
        mSharedPreferences = sharedPreferences;
        mRetrofit = retrofit;
    }


    public Observable<ResponseBody> getBashHttpBody() {
        return Observable.create(new Observable.OnSubscribe<ResponseBody>() {
            @Override
            public void call(Subscriber<? super ResponseBody> subscriber) {
                HttpRequests.HtmlRequests bodyRequest;
                for (int i = 0; i <= 50; i++) {
                    bodyRequest = mRetrofit.create(
                            HttpRequests.HtmlRequests.class);
                    bodyRequest.getHtmlBody().subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseBody ->
                            {
                                subscriber.onNext(responseBody);
                                //todo отписка
                                /*if (i == 5) {

                                }*/
                            });

                }
            }
        });
    }
}
