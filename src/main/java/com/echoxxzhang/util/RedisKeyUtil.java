package com.echoxxzhang.util;

/**
 * 用于返回“赞”，可分为实体的赞与用户的赞
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";  // 分隔符
    private static final String PREFIX_ENTITY_LIKE = "like:entity";  // key的name
    private static final String PREFIX_USER_LIKE = "like:user";

    // 某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

}
