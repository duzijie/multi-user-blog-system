package com.yilan.blog.dao;

import com.yilan.blog.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {
	
}
