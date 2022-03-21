package com.echoxxzhang.dao.elasticsearch;

import com.echoxxzhang.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * es也可以当作一种数据库使用，所以加上Repository注解
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
