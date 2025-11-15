-- src/main/resources/init.sql
--DROP TABLE IF EXISTS players;

CREATE TABLE players (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    height DOUBLE,           -- in cm
    weight DOUBLE,           -- in kg
    wingspan DOUBLE,         -- in cm
    handedness VARCHAR(10),  -- 'LEFT' or 'RIGHT'
    max_vertical_leap DOUBLE,-- in cm
    stamina INT,             -- 0-100
    agility INT,             -- 0-100
    speed INT,               -- 0-100
    photo_path VARCHAR(255)  -- e.g., "/photos/jordan.jpg"
);