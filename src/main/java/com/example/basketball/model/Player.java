// src/main/java/com/example/basketball/model/Player.java
package com.example.basketball.model;

public class Player {
    private Long id;
    private String name;
    private int age;
    private String position;
    private double pointsPerGame;

    // ---- constructors -------------------------------------------------
    public Player() {}

    public Player(String name, int age, String position, double pointsPerGame) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.pointsPerGame = pointsPerGame;
    }

    // ---- getters / setters --------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public double getPointsPerGame() { return pointsPerGame; }
    public void setPointsPerGame(double pointsPerGame) { this.pointsPerGame = pointsPerGame; }

    @Override
    public String toString() {
        return name + " (#" + id + ") – " + position + ", PPG: " + pointsPerGame;
    }
}