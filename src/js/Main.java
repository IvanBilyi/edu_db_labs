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