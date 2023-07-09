package com.yilan.blog.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.StyleDao;
import com.yilan.blog.entity.StyleEntity;
import com.yilan.blog.service.StyleService;


@Service("styleService")
public class StyleServiceImpl extends ServiceImpl<StyleDao, StyleEntity> implements StyleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StyleEntity> page = this.page(
                new Query<StyleEntity>().getPage(params),
                new QueryWrapper<StyleEntity>()
        );

        return new PageUtils(page);
    }

}