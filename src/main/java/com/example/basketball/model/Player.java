// src/main/java/com/example/basketball/model/Player.java
package com.example.basketball.model;

import com.example.basketball.model.enums.Handedness;

public class Player {
    private Long id;
    private String name;
    private int age;
    private double height;
    private double weight;
    private double wingspan;
    private Handedness handedness;
    private double maxVerticalLeap;
    private int stamina;
    private int agility;
    private int speed;
    private String photoPath;

    // Constructors
    public Player() {}

    public Player(String name, int age, double height, double weight, double wingspan,
                  Handedness handedness, double maxVerticalLeap, int stamina, int agility, int speed, String photoPath) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.wingspan = wingspan;
        this.handedness = handedness;
        this.maxVerticalLeap = maxVerticalLeap;
        this.stamina = stamina;
        this.agility = agility;
        this.speed = speed;
        this.photoPath = photoPath;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getWingspan() { return wingspan; }
    public void setWingspan(double wingspan) { this.wingspan = wingspan; }

    public Handedness getHandedness() { return handedness; }
    public void setHandedness(Handedness handedness) { this.handedness = handedness; }

    public double getMaxVerticalLeap() { return maxVerticalLeap; }
    public void setMaxVerticalLeap(double maxVerticalLeap) { this.maxVerticalLeap = maxVerticalLeap; }

    public int getStamina() { return stamina; }
    public void setStamina(int stamina) { this.stamina = stamina; }

    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    @Override
    public String toString() {
        return String.format(
                "Player[id=%d, name=%s, age=%d, height=%.2f, weight=%.2f, " +
                        "wingspan=%.2f, handedness=%s, maxVerticalLeap=%.2f, " +
                        "stamina=%d, agility=%d, speed=%d, photoPath=%s]",
                id, name, age, height, weight, wingspan,
                handedness, maxVerticalLeap, stamina, agility, speed, photoPath
        );
    }
}