package com.yilan.blog.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.CatalogDao;
import com.yilan.blog.entity.CatalogEntity;
import com.yilan.blog.service.CatalogService;


@Service("catalogService")
public class CatalogServiceImpl extends ServiceImpl<CatalogDao, CatalogEntity> implements CatalogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CatalogEntity> page = this.page(
                new Query<CatalogEntity>().getPage(params),
                new QueryWrapper<CatalogEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CatalogEntity> listWithTree() {
        //1、查出所有分类  因为这个类extends了ServiceImpl，所以baseMapper相当于CategoryDao 省去了以往需要注入才能使用的动作
        List<CatalogEntity> entities = baseMapper.selectList(null);
        //2、组装成父子的树形结构

        //2.1)、找到所有的一级分类
        List<CatalogEntity> level1Menus = entities.stream().filter(catalogEntity ->
                catalogEntity.getParentId() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).collect(Collectors.toList());
        return level1Menus;
    }
    //递归查找所有菜单的子菜单
    private List<CatalogEntity> getChildrens(CatalogEntity root, List<CatalogEntity> all){
        List<CatalogEntity> children = all.stream().filter(catalogEntity -> {
            return catalogEntity.getParentId() == root.getId();
        }).map(catalogEntity -> {
            //1、找到子菜单
            catalogEntity.setChildren(getChildrens(catalogEntity,all));
            return catalogEntity;
        }).collect(Collectors.toList());

        return children;
    }

}