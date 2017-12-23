package self.harmony.bashrandomh.service.ParsingService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import self.harmony.bashrandomh.pojo.Quote;
import self.harmony.bashrandomh.service.NetworkService.HttpService;

//Created by selfharmony
public class ListComposer {

    private HttpService mHttpService;
    private HashMap<String, Quote> quotesFilterMap;
    private Subscription omParsingCompleteSubscription;

    public ListComposer(HttpService httpService) {
        mHttpService = httpService;
        quotesFilterMap = new HashMap<>();
    }

    public void compose() {
        subscribeParser(mHttpService);
        subscribeOnParsingComplete();
//        subscribeProgressBar();
    }

    private void subscribeOnParsingComplete() {
        omParsingCompleteSubscription = ListComposingSubscription.getListComposingPublishSubject()
                .onBackpressureBuffer()
                .doOnError(Throwable::printStackTrace)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(quote -> {
                        quotesFilterMap.put(quote.getId(), quote);
                        System.out.println(quotesFilterMap.size());
                });
    }

//    private void subscribeProgressBar() {
//        ProgressBarSubscription.getProgressObservable()
//                .subscribeOn(Schedulers.newThread())
////                .onBackpressureDrop()
//                .onBackpressureBuffer()
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError(Throwable::printStackTrace)
//                .subscribe(integer -> {
//                    progress += integer;
//                    getViewState().setStartScreenProgressBarProgress(progress);
//                });
//    }

    private void subscribeParser(HttpService httpService) {
        Observable<ResponseBody> observable = httpService.getBashHttpBody();
                observable
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .subscribe(responseBody -> {
                    try {
                        new ParsingService().startParsing(responseBody);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
    }



    static class ListComposingSubscription {
        private static PublishSubject<Quote> mListComposingBehaviourSubject =
                PublishSubject.create();

        static PublishSubject<Quote> getListComposingPublishSubject() {
            return mListComposingBehaviourSubject;
        }

        public static Observable<Quote> getListComposingObservable() {
            return mListComposingBehaviourSubject.asObservable();
        }
    }
}
