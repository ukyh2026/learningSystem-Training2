package com.campus.dao;

import com.campus.model.LearningResource;

import java.util.List;

public interface ResourceDao {
    int generateId();
    void addResource(LearningResource resource);
    LearningResource getResourceByID(int id);
    List<LearningResource> getAlResource();
    void updateResource(LearningResource resource);
    void deleteResource(int id);

}
