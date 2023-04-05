package main;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;

public class Tweet {
    public double latitude;
    public double longitude;
    private final Date date;
    private final String text;
    public Double emotion;
    public String state;
    public double getEmotion() { return emotion; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getText() { return text; }
    public Tweet(double[] coords, Date date, String text) {
        this.latitude = coords[0];
        this.longitude = coords[1];
        this.date = date;
        this.text = text;
    }
    public void CalcEmotion(HashMap<String, Double> emotions) {
        emotion = 0.0;
        double count = 0;
        int count_repetitions;
        for (String key : emotions.keySet()) {
            String tt = text;
            if (text.contains(key)) {
                count_repetitions = (tt.length() - tt.replace(key, "").length()) / key.length();
                emotion += count_repetitions * emotions.get(key);
            }
        }
//        for (String t : text.split(" ")) {
//            emotion += emotions.getOrDefault(t, 0.0);
//        }
//        emotion /= count;
    }
    public void getState(HashMap<String, Point> points){
        Point dc = points.get("WA");
        int[] xy = PrintTest.OffSet(longitude, latitude);
        double len = Math.sqrt(
                Math.pow(Math.max(dc.x, xy[0]) - Math.min(dc.x, xy[0]), 2)
                        + Math.pow(Math.max(dc.y, xy[1]) - Math.min(dc.y, xy[1]), 2));
        for (String key : points.keySet()) {
            Point ps = points.get(key);
            double founded_len = Math.sqrt(
                    Math.pow(Math.max(ps.x, xy[0]) - Math.min(ps.x, xy[0]), 2)
                            + Math.pow(Math.max(ps.y, xy[1]) - Math.min(ps.y, xy[1]), 2));
            if (founded_len <= len) {
                len = founded_len;
                state = key;
            }
        }
    }

}
