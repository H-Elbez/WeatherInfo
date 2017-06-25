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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

import weatherinfo.timo.com.weatherinfo.Utils.AsyncParams;
import weatherinfo.timo.com.weatherinfo.Utils.DataArray;
import weatherinfo.timo.com.weatherinfo.Utils.SharedData;
import weatherinfo.timo.com.weatherinfo.Utils.WeatherHttp;

public class MainActivity extends AppCompatActivity {
    WeatherHttp weather ;
    AsyncParams params ;
    TextView temp , city,ttemp ;
    RelativeLayout loading ;
    Animation fading ;
    LinearLayout main ;
    ImageView icon ,ticon ;
    FloatingActionButton fab ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init Data
        ((SharedData)getApplication()).init();
        // Elt init
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current weather forecast");
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fabWeek);
        temp = (TextView) findViewById(R.id.temperature);
        ttemp = (TextView) findViewById(R.id.ttemperature);
        city = (TextView) findViewById(R.id.city);
        icon = (ImageView) findViewById(R.id.icon);
        ticon = (ImageView) findViewById(R.id.ticon);
        main = (LinearLayout) findViewById(R.id.Main);
        loading = (RelativeLayout) findViewById(R.id.loadingP);
        // Listeners
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WeeklyActivity.class));
                finish();}
        });
        // Retreive Data
        if(!((SharedData) getApplication()).isConnected()){
            Toast.makeText(getApplicationContext(),"Please check your Internet Connection !",Toast.LENGTH_LONG).show();}else{
        params = new AsyncParams(((SharedData)getApplication()).city,3);
        new RetrieveWeatherask().execute(params);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings :
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                finish();
                break;
            case R.id.action_weekly :
                startActivity(new Intent(MainActivity.this,WeeklyActivity.class));
                finish();
                break;
            case R.id.action_about :
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    // weather reception
    private class RetrieveWeatherask extends AsyncTask<AsyncParams, Object, DataArray[]> {
        @Override
        protected DataArray[] doInBackground(AsyncParams... params) {
            try {
                weather = new WeatherHttp();
                weather.Link = getResources().getString(R.string.Link);
                return  weather.Data(params[0].city, params[0].day);
            } catch (NetworkErrorException n) {
                Toast.makeText(MainActivity.this,"Internet Issue !",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(MainActivity.this,"Malformed city name",Toast.LENGTH_LONG).show();
                       Toast.makeText(MainActivity.this,"City name = Ghardaia",Toast.LENGTH_LONG).show();
                       ((SharedData) getApplication()).editor = ((SharedData)getApplication()).prefs.edit();
                       ((SharedData)getApplication()).editor.putString("city","Ghardaia");
                       ((SharedData)getApplication()).editor.apply();
                       startActivity(new Intent(MainActivity.this,SettingsActivity.class));
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
                    if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 5 &&
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 12 ){
                        temp.setText(String.valueOf(aVoid[1].tempMor).concat(" °"));
                        ttemp.setText(String.valueOf(aVoid[2].tempMor).concat(" °"));
                    }else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12 &&
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 17){
                        temp.setText(String.valueOf(aVoid[1].tempD).concat(" °"));
                        ttemp.setText(String.valueOf(aVoid[2].tempD).concat(" °"));
                    }else if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 17 &&
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <= 21){
                            System.out.println("null");
                        temp.setText(String.valueOf(aVoid[1].tempEv).concat(" °"));
                        ttemp.setText(String.valueOf(aVoid[2].tempEv).concat(" °"));
                    }else {
                        temp.setText(String.valueOf(aVoid[1].tempN).concat(" °"));
                        ttemp.setText(String.valueOf(aVoid[2].tempN).concat(" °"));
                    }
                    city.setText(aVoid[1].city);
                    Picasso.with(getApplicationContext()).load("http://openweathermap.org/img/w/" + aVoid[1].icon+".png").into(icon);
                    Picasso.with(getApplicationContext()).load("http://openweathermap.org/img/w/" + aVoid[2].icon+".png").into(ticon);
                    fading = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
                    loading.setVisibility(View.GONE);
                    main.setVisibility(View.INVISIBLE);
                    main.startAnimation(fading);
                    main.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        if(!((SharedData) getApplication()).isConnected()){
                            Toast.makeText(getApplicationContext(),"Please check your Internet Connection !",Toast.LENGTH_LONG).show();
                        }else{
                                Toast.makeText(getApplicationContext(),"Internet Issue !",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }}}
            });
            super.onPostExecute(aVoid);
        }
    }
}
