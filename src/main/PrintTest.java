package main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PrintTest extends JPanel {
    public HashMap<String, ArrayList> coords;
    public PrintTest(HashMap<String, ArrayList> states){
        coords = states;

    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        int count = 0;
        for (String key : coords.keySet()) {

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
                    g2d.setPaint(Color.black);
                    g2d.fill(test);
                    g.drawPolygon(test);
                }

            }
            else {
                int[] fillPolygon = new int[state.get(0).size()];
                int[] ints = new int[state.get(0).size()];
                System.out.println(state.toString());
                for (int i = 0; i < state.get(0).size(); i++) {
                    System.out.println(state.get(0).get(i));
                    System.out.println(state.get(0).get(0).size());
                    System.out.println("ШТАТ = " + key);
                    fillPolygon[i] = (int) ((-state.get(0).get(i).get(1).doubleValue() + 74.9) * 10.8);
                    ints[i] = (int) ((state.get(0).get(i).get(0) + 180.0) * 10.8);
                }
                g.drawPolygon(ints, fillPolygon, ints.length);
            }
            count++;
            System.out.println("НАРИСОВАННО " + count);

        }
//        int count = 0;
//        for (ArrayList<ArrayList<ArrayList<Double>>> coord : coords) {
//            count++;
//
//            System.out.println(coord.toString());
//            for (int i = 0; i < coord.get(0).size(); i++) {
//                System.out.println(coord.get(0).get(i).get(0).toString());
//                fillPolygon[i] =  (int) ((-coord.get(0).get(i).get(1) + 74.9) * 10.8);
//                ints[i] = (int) ((coord.get(0).get(i).get(0) + 180.0) * 10.8);
//                System.out.println(ints[i] + " " + fillPolygon[i]);
//
//            }
//            g.drawPolygon(ints, fillPolygon, ints.length); // последний параметр неверно был задан
//            return;
//        }

    }
}
