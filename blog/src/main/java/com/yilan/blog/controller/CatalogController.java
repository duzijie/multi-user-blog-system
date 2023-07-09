package com.yilan.blog.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yilan.blog.entity.CatalogEntity;
import com.yilan.blog.service.CatalogService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.R;



/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@RestController
@RequestMapping("blogsystem/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = catalogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		CatalogEntity catalog = catalogService.getById(id);

        return R.ok().put("catalog", catalog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CatalogEntity catalog){
		catalogService.save(catalog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CatalogEntity catalog){
		catalogService.updateById(catalog);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		catalogService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    //把分类按照三级分类方式，以树形结构返回
    @RequestMapping("/list/tree")
    public R listWithTree(){
        List<CatalogEntity> entities = catalogService.listWithTree();
        return R.ok().put("catalogEntityList",entities);
    }


}
