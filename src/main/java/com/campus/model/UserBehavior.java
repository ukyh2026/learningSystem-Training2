package com.campus.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record UserBehavior(int userID, int resourceID, BehaviorType type, LocalDateTime occurredAt) {

    public UserBehavior{
        Objects.requireNonNull(type, "行为类型不能为空!");
        Objects.requireNonNull(occurredAt, "行为发生时间不能为空!");
        if (resourceID <= 0) {
            throw new IllegalArgumentException("学习资料ID必须为正数!");
        }

        if (userID <= 0) {
            throw new IllegalArgumentException("用户ID必须为正数!");
        }
    }
}
