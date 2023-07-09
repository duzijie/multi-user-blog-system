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

import com.yilan.blog.entity.LetterEntity;
import com.yilan.blog.service.LetterService;
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
@RequestMapping("blogsystem/letter")
public class LetterController {
    @Autowired
    private LetterService letterService;

    /**
     * 展示留言页面所需的数据
     * @param token
     */

    @RequestMapping("/searchUsersByToken")
    public R searchUsersByToken(HttpServletRequest request){
        return letterService.searchUsersByToken(request);
    }

    /**
     * 点击左侧用户头像时 接受前端发送过来的user的id，展示留言内容 并把未读改为已读
     * @param userId,token
     * @return
     */
    @RequestMapping("/showLettersByUserId")
    public R showLettersByUserId(HttpServletRequest request, @RequestParam("userId") int userId){
        return letterService.showLettersByUserId(request,userId);
    }

    /**
     * 添加私信
     * @param 从token中获取from_user_id, to_user_id, content
     */
    @RequestMapping("/addLetterToUserId")
    public R addLetterToUserId(HttpServletRequest request, @RequestBody LetterEntity letterEntity){
        return letterService.addLetterToUserId(request, letterEntity);
    }

    /**
     * 把未读消息置为已读
     * @param userId,token
     */





    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = letterService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		LetterEntity letter = letterService.getById(id);

        return R.ok().put("letter", letter);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LetterEntity letter){
		letterService.save(letter);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody LetterEntity letter){
		letterService.updateById(letter);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		letterService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
