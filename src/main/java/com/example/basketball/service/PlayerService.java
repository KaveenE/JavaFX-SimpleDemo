// src/main/java/com/example/basketball/service/PlayerService.java
package com.example.basketball.service;

import com.example.basketball.model.Player;
import com.example.basketball.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    public void addPlayer(Player player) {
        String sql = "INSERT INTO players (name, age, position, points_per_game) VALUES (?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, player.getName());
            ps.setInt(2, player.getAge());
            ps.setString(3, player.getPosition());
            ps.setDouble(4, player.getPointsPerGame());

            ps.executeUpdate();

            // retrieve generated id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    player.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add player", e);
        }
    }

    public List<Player> getAllPlayers() {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT id, name, age, position, points_per_game FROM players ORDER BY name";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player p = new Player();
                p.setId(rs.getLong("id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setPosition(rs.getString("position"));
                p.setPointsPerGame(rs.getDouble("points_per_game"));
                list.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load players", e);
        }
        return list;
    }
}