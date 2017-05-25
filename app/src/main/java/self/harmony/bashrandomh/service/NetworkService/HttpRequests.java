package self.harmony.bashrandomh.service.NetworkService;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

//Created by selfharmony
public class HttpRequests {
    private static final String API_PREFIX = "/random/";

    interface HtmlRequests {
        @GET(API_PREFIX)
        Observable<ResponseBody> getHtmlBody(@Query("") String randomCode);

    }
}
