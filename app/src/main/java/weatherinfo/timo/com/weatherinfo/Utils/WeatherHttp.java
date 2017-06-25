package weatherinfo.timo.com.weatherinfo.Utils;

import android.accounts.NetworkErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * Created by Hammouda on 6/24/17.
 *
 */

public class WeatherHttp {
    public HttpURLConnection cnx ;
    public InputStream In ;
    public String temp ;
    public BufferedReader reader ;
    public JSONObject json ;
    public String Link ;
    public JSONArray data ;
    public StringBuffer output ;
    public DataArray[] Data ;
    public DataArray[] Data(String city , int days) throws NetworkErrorException , JSONException , IOException{
            cnx = (HttpURLConnection) (new URL(Link.replace("[CITY]", (city)).replace("[DAYS]", (String.valueOf(days))))).openConnection();
            cnx.setRequestMethod("GET");
            cnx.setDoInput(true);
            cnx.setDoOutput(true);
            cnx.connect();
            In = cnx.getInputStream();
            temp = "";
            output = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(In));
            while ((temp = reader.readLine()) != null){
            output = output.append(temp+"\r\n");}
            cnx.disconnect();
            reader.close();
            if(output != null){
                json = new JSONObject(output.toString());
                    data = json.getJSONArray("list");
                Data = new DataArray[data.length()];
                for( int i = 0 ; i < data.length() ;i++){
                     Data[i] = new DataArray();
                     Data[i].temp = data.getJSONObject(i).getJSONObject("temp");
                     Data[i].weather = data.getJSONObject(i).getJSONArray("weather");
                     Data[i].place = json.getJSONObject("city");
                     Data[i].clauds = data.getJSONObject(i).getString("clouds");
                     Data[i].wind = data.getJSONObject(i).getString("speed");
                     Data[i].pressure = data.getJSONObject(i).getString("pressure");
                     Data[i].date = String.valueOf(i);
                     Data[i].tempD = Data[i].temp.getInt("day");
                     Data[i].city = Data[i].place.getString("name")+" , "+Data[i].place.getString("country");
                     Data[i].tempN = Data[i].temp.getInt("night");
                     Data[i].tempEv = Data[i].temp.getInt("eve");
                     Data[i].tempMor = Data[i].temp.getInt("morn");
                     Data[i].icon = Data[i].weather.getJSONObject(0).getString("icon");
                     Data[i].desc = Data[i].weather.getJSONObject(0).getString("description");}
            }else {
                return null;
            }

            return Data;
    }


}
