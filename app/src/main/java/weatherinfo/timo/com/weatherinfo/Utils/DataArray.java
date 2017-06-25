package weatherinfo.timo.com.weatherinfo.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hammouda on 6/24/17.
 *
 */

public class DataArray {
    public String date = "" ;
    public int tempD , tempN , tempEv , tempMor = 0 ;
    public String pressure = "";
    public String city = "";
    public String desc = "";
    public String icon = "";
    public String clauds = "";
    public String wind = "";
    public JSONObject temp , place ;
    public JSONArray weather  ;
}
