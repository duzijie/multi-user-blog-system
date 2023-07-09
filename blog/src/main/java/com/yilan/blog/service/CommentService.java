package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.CommentEntity;
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
public interface CommentService extends IService<CommentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R listWithTree(int blogId);

    R saveCommentByBlogId(CommentEntity commentEntity, HttpServletRequest request);

    R deleteComment(CommentEntity commentEntity, HttpServletRequest request);

    R searchUsernameAndAvatarByUserId(CommentEntity commentEntity);

    R searchUserById(CommentEntity commentEntity);
}

