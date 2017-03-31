//package self.harmony.bashrandomh.service;
//
//import android.util.Log;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.io.IOException;
//
//import rx.Observable;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//import rx.subjects.BehaviorSubject;
//import self.harmony.bashrandomh.service.ParsingService.ParsingService;
//import self.harmony.bashrandomh.util.NetworkUtil;
//
////Created by selfharmony
//public class HttpServiceOld {
//
//    private static final String HTTP_BASH_IM = "http://bash.im/random/";
//    private static BehaviorSubject<Document> mParsingDocumentBehaviourSubject = BehaviorSubject.create();
//    private Subscription connectSubscription;
//    private static HttpServiceOld mHttpService;
//    private static ParsingService mParsingService;
//
//    public static HttpServiceOld getInstance() {
//        if (mHttpService == null) {
//            mHttpService = new HttpServiceOld();
//            mParsingService = new ParsingService();
//        }
//        return mHttpService;
//    }
//
//    public void getHtmlAndPublishIt() {
//        System.out.println("before checking connection");
//        if (NetworkUtil.isConnectionAvailable()) {
//            System.out.println("http: connection available");
//            subscribeBashConnecting();
//            connectToBash();
//        } else {
//            System.out.println("No Connetion");
//        }
//    }
//
//    private void connectToBash() {}
//
//    public Observable<Document> getJsoup() {
//        return Observable.fromCallable(() -> {
//            try {
//                Document jsoup_proxy = Jsoup.connect(HTTP_BASH_IM)
//                        .ignoreContentType(true)
//                        .ignoreHttpErrors(true)
//                        .timeout(10000)
//                        .get();
//
//                return jsoup_proxy;
//            } catch (IOException e) {
//                // just rethrow as RuntimeException to be caught in subscriber's onError
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    public void subscribeBashConnecting() {
//        System.out.println("before StartParsing");
//        if (connectSubscription == null || connectSubscription.isUnsubscribed()) {
//            connectSubscription = getJsoup()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread()) // this scheduler is exported by RxAndroid library
//                    .subscribe(document -> {
//                                giveDocumentToParser().onNext(document);
//                                // reloadAdapter();
//                            },
//                            error -> Log.getStackTraceString(error));
//        }
//    }
//
//
//
//    private Document getJsoupData(String http) {
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(http).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return doc;
//    }
//
//    private static BehaviorSubject<Document> giveDocumentToParser() {
//        return mParsingDocumentBehaviourSubject;
//    }
//
//    public static Observable<Document> getDocumentObservable() {
//        return mParsingDocumentBehaviourSubject.asObservable();
//    }
//}
