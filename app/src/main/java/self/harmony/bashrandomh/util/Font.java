package self.harmony.bashrandomh.util;

import android.content.Context;
import android.graphics.Typeface;

//Created by selfharmony
public enum Font {
    UBUNTU_MONO                 (0, "fonts/ubuntu.ttf"),
    GEORGIA                     (1, "fonts/georgia.ttf"),
    HELVETICA                   (2, "fonts/helvetica.ttf");


    private final int mEnumValue;
    private final String assetPath;

    Font(int enumValue, String assetPath) {
        this.mEnumValue = enumValue;
        this.assetPath = assetPath; //Typeface.createFromAsset(App.getContext().getAssets(), assetPath);
    }



    public Typeface asTypeface(Context context) {
        Typeface mTypeface = Typeface.createFromAsset(context.getAssets(), assetPath);
        return mTypeface;
    }

    public int getEnumValue() {
        return mEnumValue;
    }

    public static Typeface valueOf(int value, Context context) {
        for (Font font : values()) {
            if (font.mEnumValue == value) {
                return font.asTypeface(context);
            }
        }
        // You can use this return value to select
        // a default typeface to be returned if one
        // is not explicitly selected
        return GEORGIA.asTypeface(context);
    }

}
