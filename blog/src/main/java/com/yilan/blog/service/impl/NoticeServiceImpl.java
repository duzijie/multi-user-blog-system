package com.yilan.blog.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.NoticeDao;
import com.yilan.blog.entity.NoticeEntity;
import com.yilan.blog.service.NoticeService;


@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, NoticeEntity> implements NoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<NoticeEntity> page = this.page(
                new Query<NoticeEntity>().getPage(params),
                new QueryWrapper<NoticeEntity>()
        );

        return new PageUtils(page);
    }

    //获取需要展示的状态为1的公告
    public List<NoticeEntity> noticeList() {
        List<NoticeEntity> entities = baseMapper.selectList(new QueryWrapper<NoticeEntity>().eq("status",1));

        return entities;
    }

    //获取全部已有公共
    public List<NoticeEntity> getAllNotice() {
        List<NoticeEntity> entities = baseMapper.selectList(new QueryWrapper<NoticeEntity>().in("status",0,1));

        return entities;
    }
}