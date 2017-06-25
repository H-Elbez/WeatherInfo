package weatherinfo.timo.com.weatherinfo.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

/**
 * Created by Hammouda on 6/24/17.
 *
 */

public class SharedData extends Application{
    public String city ;
    public SharedPreferences prefs ;
    public SharedPreferences.Editor editor ;

    public void init(){
        prefs =  getSharedPreferences("Data" , 0);
        city = prefs.getString("city","Ghardaïa, DZ");
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            return true;
        } else
            return false;

    }
}
