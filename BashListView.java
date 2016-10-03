package self.harmony.bashrandomh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BashListView extends ListView {
    public BashListView(Context context) {
        super(context);
    }

    public BashListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BashListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        
    }
}
