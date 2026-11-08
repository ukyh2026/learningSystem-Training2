package com.campus.index;

import com.campus.model.LearningResource;

import java.util.*;

public class InvertedIndex {
    Map<String, Set<Integer>> invertedIndex =  new HashMap<>();

    public InvertedIndex() {
    }

    public void addResource(LearningResource resource){
        Set<String> keywords  = new HashSet<>();
        String title = resource.getTitle();
        if(!title.isEmpty()){
            Collections.addAll(keywords,title.split("\\s+"));
        }
        Set<String> tags = resource.getTags();
        keywords.addAll(tags);

        for(String key:keywords){
            key = key.trim().toLowerCase();

            invertedIndex.computeIfAbsent(key,k->new HashSet<>()).add(resource.getId());
        }
    }
    public void removeResource(LearningResource resource){//这种算法有一个弊端：
                                                            // 它需要遍历所有的 map 集合 即使这个资源只和三条相关的 map 记录有关
                                                            // 它也要扫描所有的 map 集合元素 比如 10 万条
        Set<String> keywords  = new HashSet<>();
        String title = resource.getTitle();
        int id = resource.getId();
        if(!title.isEmpty()){
            Collections.addAll(keywords,title.split("\\s+"));
        }
        Set<String> tags = resource.getTags();
        if(tags !=null)
            keywords.addAll(tags);
        for(String key:keywords){
            key = key.trim().toLowerCase();

            Set<Integer> idSet = invertedIndex.get(key) ;
            if(idSet!=null){
                idSet.remove(id);
                if(idSet.isEmpty())
                    invertedIndex.remove(key);
            }
        }

    }

    public Set<Integer> search(String keyword){
        keyword = keyword.trim().toLowerCase();
        return invertedIndex.getOrDefault(keyword,Collections.emptySet());
    }

}
