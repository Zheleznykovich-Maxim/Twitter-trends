package main;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Tweet> tweets = ReadTweets("./src/resources/cali_tweets2014.txt");
//        Date d = new Date();
//        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
//        System.out.println(format1.format(d));
//        for (Tweet tweet : tweets){
//            System.out.println(tweet.toString());
//        }
        HashMap<String, Double> emotions = ReadCSV("./src/resources/sentiments.csv");
        for (Tweet tweet : tweets) {
            tweet.CalcEmotion(emotions);
            System.out.println(tweet.getEmotion());
        }


//        List<String> mapKeys = new ArrayList<>(emotions.keySet());
//        Collections.sort(mapKeys);
//        System.out.println(mapKeys);
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
                double[] latitude = Arrays.stream(tweet[0].split(" "))
                        .mapToDouble(Double::parseDouble).toArray();
                DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = date1.parse(tweet[2]);
                tweets.add(new Tweet(latitude, date, tweet[3]));
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
}