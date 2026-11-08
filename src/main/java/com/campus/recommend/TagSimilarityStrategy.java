package com.campus.recommend;

import com.campus.model.LearningResource;
import com.campus.model.User;
import com.campus.service.RecommendationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagSimilarityStrategy implements RecommendStrategy {
    RecommendationService service;
    public TagSimilarityStrategy() {
        this(new RecommendationService());
    }
    public TagSimilarityStrategy(RecommendationService recommendationService) {
        this.service = recommendationService;
    }

    @Override
    public Map<Integer, Double> scoreAll(User user, List<LearningResource> allResources) {
       Map<Integer,Double> scoreAll = new HashMap<>();
       if(allResources == null || user == null) return scoreAll;

        for(LearningResource resource:allResources){
            scoreAll.put(resource.getId(),this.service.calculateJaccard(user.getTags(),resource.getTags()));
        }
        return scoreAll;
    }
}
