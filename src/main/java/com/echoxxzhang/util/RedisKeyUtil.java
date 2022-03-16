package com.echoxxzhang.util;

/**
 * 用于返回“赞”，可分为实体的赞与用户的赞
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";  // 分隔符
    private static final String PREFIX_ENTITY_LIKE = "like:entity";  // 实体的赞
    private static final String PREFIX_USER_LIKE = "like:user"; // 用户的赞
    private static final String PREFIX_FOLLOWEE = "followee"; // 关注者
    private static final String PREFIX_FOLLOWER = "follower"; // 粉丝


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


    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }


}
