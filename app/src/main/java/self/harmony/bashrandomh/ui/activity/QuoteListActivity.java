package self.harmony.bashrandomh.ui.activity;

import static self.harmony.bashrandomh.R.id.footerProgressBar;
import static self.harmony.bashrandomh.R.id.progressBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.harmony.bashrandomh.R;
import self.harmony.bashrandomh.Views.BashListView;
import self.harmony.bashrandomh.Views.FontedTextView;
import self.harmony.bashrandomh.adapter.QuoteAdapter;
import self.harmony.bashrandomh.pojo.Quote;
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
    FontedTextView mEmptyTextView;
    @BindView(progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.list)
    BashListView mListView;
    @BindView(R.id.bashImageView)
    ImageView mBashImageView;
    private NetworkInfo mNetworkInfo;
    private ConnectivityManager mCm;
    private QuoteAdapter quoteAdapter;
    private boolean bashIsReachable = true;
    private ProgressBar mFooterProgressBar;
    private int minRating;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, QuoteListActivity.class);
        return intent;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        initAllFields();
        CheckNetworkAndSetViews();
    }


    private void initAllFields() {
        mCm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mCm.getActiveNetworkInfo();
        mProgressBar.setMax(MAX_QUOTES); //задаем значение полного заполнения
        quoteAdapter = new QuoteAdapter(this, new ArrayList<Quote>());

        getMinRatingFromPreferences();
    }

    private void CheckNetworkAndSetViews() {
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            mEmptyTextView.setVisibility(View.GONE);
            //выделяем отдельный объект, чтобы потом при обновлении просто запускать задачу снова -> parser.execute(HTTP_BASH_IM);
            //MainActivity.BackgroundJsoup parser = new MainActivity.BackgroundJsoup();
            quoteAdapter = new QuoteAdapter(this, new ArrayList<Quote>());
            //parser.execute(HTTP_BASH_IM);

            if (bashIsReachable) {

                View footer = getLayoutInflater().inflate(R.layout.footer, null);
                mListView.setVisibility(View.GONE); //скрываем mListView чтоб не показывать лишние элементы на стартово экране
                mListView.addFooterView(footer); //впихиваем футер в наш mListView
                mFooterProgressBar = (ProgressBar) findViewById(footerProgressBar);
                mFooterProgressBar.setMax(MAX_QUOTES); //задаем значение полного заполнения
                mListView.setAdapter(quoteAdapter); //задаем адаптер ПОСЛЕ установки футера

            } else {
                mEmptyTextView.setVisibility(View.VISIBLE);
                hideQuotesView();
                mEmptyTextView.setText(R.string.unreachable);
            }

        } else {
            hideQuotesView();
            mEmptyTextView.setText(R.string.noInternet);
        }
    }

    private void hideQuotesView() {
        mListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mProgressBarTextView.setVisibility(View.GONE);
        mBashImageView.setVisibility(View.GONE);
    }

    private void getMinRatingFromPreferences() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String stringRating = sharedPrefs.getString(getString(R.string.settings_minRating_key),
                getString(R.string.settings_minRating_default));

        minRating = getValidRating(stringRating);
    }

    private int getValidRating(String ratingS) {
        int rating;
        try {
            rating = Integer.parseInt(ratingS);
        } catch (Exception e) {
            rating = Integer.parseInt(String.valueOf(R.string.settings_minRating_default));
        }
        return rating;
    }



    @Override
    public void setBashAvailability(boolean b) {
        bashIsReachable = b;
    }

    @Override
    public void setFieldsForPresenter() {
        mQuoteListPresenter.setActivityContext(this);
        mQuoteListPresenter.setBashIsAvailable(bashIsReachable);
        mQuoteListPresenter.setMinRating(minRating);
    }

}
