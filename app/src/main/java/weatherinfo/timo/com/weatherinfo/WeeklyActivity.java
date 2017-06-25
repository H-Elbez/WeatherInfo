package weatherinfo.timo.com.weatherinfo;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import weatherinfo.timo.com.weatherinfo.Utils.Adapter;
import weatherinfo.timo.com.weatherinfo.Utils.AsyncParams;
import weatherinfo.timo.com.weatherinfo.Utils.Data;
import weatherinfo.timo.com.weatherinfo.Utils.DataArray;
import weatherinfo.timo.com.weatherinfo.Utils.SharedData;
import weatherinfo.timo.com.weatherinfo.Utils.WeatherHttp;

public class WeeklyActivity extends AppCompatActivity {
    FloatingActionButton Today;
    ListView listView;
    AsyncParams params;
    SimpleDateFormat sdf1;
    List<Data> list;
    Data data;
    WeatherHttp weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.toolbar);
        actionBarToolBar.setTitle("Weekly weather forecast");
        actionBarToolBar.inflateMenu(R.menu.menu_weekly);
        setSupportActionBar(actionBarToolBar);
        Today = (FloatingActionButton) findViewById(R.id.fabToday);
        listView = (ListView) findViewById(R.id.list);
        sdf1 = new SimpleDateFormat("EEEE");
        Today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeeklyActivity.this,MainActivity.class));
                finish();
            }
        });
        // Retreive Data
        params = new AsyncParams(((SharedData)getApplication()).city,8);
        new RetrieveWeeklyWeatherask().execute(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weekly, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings :
                startActivity(new Intent(WeeklyActivity.this,SettingsActivity.class));
                finish();
                break;
            case R.id.action_daily :
                startActivity(new Intent(WeeklyActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.action_about :
                startActivity(new Intent(WeeklyActivity.this,AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // weather reception
    private class RetrieveWeeklyWeatherask extends AsyncTask<AsyncParams, Object, DataArray[]> {
        @Override
        protected DataArray[] doInBackground(AsyncParams... params) {
            try {
                weather = new WeatherHttp();
                weather.Link = getResources().getString(R.string.Link);
                return  weather.Data(params[0].city, params[0].day);
            } catch (NetworkErrorException n) {
                Toast.makeText(WeeklyActivity.this,"Internet Issue !",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeeklyActivity.this,"Malformed city name",Toast.LENGTH_LONG).show();
                        Toast.makeText(WeeklyActivity.this,"City name = Ghardaia",Toast.LENGTH_LONG).show();
                        ((SharedData) getApplication()).editor = ((SharedData)getApplication()).prefs.edit();
                        ((SharedData)getApplication()).editor.putString("city","Ghardaia");
                        ((SharedData)getApplication()).editor.apply();
                        startActivity(new Intent(WeeklyActivity.this,SettingsActivity.class));
                        finish();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(final DataArray[] aVoid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        list = new ArrayList<>();
                        for( DataArray d : aVoid){
                            if(Integer.valueOf(d.date) != 0){
                            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 5 &&
                                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 12 ){
                                data = new Data(numToDate(d.date),String.valueOf(d.tempMor)+" °",d.icon);

                            }else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12 &&
                                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 17){
                                data = new Data(numToDate(d.date),String.valueOf(d.tempD)+" °",d.icon);

                            }else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 17 &&
                                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 21){
                                data = new Data(numToDate(d.date),String.valueOf(d.tempEv)+" °",d.icon);

                            }else {
                                data = new Data(numToDate(d.date),String .valueOf(d.tempN)+" °",d.icon);

                            }
                            list.add(data);}
                        }
                        Adapter adapter = new Adapter(WeeklyActivity.this, R.layout.row, list);
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        if(!((SharedData) getApplication()).isConnected()){
                            Toast.makeText(getApplicationContext(),"Please check your Internet Connection !",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Internet Issue !",Toast.LENGTH_LONG).show();
                        }}}
            });
            super.onPostExecute(aVoid);
        }
    }
    public String numToDate(String x){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, Integer.valueOf(x)-1);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        return  sdf1.format(c.getTime());
    }
}
