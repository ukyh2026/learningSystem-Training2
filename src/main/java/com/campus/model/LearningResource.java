package com.campus.model;

import java.util.HashSet;
import java.util.Set;

public class LearningResource {
    private int id;
    private String title;
    private String category;
    private Set<String> tags;
    private int heatScore;

    public LearningResource(int id, String title, String category, Set<String> tags, int heatScore) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.tags = tags;
        this.heatScore = heatScore;
    }

    public LearningResource(int id, String title, String category, int heatScore) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.heatScore = heatScore;
        this.tags= new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getHeatScore() {
        return heatScore;
    }

  public void addTag(String tag){
        this.tags.add(tag);
  }
}
