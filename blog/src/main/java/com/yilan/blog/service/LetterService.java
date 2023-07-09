package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.LetterEntity;
import com.yilan.blog.utils.R;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
public interface LetterService extends IService<LetterEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R searchUsersByToken(HttpServletRequest request);

    R showLettersByUserId(HttpServletRequest request, int userId);

    R addLetterToUserId(HttpServletRequest request, LetterEntity to_user_id);
}

