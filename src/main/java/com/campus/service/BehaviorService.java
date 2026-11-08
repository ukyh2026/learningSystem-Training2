package com.campus.service;

import com.campus.model.BehaviorType;
import com.campus.model.User;
import com.campus.model.UserBehavior;
import com.campus.repository.BehaviorRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BehaviorService {
    BehaviorRepository behaviorRepository;

    public BehaviorService(BehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }
    public void record(int  userID, int resourceID, BehaviorType type){
        UserBehavior userBehavior = new UserBehavior(userID, resourceID,type, LocalDateTime.now());
        behaviorRepository.save(userBehavior);
    }

    public Set<Integer> consumeResourcedIds(int userID){
       // 这种简洁版，学生理解起来没有 foreach 容易
        //return behaviorRepository.findByUserID(userID).stream().map(UserBehavior::resourceID).collect(Collectors.toSet());
        List<UserBehavior> behaviors = behaviorRepository.findByUserID(userID);
        Set<Integer> resourceIds = new HashSet<>();
        for(UserBehavior behavior:behaviors){
            resourceIds.add(behavior.resourceID());
        }
        return resourceIds;

    }
}
