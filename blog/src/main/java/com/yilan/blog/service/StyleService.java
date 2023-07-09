package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.StyleEntity;

import java.util.Map;

/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
public interface StyleService extends IService<StyleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

