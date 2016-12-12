package self.harmony.bashrandomh.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import self.harmony.bashrandomh.util.Font;
import self.harmony.bashrandomh.R;

//Created by selfharmony
public class FontedTextView extends TextView {


    public FontedTextView(Context context) {
        super(context);

    }

    public FontedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFonts(attrs,context);

    }

    public FontedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFonts(attrs,context);
    }

    private void setFonts (AttributeSet attributeSet, Context context) {
        // Get all of the attributes we care about for this View
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FontedTextView);

        // See if a specific fontFace was defined. If not, default to Condensed
        int fontValue = a.getInteger(R.styleable.FontedTextView_fontFace,
                Font.GEORGIA.getEnumValue());
        final Typeface font = Font.valueOf(fontValue, context);
        setTypeface(font);
        a.recycle();
    }
}
