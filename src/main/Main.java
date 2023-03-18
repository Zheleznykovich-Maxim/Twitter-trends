package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ArrayList<Tweet> tweets = ReadFile("./src/resources/cali_tweets2014.txt");
//        Date d = new Date();
//        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
//        System.out.println(format1.format(d));
        for (Tweet tweet : tweets){
            System.out.println(tweet.toString());
        }

    }
    public static ArrayList<Tweet> ReadFile(String name) {
        BufferedReader reader;
        ArrayList<Tweet> tweets = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(name));
            String line = reader.readLine();

            while (line != null) {

//                System.out.println(line);
                // read next line
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
}