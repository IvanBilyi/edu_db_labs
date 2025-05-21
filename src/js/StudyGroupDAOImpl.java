package com.example.dao.impl;

import com.example.dao.StudyGroupDAO;
import com.example.model.StudyGroup;
import com.example.util.DatabaseConnection;
import com.example.util.UUIDUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudyGroupDAOImpl implements StudyGroupDAO {

    private Connection conn;

    public StudyGroupDAOImpl() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public void addStudyGroup(StudyGroup group) throws SQLException {
        String sql = "INSERT INTO study_group (id, name, year) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, UUIDUtil.toBytes(group.getId()));
            stmt.setString(2, group.getName());
            stmt.setInt(3, group.getYear());
            stmt.executeUpdate();
        }
    }

    @Override
    public StudyGroup getStudyGroupById(UUID id) throws SQLException {
        String sql = "SELECT * FROM study_group WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, UUIDUtil.toBytes(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new StudyGroup(
                        UUIDUtil.fromBytes(rs.getBytes("id")),
                        rs.getString("name"),
                        rs.getInt("year")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<StudyGroup> getAllStudyGroups() throws SQLException {
        List<StudyGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM study_group";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groups.add(new StudyGroup(
                    UUIDUtil.fromBytes(rs.getBytes("id")),
                    rs.getString("name"),
                    rs.getInt("year")
                ));
            }
        }
        return groups;
    }

    @Override
    public void updateStudyGroup(StudyGroup group) throws SQLException {
        String sql = "UPDATE study_group SET name = ?, year = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getYear());
            stmt.setBytes(3, UUIDUtil.toBytes(group.getId()));
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteStudyGroup(UUID id) throws SQLException {
        String sql = "DELETE FROM study_group WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, UUIDUtil.toBytes(id));
            stmt.executeUpdate();
        }
    }
}
