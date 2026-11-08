package com.campus.recommend;

import com.campus.model.BehaviorType;
import com.campus.model.LearningResource;
import com.campus.model.User;
import com.campus.model.UserBehavior;
import com.campus.repository.BehaviorRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
//协同国力推荐策略
public class CollaborativeFilteringStrategy implements RecommendStrategy{
    BehaviorRepository behaviorRepository ;

    public CollaborativeFilteringStrategy(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }
    private  double getBehaviorWeight(BehaviorType type){
        switch (type){
            case VIEW : return 1;
            case LOOK:  return 1;
            case FAVORITE:  return 3;
            case DOWNLOAD : return 5;
            default:return  0;
        }
        //这个地方可以用 Lambda 表达式写如下
//        return switch (type){
//            case VIEW,LOOK ->1;
//            case FAVORITE-> 3;
//            case DOWNLOAD->5;
//            default->0;
//        };

    }
//    把全部行为记录转换成用户—资源评分
    public Map<Integer,Map<Integer,Double>> buildRatingMatrix(){
        Map<Integer ,Map<Integer,Double>> matrix = new HashMap<>();
        List<UserBehavior> allBehaviors = behaviorRepository.findAll();
        for(UserBehavior behavior:allBehaviors){
            int userID = behavior.userID();
            int resourceID = behavior.resourceID();
            double weight = getBehaviorWeight(behavior.type());

            Map<Integer,Double> userRating = matrix.get(userID);
            if (userRating == null) {
                userRating = new HashMap<>();
                matrix.put(userID,userRating);
            }
            double oldScore = userRating.getOrDefault(resourceID ,0.0);
            userRating.put(resourceID,oldScore+weight);
        }
        return matrix;
    }
    private double cosineSimilarity(Map<Integer, Double> ratingsA, Map<Integer, Double> ratingsB){
        double dotProduct=0,sumSquareA=0,sumSquareB=0;
        if(ratingsA== null || ratingsB == null|| ratingsA.isEmpty()|| ratingsB.isEmpty())
            return 0;
        for(int resourceID:ratingsA.keySet()){
           if(! ratingsB.containsKey(resourceID))
               continue;
           double scoreA = ratingsA.get(resourceID);
           double scoreB = ratingsB.get(resourceID);
           dotProduct += scoreA*scoreB;
            sumSquareA +=  scoreA*scoreA;
            sumSquareB += scoreB*scoreB;

        }
        if(sumSquareA ==0 || sumSquareB ==0)
            return 0;
        return dotProduct/(Math.sqrt(sumSquareA)*Math.sqrt(sumSquareB));

    }
    @Override
    public Map<Integer, Double> scoreAll(User user, List<LearningResource> allResources) {
        return Map.of();
    }
}
