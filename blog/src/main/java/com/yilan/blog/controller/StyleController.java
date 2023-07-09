package com.yilan.blog.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yilan.blog.entity.StyleEntity;
import com.yilan.blog.service.StyleService;
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
@RequestMapping("blogsystem/style")
public class StyleController {
    @Autowired
    private StyleService styleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = styleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		StyleEntity style = styleService.getById(id);

        return R.ok().put("style", style);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StyleEntity style){
		styleService.save(style);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StyleEntity style){
		styleService.updateById(style);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		styleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
