package main;

public class State {
    private long id;
    private String WA;
    private String LA;
    private String NY;
        // Getters and Setters
    public String toString(){
        return String.format("id=%d name=%s", id, WA);
    }
}
