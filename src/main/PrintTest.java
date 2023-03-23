package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PrintTest extends JPanel {
    public HashMap<String, ArrayList> coords;
    public HashMap<String, Point> points = new HashMap<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    public PrintTest(HashMap<String, ArrayList> states){
        coords = states;

    }
    public void paint(Graphics g) {
//        super.paintComponent(g);
        ArrayList<Polygon> polygons = new ArrayList<>();
        for (String key : coords.keySet()) {
            ArrayList<ArrayList<ArrayList<Double>>> state = coords.get(key);
            if (state.get(0).get(0).size() != 2) {
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
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setStroke(new BasicStroke(1.2F));
                    g2d.setColor(Color.GREEN);
                    g2d.fill(polygon);
                    g2d.setColor(Color.gray);
                    g.drawPolygon(polygon);
//                    if (i == state4.size() / 2 - 1){
//                        points.put(key, findCentroid(state4.get(i).get(0)));
//                    }
                    centroids.add(findCentroid(state4.get(i).get(0)));
                }
                points.put(key, findCentroidByMass(centroids));
            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                for (int i = 0; i < state.get(0).size(); i++) {
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1) + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                Graphics2D g2d = (Graphics2D) g;
                Polygon polygon = new Polygon(ints, fillPolygon, ints.length);
                g2d.setColor(Color.GREEN);
                g2d.fill(polygon);
                g2d.setColor(Color.gray);
                g2d.drawPolygon(polygon);
                Double[] result = findCentroid(state.get(0));
                points.put(key, new Point(result[0].intValue(), result[1].intValue()));
            }
        }
        int count = 0;
        for (String key : points.keySet()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Sergio", 1, 11));
            count++;
            System.out.println(key + " " + points.get(key));
            g.drawString(key, points.get(key).x, points.get(key).y);
        }
        System.out.println("Названий: " + count);
    }
    public Point getCenter(Polygon p) {
        int max_x;
        int max_y;
        int min_x;
        int min_y;
        int[] xpoints = p.xpoints;
        int sum_x = Arrays.stream(xpoints).sum();
        int[] ypoints = p.ypoints;
        int sum_y = Arrays.stream(ypoints).sum();
        Arrays.sort(xpoints);
        Arrays.sort(ypoints);
        max_x = xpoints[xpoints.length - 1];
        min_x = xpoints[0];
        max_y = ypoints[ypoints.length - 1];
        min_y = ypoints[0];
        Point result = new Point((int) ((max_x + min_x) / 2.0), (int) ((max_y + min_y) / 2.0));
        return result;
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
            System.out.println("ОПАНЬКИ  ");
            System.out.println(state.get(0).get(1));
            state.get(0).set(1, state.get(0).get(1) + 0.01);
            System.out.println(state.get(0).get(1));
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
        int nPts = state.size();
        for (int i = 0; i < state.size(); i++) {
            x = x + state.get(i)[0] * Math.abs(state.get(i)[2]);
            y = y + state.get(i)[1] * Math.abs(state.get(i)[2]);
            mass = mass + Math.abs(state.get(i)[2]);
        }
        Point result = new Point((int) (((x / mass))), (int) (((y / mass))));
        return result;

    }

}
