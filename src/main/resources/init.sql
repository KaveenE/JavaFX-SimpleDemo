CREATE TABLE IF NOT EXISTS players (
    id IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    position VARCHAR(20),
    points_per_game DOUBLE
);