// src/main/java/com/example/basketball/service/PlayerService.java
package com.example.basketball.service;

import com.example.basketball.model.Player;
import com.example.basketball.model.enums.Handedness;
import com.example.basketball.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PlayerService {

    private static final String INSERT_SQL = """
        INSERT INTO players
        (name, age, height, weight, wingspan, handedness, max_vertical_leap, stamina, agility, speed, photo_path)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String SELECT_ALL = "SELECT * FROM players ORDER BY name";

    public void addPlayer(Player player) {
        System.out.println("Executing: " + INSERT_SQL);
        System.out.printf("Params: %s, %d, %.2f, %.2f, %.2f, %s, %.2f, %d, %d, %d, %s%n",
                player.getName(), player.getAge(), player.getHeight(), player.getWeight(),
                player.getWingspan(), player.getHandedness(),
                player.getMaxVerticalLeap(), player.getStamina(), player.getAgility(),
                player.getSpeed(), player.getPhotoPath());
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, player.getName());
            ps.setInt(2, player.getAge());
            ps.setDouble(3, player.getHeight());
            ps.setDouble(4, player.getWeight());
            ps.setDouble(5, player.getWingspan());

            if (player.getHandedness() != null) {
                ps.setString(6, player.getHandedness().name());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setDouble(7, player.getMaxVerticalLeap());
            ps.setInt(8, player.getStamina());
            ps.setInt(9, player.getAgility());
            ps.setInt(10, player.getSpeed());

            if (player.getPhotoPath() != null && !player.getPhotoPath().isEmpty()) {
                ps.setString(11, player.getPhotoPath());
            } else {
                ps.setNull(11, Types.VARCHAR);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) player.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add player", e);
        }
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player p = new Player();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setHeight(rs.getDouble("height"));
                p.setWeight(rs.getDouble("weight"));
                p.setWingspan(rs.getDouble("wingspan"));
                p.setHandedness(
                        rs.getString("handedness") != null
                                ? Handedness.valueOf(rs.getString("handedness"))
                                : null
                );
                p.setMaxVerticalLeap(rs.getDouble("max_vertical_leap"));
                p.setStamina(rs.getInt("stamina"));
                p.setAgility(rs.getInt("agility"));
                p.setSpeed(rs.getInt("speed"));
                p.setPhotoPath(rs.getString("photo_path"));
                players.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load players", e);
        }
        return players;
    }

    // add these two methods to PlayerService.java (keep the rest unchanged)

    private static final String UPDATE_SQL = """
    UPDATE players SET
        name = ?, age = ?, height = ?, weight = ?, wingspan = ?,
        handedness = ?, max_vertical_leap = ?, stamina = ?, agility = ?, speed = ?, photo_path = ?
    WHERE id = ?
    """;

    private static final String DELETE_SQL = "DELETE FROM players WHERE id = ?";

    public void updatePlayer(Player p) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setDouble(3, p.getHeight());
            ps.setDouble(4, p.getWeight());
            ps.setDouble(5, p.getWingspan());
            ps.setString(6, p.getHandedness() != null ? p.getHandedness().name() : null);
            ps.setDouble(7, p.getMaxVerticalLeap());
            ps.setInt(8, p.getStamina());
            ps.setInt(9, p.getAgility());
            ps.setInt(10, p.getSpeed());
            ps.setString(11, p.getPhotoPath());
            ps.setLong(12, p.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update player", e);
        }
    }

    public void deletePlayer(Long id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete player", e);
        }
    }
}