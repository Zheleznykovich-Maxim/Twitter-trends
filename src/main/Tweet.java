package main;

import java.util.Date;
import java.util.HashMap;

public class Tweet {
    private double[] latitude;

    private Date date;
    private String text;
    private double emotion;
    public double getEmotion() { return emotion; }
    public double[] getLatitude() { return latitude; }

    public Date getDate() { return date; }
    public String getText() { return text; }
    public Tweet(double[] latitude, Date date, String text) {
        this.latitude = latitude;
        this.date = date;
        this.text = text;
    }
    public void CalcEmotion(HashMap<String, Double> emotions) {
        emotion = 0;
        int count = 0;
        for (String t : text.split(" ")) {
            double result = emotions.getOrDefault(t, 0.0);
            if (result != 0.0){
                count++;
                emotion += result;
            }
        }
        if (emotion != 0){
            emotion /= count;
        }
    }


}
