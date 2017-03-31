package self.harmony.bashrandomh.presentation.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import rx.subscriptions.CompositeSubscription;
import self.harmony.bashrandomh.presentation.view.LaunchView;
import self.harmony.bashrandomh.service.NetworkService.HttpService;
import self.harmony.bashrandomh.service.ParsingService.ListComposer;
import self.harmony.bashrandomh.util.NetworkUtil;


@InjectViewState
public class LaunchPresenter extends MvpPresenter<LaunchView> {
    private CompositeSubscription mCompositeSubscription;
    private int progress = 0;

    ListComposer mListComposer;

    public void loadQuotes(HttpService httpService) {
        mListComposer = new ListComposer(httpService);

        if (NetworkUtil.isConnectionAvailable()) {
            mListComposer.compose();
        }
    }



}
