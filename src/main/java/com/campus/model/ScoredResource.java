package com.campus.model;

public class ScoredResource {
    LearningResource resource;
    double score;

    public ScoredResource(LearningResource resource, double score) {
        this.resource = resource;
        this.score = score;
    }

    public LearningResource getResource() {
        return resource;
    }

    public double getScore() {
        return score;
    }
}
