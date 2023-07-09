package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.CatalogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
public interface CatalogService extends IService<CatalogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CatalogEntity> listWithTree();


}

