package com.campus.dao.impl;

import com.campus.dao.ResourceDao;
import com.campus.model.LearningResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryResourceDao implements ResourceDao {
    Map<Integer,LearningResource> resources = new HashMap<>();
    private int nextID = 1;
    @Override
    public int generateId() {
        return nextID++;
    }

    @Override
    public void addResource(LearningResource resource) {
        resources.put(resource.getId(),resource);
    }

    @Override
    public LearningResource getResourceByID(int id) {
        return resources.get(id);

    }

    @Override
    public List<LearningResource> getAlResource() {
        return new ArrayList<>(resources.values()) ;

    }

    @Override
    public void updateResource(LearningResource resource) {
        if(resources.containsKey(resource.getId()))
            resources.put(resource.getId(),resource);
    }

    @Override
    public void deleteResource(int id) {
        resources.remove(id);
    }
}
