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
