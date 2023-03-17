package main;

import javax.xml.crypto.Data;
import java.util.Date;

public class Tweet {
    private double[] latitude;

    private Date date;
    private String text;

    public double[] getLatitude() { return latitude; }

    public Date getDate() { return date; }
    public String getText() { return text; }
    public Tweet(double[] latitude, Date date, String text) {
        this.latitude = latitude;
        this.date = date;
        this.text = text;
    }


}
