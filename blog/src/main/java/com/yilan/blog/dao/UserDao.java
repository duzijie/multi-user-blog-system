package com.yilan.blog.dao;

import com.yilan.blog.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	List<UserEntity> searchUsersByParams(UserEntity userEntity);
	List<UserEntity> searchAdminsByParams(UserEntity userEntity);
	List<UserEntity> findUsersByPages(UserEntity userEntity,int pageCount, int pageSize);
}
