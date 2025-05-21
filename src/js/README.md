# Звіт по роботі з базою даних "Система управління учбовими процесами"

## Опис коду Java

### 1. Модель `StudyGroup.java`

```java
package com.example.model;

import java.util.UUID;

public class StudyGroup {
    private UUID id;
    private String name;
    private int year;

    public StudyGroup() {}

    public StudyGroup(UUID id, String name, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
```

---

### 2. Утилітний клас `UUIDUtil.java`

```java
package com.example.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtil {

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID fromBytes(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}
```

---

### 3. Клас підключення до бази `DatabaseConnection.java`

```java
package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/studygroup";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
```

---

### 4. DAO інтерфейс `StudyGroupDAO.java`

```java
package com.example.dao;

import com.example.model.StudyGroup;
import java.util.List;
import java.util.UUID;

public interface StudyGroupDAO {
    void addStudyGroup(StudyGroup group) throws Exception;
    StudyGroup getStudyGroupById(UUID id) throws Exception;
    List<StudyGroup> getAllStudyGroups() throws Exception;
    void updateStudyGroup(StudyGroup group) throws Exception;
    void deleteStudyGroup(UUID id) throws Exception;
}
```

---

### 5. Реалізація DAO `StudyGroupDAOImpl.java`

```java
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
```

---

### 6. Приклад використання DAO `Main.java`

```java
package com.example;

import com.example.dao.StudyGroupDAO;
import com.example.dao.impl.StudyGroupDAOImpl;
import com.example.model.StudyGroup;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try {
            StudyGroupDAO dao = new StudyGroupDAOImpl();

            // Додати групу
            StudyGroup group = new StudyGroup(UUID.randomUUID(), "IPZ-21", 2021);
            dao.addStudyGroup(group);

            // Вивести всі групи
            List<StudyGroup> groups = dao.getAllStudyGroups();
            groups.forEach(g -> System.out.println(g.getName() + " - " + g.getYear()));

            // Оновити групу
            group.setYear(2022);
            dao.updateStudyGroup(group);

            // Видалити групу
            dao.deleteStudyGroup(group.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
