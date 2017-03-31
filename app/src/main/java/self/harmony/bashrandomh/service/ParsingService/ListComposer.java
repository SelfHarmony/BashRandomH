package self.harmony.bashrandomh.service.ParsingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import self.harmony.bashrandomh.pojo.Quote;
import self.harmony.bashrandomh.service.NetworkService.HttpService;

//Created by selfharmony
public class ListComposer {

    private HttpService mHttpService;
    private HashMap<String, Quote> quotesFilterMap;

    public ListComposer(HttpService httpService) {
        mHttpService = httpService;
    }

    public void compose() {
        subscribeParser(mHttpService);
//        subscribeProgressBar();
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
                        addMoreFilteredQuotes(quotesFilterMap, new ParsingService().startParsing(responseBody));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
    }

    private void addMoreFilteredQuotes(HashMap<String, Quote> quotesFilterMap,
            ArrayList<Quote> quotes) {
        for (Quote quote : quotes) {
            String key = quote.getId();
            quotesFilterMap.put(key, quote);
        }
    }


    public static class ListComposingSubscription {
        private static PublishSubject<Integer> mListComposingBehaviourSubject =
                PublishSubject.create();

        public static PublishSubject<Integer> getListComposingBehaviourSubject() {
            return mListComposingBehaviourSubject;
        }

        public static Observable<Integer> getListComposingObservable() {
            return mListComposingBehaviourSubject.asObservable();
        }
    }
}
