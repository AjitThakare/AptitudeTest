package psl.com.util;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ajit_thakare on 11/3/2016.
 */
public class NetworkHelper {
    public static final String TAG = NetworkHelper.class.getSimpleName();
   private static NetworkHelper singleTon;
    private ConnectivityManager cm;

    public NetworkHelper(ConnectivityManager cm) {
        this.cm=cm;
    }

        public static synchronized NetworkHelper getInstance(ConnectivityManager cm)
    {
        if (null == singleTon)
        {
            singleTon= new NetworkHelper(cm);
            Log.d(TAG,"NetworkHelper was Null");
        }
        return singleTon;

    }

    public boolean isOnline() { // checks if actual data traffic is ON or not

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public boolean isConnectedToInternet() { // simply checking wifi/sim net ON/OFF
       // ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
