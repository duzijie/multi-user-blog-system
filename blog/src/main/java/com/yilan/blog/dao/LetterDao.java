package com.yilan.blog.dao;

import com.yilan.blog.entity.LetterEntity;
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
public interface LetterDao extends BaseMapper<LetterEntity> {
	List<Integer> searchIdsLetterToMe(int to_user_id);
}
