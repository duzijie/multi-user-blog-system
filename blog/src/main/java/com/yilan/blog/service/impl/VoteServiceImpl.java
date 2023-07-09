package com.yilan.blog.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.VoteDao;
import com.yilan.blog.entity.VoteEntity;
import com.yilan.blog.service.VoteService;


@Service("voteService")
public class VoteServiceImpl extends ServiceImpl<VoteDao, VoteEntity> implements VoteService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VoteEntity> page = this.page(
                new Query<VoteEntity>().getPage(params),
                new QueryWrapper<VoteEntity>()
        );

        return new PageUtils(page);
    }

}