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
        //TWEETS
        ArrayList<Tweet> tweets = ReadTweets("./src/resources/cali_tweets2014.txt");
        //EMOTIONS
        HashMap<String, Double> emotions = ReadCSV("./src/resources/sentiments.csv");
        //STATES
        HashMap<String, ArrayList> states = ReadJSON("./src/resources/states.json");
        //TWEET EMOTIONS
        ArrayList<Integer> tweets_emotions = new ArrayList<>();
        for (Tweet tweet : tweets) {
            tweet.CalcEmotion(emotions);
            tweets_emotions.add((int) (tweet.getEmotion() * 100));
        }
        Collections.sort(tweets_emotions);
//        tweets_emotions.forEach((t) -> System.out.println(t));
//        System.out.println("Максимум: " + max_emoiton);
//        System.out.println("Минимум: " + min_emotion);

        //PAINTING
        JFrame f = new JFrame("Полигон"); // создали фрейм
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PrintTest test1 = new PrintTest(states, tweets);
        f.setSize(1280,720);
        f.add(test1);
        f.pack(); // собрали
        f.setVisible(true);

    }
    public static ArrayList<Tweet> ReadTweets(String name) {
        BufferedReader reader;
        ArrayList<Tweet> tweets = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(name));
            String line = reader.readLine();

            while (line != null) {
                // read next line.
                String[] tweet = line.split("\t");
                tweet[0] = tweet[0].replaceAll("[,\\[\\]]", "");
                double[] coords = Arrays.stream(tweet[0].split(" "))
                        .mapToDouble(Double::parseDouble).toArray();
                DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = date1.parse(tweet[2]);
                tweet[3] = tweet[3].replaceAll("[^\\da-zA-Za ]", "");
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
            return (HashMap<String, ArrayList>) map;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}