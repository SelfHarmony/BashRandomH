package self.harmony.bashrandomh.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import self.harmony.bashrandomh.BashRandom;
import self.harmony.bashrandomh.R;
import self.harmony.bashrandomh.presentation.presenter.LaunchPresenter;
import self.harmony.bashrandomh.presentation.view.LaunchView;
import self.harmony.bashrandomh.service.NetworkService.HttpService;

public class LaunchActivity extends MvpAppCompatActivity implements LaunchView {
    public static final String TAG = "LaunchActivity";
    @InjectPresenter
    LaunchPresenter mLaunchPresenter;
    @BindView(R.id.progressBar3)
    ProgressBar mProgressBar;
    Subscription mProgressBarSubscription;

    @Inject
    HttpService mHttpService;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, LaunchActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        BashRandom.getNetComponent().inject(this);
        mProgressBar.setMax(1000);
        mLaunchPresenter.loadQuotes(mHttpService);

    }


    @Override
    public void startQuoteListActivity() {
        startActivity(QuoteListActivity.getIntent(this));
        finish();
    }

    @Override
    public void setStartScreenProgressBarProgress(int progress) {
        mProgressBar.setProgress(progress);
    }


    public void unsubscribeProgress() {
        if (mProgressBarSubscription != null && !mProgressBarSubscription.isUnsubscribed()) {
            mProgressBarSubscription.unsubscribe();
        }
    }
}
