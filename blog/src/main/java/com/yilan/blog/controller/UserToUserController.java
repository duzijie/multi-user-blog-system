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

import com.yilan.blog.entity.UserToUserEntity;
import com.yilan.blog.service.UserToUserService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.R;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@RestController
@RequestMapping("blogsystem/usertouser")
public class UserToUserController {
    @Autowired
    private UserToUserService userToUserService;

    /**
     * 判断用户之间是否已关注，返回用户之间的关系状态值，0-好友，1-申请中（1作废了），2-fromUser屏蔽toUser
     * @param to_user_id, token
     */
    @RequestMapping("/isFollow")
    public R isFollow(HttpServletRequest request, int to_user_id){
        return userToUserService.isFollow(request,to_user_id);
    }

    /**
     * 取消关注
     * @param token, to_user_id
     */
    @RequestMapping("/deleteFollow")
    public R deleteFollow(HttpServletRequest request, int to_user_id){
        return userToUserService.deleteFollow(request,to_user_id);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userToUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		UserToUserEntity userToUser = userToUserService.getById(id);

        return R.ok().put("userToUser", userToUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserToUserEntity userToUser){
		userToUserService.save(userToUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserToUserEntity userToUser){
		userToUserService.updateById(userToUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		userToUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
