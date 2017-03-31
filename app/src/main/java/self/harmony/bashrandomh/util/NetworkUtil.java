package self.harmony.bashrandomh.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import self.harmony.bashrandomh.BashRandom;

public class NetworkUtil {
    public static boolean isConnectionAvailable() {
        Context context = BashRandom.getContext();
        ConnectivityManager cm = (ConnectivityManager) BashRandom.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
