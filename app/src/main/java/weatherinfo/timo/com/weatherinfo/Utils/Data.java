package weatherinfo.timo.com.weatherinfo.Utils;

/**
 * Created by Hammouda on 6/24/17
 *
 */

public class Data {
    public String temp;
    public String day;
    public String icon;

    public Data(String day, String temp, String icon) {
        this.day = day;
        this.temp = temp;
        this.icon = icon;
    }
}