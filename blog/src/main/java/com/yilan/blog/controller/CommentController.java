package com.yilan.blog.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yilan.blog.entity.CommentEntity;
import com.yilan.blog.service.CommentService;
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
@RequestMapping("blogsystem/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 1.根据blog_id查找出所有comment记录，得到该博文下的所有评论，返回List<Comment>
     * 2.找到所有一级评论
     * 3.递归找出所有一级评论的子评论
     * @Param blogId
     */
    @RequestMapping("/listWithTree")
    public R listWithTree(@RequestParam int blogId) {
        return commentService.listWithTree(blogId);
    }

    /**
     * 增加一条评论
     * 前端应该给后端传回 content token  parent_id blog_id
     * 然后返回要添加数据的实体类
     */
    @RequestMapping("/saveCommentByBlogId")
    public R saveCommentByBlogId(@RequestBody CommentEntity commentEntity, HttpServletRequest request){
        return commentService.saveCommentByBlogId(commentEntity, request);
    }

    /**
     * 用户本人删除属于自己的一条评论  或者管理员删除评论
     * 参数需要：id token
     */
    @RequestMapping("/deleteComment")
    public R deleteComment(@RequestBody CommentEntity commentEntity, HttpServletRequest request) {
        return commentService.deleteComment(commentEntity,request);
    }

    /**
     * 根据某条评论的id，返回 userId和role
     * 需要的参数：comment表的id
     */
    @Autowired
    private UserService userService;
    @RequestMapping("/searchUserById")
    public R searchUserById(@RequestBody CommentEntity commentEntity){
        return commentService.searchUserById(commentEntity);
    }

    /**
     * 评论表根据id，查找出UserEntity的username和avatar
     * 参数要有userId
     */
    @RequestMapping("/searchUsernameAndAvatarByUserId")
    public R searchUsernameAndAvatarByUserId(@RequestBody CommentEntity commentEntity){
        return commentService.searchUsernameAndAvatarByUserId(commentEntity);
    }







    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		CommentEntity comment = commentService.getById(id);

        return R.ok().put("comment", comment);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CommentEntity comment){
		commentService.save(comment);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CommentEntity comment){
		commentService.updateById(comment);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		commentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
