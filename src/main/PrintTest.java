package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.Font.BOLD;

public class PrintTest extends JPanel {
    public HashMap<String, ArrayList> coords;
    public HashMap<String, Point> points = new HashMap<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    public PrintTest(HashMap<String, ArrayList> states){
        coords = states;

    }
    public ArrayList<Polygon> GetPolygons() {
        for (String key : coords.keySet()) {
            Polygon polygon;
            ArrayList<ArrayList<ArrayList<Double>>> state = coords.get(key);
            if (state.get(0).get(0).size() != 2) {
                ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> state4 = coords.get(key);
                for (int i = 0; i < state4.size(); i++) {
                    int[] fillPolygon = new int[state4.get(i).get(0).size()];
                    int[] ints = new int[fillPolygon.length];
                    for (int j = 0; j < state4.get(i).get(0).size(); j++) {
                        fillPolygon[j] = (int) ((-state4.get(i).get(0).get(j).get(1) + 74.9) * 10.8);
                        ints[j] = (int) ((state4.get(i).get(0).get(j).get(0) + 180.0) * 10.8);
                    }
                    polygon = new Polygon(ints, fillPolygon, ints.length);
                    polygons.add(polygon);
                    if (i == state4.size() / 2 - 1){
                        points.put(key, getCenter(polygon));
                    }
                }
            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                for (int i = 0; i < state.get(0).size(); i++) {
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1) + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                polygon = new Polygon(ints, fillPolygon, ints.length);
                polygons.add(polygon);
                points.put(key, getCenter(polygon));
            }
        }
        System.out.print(points.toString());
        return polygons;
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        int count = 0;
        ArrayList<Polygon> polygons = new ArrayList<>();
        System.out.println(coords.keySet());
        for (String key : coords.keySet()) {
//            System.out.println("ШТАТ = " + key);
            ArrayList<ArrayList<ArrayList<Double>>> state = coords.get(key);
            if (state.get(0).get(0).size() != 2) {
                ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> state4 = coords.get(key);
                for (int i = 0; i < state4.size(); i++) {
                    int[] fillPolygon = new int[state4.get(i).get(0).size()];
                    int[] ints = new int[state4.get(i).get(0).size()];
                    for (int j = 0; j < state4.get(i).get(0).size(); j++){
                        fillPolygon[j] = (int) ((-state4.get(i).get(0).get(j).get(1) + 74.9) * 10.8);
                        ints[j] = (int) ((state4.get(i).get(0).get(j).get(0) + 180.0) * 10.8);
                    }

                    Polygon test = new Polygon(ints, fillPolygon, ints.length);
                    polygons.add(test);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(Color.green);
                    g2d.fill(test);
                    g2d.setPaint(Color.black);
                    g2d.setFont(new Font("Serif", 1, 12));
                    g2d.setStroke(new BasicStroke(2));
                    if (i == state4.size() / 2 - 2){
                        Point test2 = new Point(getCenter(test));
                        points.put(key, test2);

                    }
                    g2d.setPaint(Color.gray);
                    g.drawPolygon(ints, fillPolygon, ints.length);
                    System.out.println(test.npoints);
                }

            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                for (int i = 0; i < state.get(0).size(); i++) {
//                    System.out.println(state.get(0).get(i));
//                    System.out.println(state.get(0).get(0).size());
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1) + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                Polygon test = new Polygon(ints, fillPolygon, ints.length);
                polygons.add(test);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(Color.green);
                g2d.fill(test);
                Point test2 = new Point(getCenter(test));
                points.put(key, test2);
                g2d.setPaint(Color.gray);
                g.drawPolygon(ints, fillPolygon, ints.length);
                ;
//                g.fillOval(test2.x, test2.y, 10, 10);
//                g.drawOval(test2.x, test2.y, 10, 10);


            }
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.black);

        for (String key : points.keySet()) {
            g.drawString(key, points.get(key).x, points.get(key).y);
        }
//
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


}
