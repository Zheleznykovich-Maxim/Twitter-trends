package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException, org.json.simple.parser.ParseException {
        ArrayList<Tweet> tweets = ReadTweets("./src/resources/cali_tweets2014.txt");
//        Date d = new Date();
//        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
//        System.out.println(format1.format(d));
//        for (Tweet tweet : tweets){
//            System.out.println(tweet.toString());
//        }
        HashMap<String, Double> emotions = ReadCSV("./src/resources/sentiments.csv");
//        for (Tweet tweet : tweets) {
//            tweet.CalcEmotion(emotions);
//            System.out.println(tweet.getEmotion());
//        }
        HashMap<String, ArrayList> states = ReadJSON("./src/resources/states.json");
        JFrame f = new JFrame("Полигон"); // создали фрейм
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        for (String state : states.keySet()) {
//            System.out.println("STATE= " + state);
//            test = new PrintTest((ArrayList) states.get(state).get(0));
//            f.add(test); // добавили наш
//
//        }
        PrintTest test1 = new PrintTest(states, tweets);
        f.setSize(1280,720);
        f.add(test1);
        f.pack(); // собрали
        f.setVisible(true);
//        PrintTest test = new PrintTest((ArrayList) states.get("NY").get(0));

//
//        List<Double> mapKeys = new ArrayList<>(emotions.values());
//        Collections.sort(mapKeys);
//        System.out.println(mapKeys);
        // file name is File.json


//        System.out.println(j.toString());
//        String Name = j.get("WA").toString();
//        System.out.println("Name :" + Name);
//
//        String userJson = "[{'WA': 'Alex','id': 1}, "
//                + "{'LA': 'Brian','od':2}, "
//                + "{'NY': 'Charles','id': 3}]";
//        Gson gson = new Gson();
//
//        State[] userArray = gson.fromJson(userJson, State[].class);
    }
    public static ArrayList<Tweet> ReadTweets(String name) {
        BufferedReader reader;
        ArrayList<Tweet> tweets = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(name));
            String line = reader.readLine();

            while (line != null) {

//                System.out.println(line);
                // read next line.
                String[] tweet = line.split("\t");
                tweet[0] = tweet[0].replaceAll("[,\\[\\]]", "");
                double[] coords = Arrays.stream(tweet[0].split(" "))
                        .mapToDouble(Double::parseDouble).toArray();
                DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = date1.parse(tweet[2]);
//                System.out.println("BEFORE: " + tweet[3]);
                tweet[3] = tweet[3].replaceAll("[^\\da-zA-Za ]", "");
//                System.out.println("AFTER: " + tweet[3]);
                tweets.add(new Tweet(coords, date, tweet[3]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return tweets;
    }
    public static HashMap<String, Double> ReadCSV(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(name));
        HashMap<String, Double> map = new HashMap<>();

        String line;
        Scanner scanner;

        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter(",");
            String word = scanner.next();
            Double emotion = Double.parseDouble(scanner.next());
            map.put(word, emotion);
        }
        //закрываем наш ридер
        reader.close();

        return map;
    }
    public static HashMap<String, ArrayList> ReadJSON(String name) throws IOException, org.json.simple.parser.ParseException {

//JACKSON
        Object o = new JSONParser().parse(new FileReader(name));
        JSONObject j = (JSONObject) o;
        ObjectMapper mapper = new ObjectMapper();
        String json = j.toString();

        try {
            // convert JSON string to Map
            Map<String, ArrayList> map = mapper.readValue(json, Map.class);

            // it works
            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

            System.out.println(map);
            return (HashMap<String, ArrayList>) map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}