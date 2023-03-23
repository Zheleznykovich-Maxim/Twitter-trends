package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.awt.Font.BOLD;

public class PrintTest extends JPanel {
    public HashMap<String, ArrayList> coords;
    public HashMap<String, Point> points;

    public PrintTest(HashMap<String, ArrayList> states){
        coords = states;

    }
    public ArrayList<Polygon> GetPolygons() {
        ArrayList<Polygon> polygons = new ArrayList<>();
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
            }
        }
        return polygons;
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        int count = 0;
        for (String key : coords.keySet()) {
            System.out.println("ШТАТ = " + key);
            ArrayList<ArrayList<ArrayList<Double>>> state = coords.get(key);
            if (state.get(0).get(0).size() != 2) {
                System.out.println("ЧЕТЫРЁХМЕРНЫЙ МАССИВ");
                ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> state4 = coords.get(key);
                for (int i = 0; i < state4.size(); i++) {
                    int[] fillPolygon = new int[state4.get(i).get(0).size()];
                    int[] ints = new int[state4.get(i).get(0).size()];
                    for (int j = 0; j < state4.get(i).get(0).size(); j++){
                        fillPolygon[j] = (int) ((-state4.get(i).get(0).get(j).get(1) + 74.9) * 10.8);
                        ints[j] = (int) ((state4.get(i).get(0).get(j).get(0) + 180.0) * 10.8);
                    }

                    Polygon test = new Polygon(ints, fillPolygon, ints.length);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(Color.green);
                    g2d.fill(test);
                    g2d.setPaint(Color.black);
                    g2d.setFont(new Font("Serif", 1, 12));
                    g2d.setStroke(new BasicStroke(2));
                    if (i == state4.size() / 2 - 1){
                        Point test2 = new Point(getCenter(test));
                        points.put(key, test2);

                    }
                    g2d.setPaint(Color.gray);
                    g.drawPolygon(ints, fillPolygon, ints.length);
                }

            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                System.out.println(state.toString());
                for (int i = 0; i < state.get(0).size(); i++) {
//                    System.out.println(state.get(0).get(i));
//                    System.out.println(state.get(0).get(0).size());
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1) + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                Polygon test = new Polygon(ints, fillPolygon, ints.length);

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
            count++;
            System.out.println("НАРИСОВАННО " + count);
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
        int[] ypoints = p.ypoints;
        Arrays.sort(xpoints);
        Arrays.sort(ypoints);
        max_x = xpoints[xpoints.length - 1];
        min_x = xpoints[0];
        max_y = ypoints[ypoints.length - 1];
        min_y = ypoints[0];
        System.out.println(String.format("XMAX = %d\nXMIN = %d\nYMAX = %d\nYMIN = %d", max_x, min_x, max_y, min_y));
        return new Point((int) ((max_x + min_x) / 2.0), (int) ((max_y + min_y) / 2.0));
    }


}
