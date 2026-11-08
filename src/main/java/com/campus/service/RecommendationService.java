package com.campus.service;

import com.campus.model.LearningResource;
import com.campus.model.ScoredResource;

import java.util.*;

public class RecommendationService {
    public double calculateJaccard(Set<String> userTags,Set<String> resourceTags){
        if(userTags ==null ||resourceTags == null ||userTags.isEmpty()||resourceTags.isEmpty())
            return 0;
        Set<String> intersectiion = new HashSet<>(userTags);
        intersectiion.retainAll(resourceTags);

        Set<String> union = new HashSet<>(userTags);
        union.addAll(resourceTags);
        return  (intersectiion.size()*1.0/union.size());
    }

    public List<LearningResource>  getTopRecommendations(Set<String> userTags,List<LearningResource> allResources,int n){
        List<ScoredResource> scoredResources = new ArrayList<>();
        if (allResources == null || allResources.isEmpty() || n <= 0) {
            return new ArrayList<>();
        }

        for(LearningResource resource:allResources){
            double score = calculateJaccard(userTags,resource.getTags());
            if (score > 0)
                scoredResources.add(new ScoredResource(resource,score));
        }
        //排序的传统写法
//        scoredResources.sort(new Comparator<ScoredResource>() {
//            @Override
//            public int compare(ScoredResource o1, ScoredResource o2) {
//              return  Double.compare(o2.getScore(),o1.getScore());
//
//            }
//        });
        //排序的简洁写法
       scoredResources.sort(Comparator.comparingDouble(ScoredResource::getScore).reversed());
        int count = Math.min(n,scoredResources.size());
        List<LearningResource> topNList = new ArrayList<>();
        for(int i=0;i<count;i++){
            topNList.add(scoredResources.get(i).getResource());
        }
        return  topNList;
    }
}
