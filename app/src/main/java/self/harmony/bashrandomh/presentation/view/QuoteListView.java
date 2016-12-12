package self.harmony.bashrandomh.presentation.view;

import android.content.Context;

import com.arellomobile.mvp.MvpView;

public interface QuoteListView extends MvpView {

    String getString(int StringFromResource);

    Context getContext();
}
