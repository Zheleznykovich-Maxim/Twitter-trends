package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PrintTest extends JPanel {
    public HashMap<String, ArrayList> coords;
    public HashMap<String, Point> points = new HashMap<>();
    public ArrayList<Tweet> tweets;
    public HashMap<Double[], Color> emotions_colors;
    public HashMap<String, Polygon> polygons;
    public HashMap<String, ArrayList<Polygon>> state_polygons4;

    public PrintTest(HashMap<String, ArrayList> states, ArrayList<Tweet> tweets){
        coords = states;
        this.tweets = tweets;
        this.emotions_colors = emotions_colors;

    }
    public void GetPolygons(Graphics g) {
        polygons = new HashMap<>();
        state_polygons4 = new HashMap<>();
        for (String key : coords.keySet()) {
            ArrayList<ArrayList<ArrayList<Double>>> state = coords.get(key);
            if (state.get(0).get(0).size() != 2) {
                ArrayList<Polygon> polygons4 = new ArrayList<>();
                ArrayList<Double[]> centroids = new ArrayList<>();
                ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> state4 = coords.get(key);
                for (int i = 0; i < state4.size(); i++) {
                    int[] fillPolygon = new int[state4.get(i).get(0).size()];
                    int[] ints = new int[fillPolygon.length];
                    for (int j = 0; j < state4.get(i).get(0).size(); j++) {
                        fillPolygon[j] = (int) ((-state4.get(i).get(0).get(j).get(1) + 74.9) * 10.8);
                        ints[j] = (int) ((state4.get(i).get(0).get(j).get(0) + 180.0) * 10.8);
                    }
                    Polygon polygon = new Polygon(ints, fillPolygon, ints.length);
                    polygons.put(key, polygon);
                    polygons4.add(polygon);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setStroke(new BasicStroke(1.2F));
                    g2d.setColor(Color.gray);
                    g.drawPolygon(polygon);
                    centroids.add(findCentroid(state4.get(i).get(0)));
                }
                state_polygons4.put(key, polygons4);
                points.put(key, findCentroidByMass(centroids));
            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                for (int i = 0; i < state.get(0).size(); i++) {
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1) + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                Polygon polygon = new Polygon(ints, fillPolygon, ints.length);
                polygons.put(key, polygon);
                Double[] result = findCentroid(state.get(0));
                points.put(key, new Point(result[0].intValue(), result[1].intValue()));
            }
        }
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        GetPolygons(g);
        //FILL POLYGONS
        for (String key : polygons.keySet()){
            double state_emotion = 0;
            int count_tweets = 0;
            for (Tweet t : tweets) {
                t.getState(points);
                if (key.equals(t.state)) {
                    count_tweets++;
                    state_emotion+= t.emotion;
                }
            }
//            state_emotion /= count_tweets;
            ChooseColor(count_tweets, state_emotion, g);
            if (state_polygons4.get(key) != null){
                for (Polygon p : state_polygons4.get(key)) {
                    g.fillPolygon(p);
                }
            }
            g.fillPolygon(polygons.get(key));
            g.setColor(Color.gray);
            g.drawPolygon(polygons.get(key));
        }
        //SIGNATURES OF STATES
        for (String key : points.keySet()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Sergio", Font.BOLD, 11));
            g.drawString(key, points.get(key).x, points.get(key).y);
        }
        //PAINTING TWEETS
        for (Tweet t : tweets) {
            if (t.emotion == null){
                ChooseColor(0, 0.0, g);
            }
            else {
                ChooseColor(1, t.emotion, g);
            }
            int[] xy = OffSet(t.getLongitude(), t.getLatitude());
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(0));
            g.fillOval(xy[0], xy[1], 6, 6);
            g.setColor(Color.gray);
            g.drawOval(xy[0], xy[1], 6, 6);
        }
    }
    public Double[] findCentroid(ArrayList<ArrayList<Double>> state) {

        ArrayList<Double> off = state.get(0);
        double twicearea = 0;
        double x = 0;
        double y = 0;
        ArrayList<Double> p1;
        ArrayList<Double> p2;
        double f;
        int nPts = state.size();
        ArrayList<Double> x_coords = new ArrayList<>();
        ArrayList<Double> y_coords = new ArrayList<>();
        for (ArrayList<Double> s : state) {
            x_coords.add(s.get(0));
            y_coords.add(s.get(1));
        }
        if (Collections.frequency(y_coords, y_coords.get(0)) == y_coords.size()) {
            state.get(0).set(1, state.get(0).get(1) + 0.01);
        }
        for (int i = 0, j = nPts - 1; i < nPts; j = i++) {
            p1 = state.get(i);
            p2 = state.get(j);
            f = (p1.get(0) - off.get(0)) * (p2.get(1) - off.get(1)) - (p2.get(0) - off.get(0)) * (p1.get(1) - off.get(1));
            twicearea += f;
            x += (p1.get(0) + p2.get(0) - 2 * off.get(0)) * f;
            y += (p1.get(1) + p2.get(1) - 2 * off.get(1)) * f;
        }
        f = twicearea * 3;
        return new Double[] {((x / f + off.get(0)) + 179.5) * 10.8, (-(y / f + off.get(1)) + 75.5f) * 10.8, twicearea / 2};
    }
    public Point findCentroidByMass(ArrayList<Double[]> state) {
        double x = 0;
        double y = 0;
        double mass = 0;
        for (Double[] doubles : state) {
            x = x + doubles[0] * Math.abs(doubles[2]);
            y = y + doubles[1] * Math.abs(doubles[2]);
            mass = mass + Math.abs(doubles[2]);
        }
        return new Point((int) (((x / mass))), (int) (((y / mass))));

    }
    public void ChooseColor(int count_tweets, double state_emotion, Graphics g) {
        if (count_tweets == 0) {
            g.setColor(new Color(184,184,184));
        }
        else if (state_emotion == 0.0) {
            g.setColor(new Color(255,255,255));
        }
        else {
            if (state_emotion > 0) {
                g.setColor(new Color(212, 255 - 100 * (int) state_emotion % 255, 0));
            }
            else {
                g.setColor(new Color(44, 122 + (100  * (int)state_emotion) % 73, 122));
            }
//            if (state_emotion > 0) {
//                g.setColor(new Color((100 * (int) state_emotion % 255), 140, 0));
//            }
//            else {
//                g.setColor(new Color(140, ((138 * (-1) * (int)state_emotion) % 255), 0));
//            }
//            if (state_emotion > 0) {
//                g.setColor(new Color(255, 0, 0, (100 * (int) state_emotion) % 255));
//            }
//            else {
//                g.setColor(new Color(0, 0, 255, (-100 * (int) state_emotion) % 255));
//            }
        }
    }
    public static int[] OffSet(double x, double y) {
        return new int[] { (int) ((x + 180.0) * 10.8), (int) ((-y + 74.9) * 10.8)};
    }
}
