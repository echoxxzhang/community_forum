package com.echoxxzhang.dao;

import com.echoxxzhang.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 根据分页查询文章（）
     * @param userId 可选：是否根据userId查询(“我的主页”功能)
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 查询文章条目数
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);
    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且会在<if>里使用,则必须加别名.

    /**
     * 插入一条文章
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    /**
     * 根据postID查询文章
     * @param id
     * @return
     */
    DiscussPost selectDiscussPostById(int id);

}
