package self.harmony.bashrandomh.ui.activity;

import static self.harmony.bashrandomh.R.id.progressBar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.stetho.Stetho;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.harmony.bashrandomh.R;
import self.harmony.bashrandomh.Views.BashListView;
import self.harmony.bashrandomh.Views.FontedTextView;
import self.harmony.bashrandomh.presentation.presenter.QuoteListPresenter;
import self.harmony.bashrandomh.presentation.view.QuoteListView;

public class QuoteListActivity extends MvpAppCompatActivity implements QuoteListView {

    public static final String TAG = "QuoteListActivity";
    private static final int MAX_QUOTES = 30;

    @InjectPresenter
    QuoteListPresenter mQuoteListPresenter;
    @BindView(R.id.textViewProgressBar)
    FontedTextView mProgressBarTextView;
    @BindView(R.id.emptyView)
    FontedTextView mEmptyView;
    @BindView(progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.list)
    BashListView mListView;
    @BindView(R.id.bashImageView)
    ImageView mBashImageView;
    private NetworkInfo mNetworkInfo;
    private ConnectivityManager mCm;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, QuoteListActivity.class);
        return intent;
    }


    @Override
    public Context getContext() {
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        initAllFields();

    }

    private void initAllFields() {
        mCm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mCm.getActiveNetworkInfo();
        mProgressBar.setMax(MAX_QUOTES); //задаем значение полного заполнения

    }
}
