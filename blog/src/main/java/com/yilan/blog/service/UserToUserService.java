package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.UserToUserEntity;
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
public interface UserToUserService extends IService<UserToUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R isFollow(HttpServletRequest request, int to_user_id);

    R deleteFollow(HttpServletRequest request, int to_user_id);
}

