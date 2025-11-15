package com.example.basketball.model.enums;

public enum Handedness {
    LEFT("Left-Handed"),
    RIGHT("Right-Handed");

    private final String displayName;

    Handedness(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Handedness fromString(String text) {
        for (Handedness h : values()) {
            if (h.displayName.equalsIgnoreCase(text) || h.name().equalsIgnoreCase(text)) {
                return h;
            }
        }
        throw new IllegalArgumentException("Unknown handedness: " + text);
    }
}
