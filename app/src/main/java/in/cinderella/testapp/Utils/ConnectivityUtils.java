package in.cinderella.testapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }
}