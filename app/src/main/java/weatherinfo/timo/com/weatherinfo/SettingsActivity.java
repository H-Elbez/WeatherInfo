package weatherinfo.timo.com.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import weatherinfo.timo.com.weatherinfo.Utils.SharedData;


public class SettingsActivity extends AppCompatActivity {


    static EditTextPreference pass  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new GeneralPreferenceFragment()).commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            setMenuVisibility(true);
            pass = (EditTextPreference) findPreference("list");
            pass.setText(((SharedData)getActivity().getApplication()).city);
            pass.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, final Object newVal) {
                    if(newVal.toString().length() > 0){
                        if (!newVal.equals(((SharedData)getActivity().getApplication()).city)) {
                            ((SharedData) getActivity().getApplication()).editor = ((SharedData) getActivity().getApplication()).prefs.edit();
                            ((SharedData) getActivity().getApplication()).editor.putString("city", String.valueOf(newVal));
                            ((SharedData) getActivity().getApplication()).editor.apply();
                            Toast.makeText(getActivity(), "City changed successfully", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    } else{
                        return false;}
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this,MainActivity.class));
        finish();
    }
}