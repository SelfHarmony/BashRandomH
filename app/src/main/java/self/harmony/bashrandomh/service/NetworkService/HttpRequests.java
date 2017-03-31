package self.harmony.bashrandomh.service.NetworkService;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

//Created by selfharmony
public class HttpRequests {

    interface HtmlRequests {

        @GET("/")
        Observable<ResponseBody> getHtmlBody();

    }
}
