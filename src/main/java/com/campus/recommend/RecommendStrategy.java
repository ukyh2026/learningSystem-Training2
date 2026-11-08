package com.campus.recommend;

import com.campus.model.LearningResource;
import com.campus.model.User;

import java.util.List;
import java.util.Map;

//RecommendStrategy 是所有推荐策略共同遵守的规则。
// 本模块只定义“推荐算法应该能做什么”，不规定具体怎样计算分数。
public interface RecommendStrategy {
    Map<Integer,Double> scoreAll(User user, List<LearningResource> allResources);
}
