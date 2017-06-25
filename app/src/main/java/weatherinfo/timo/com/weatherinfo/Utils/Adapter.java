package weatherinfo.timo.com.weatherinfo.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import weatherinfo.timo.com.weatherinfo.R;
public class Adapter extends ArrayAdapter<Data> {

    private int layoutResource;

    public Adapter(Context context, int layoutResource, List<Data> data) {
        super(context, layoutResource, data);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Data data = getItem(position);

        if (data != null) {
            TextView day = (TextView) view.findViewById(R.id.d);
            TextView temp = (TextView) view.findViewById(R.id.t);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            if (day != null) {
                day.setText(data.day);
            }

            if (temp != null) {
                temp.setText(data.temp);
            }

            if (icon != null) {
                Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + data.icon+".png").into(icon);
            }
        }

        return view;
    }
}